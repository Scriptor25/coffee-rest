package io.scriptor.http;

import io.scriptor.annotation.*;
import io.scriptor.log.Log;
import io.scriptor.type.IConverter;
import io.scriptor.type.TypeRef;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.*;
import java.util.concurrent.*;

public class HTTPServer implements AutoCloseable {

    private record RouteBundle(
            Object instance,
            Method callee,
            HTTPRoute route,
            String accept,
            String result
    ) {
    }

    private final boolean enableTLS;

    private final ServerSocket serverSocket;

    private final Map<HTTPMethod, List<RouteBundle>> routes = new HashMap<>();
    private final Map<Type, Map<Type, IConverter<?, ?>>> converters = new HashMap<>();

    private final BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(256);
    private final Executor executor = new ThreadPoolExecutor(10,
                                                             10,
                                                             10,
                                                             TimeUnit.MINUTES,
                                                             workQueue);

    public HTTPServer(
            final int port,
            final boolean enableTLS,
            final String keystoreFilename,
            final String keystorePassphrase
    )
            throws CertificateException,
                   IOException,
                   KeyManagementException,
                   KeyStoreException,
                   NoSuchAlgorithmException,
                   UnrecoverableKeyException {

        this.enableTLS = enableTLS;

        final ServerSocketFactory serverSocketFactory;
        if (enableTLS) {
            final var passphrase = keystorePassphrase.toCharArray();
            final var keystore   = KeyStore.getInstance("JKS");
            keystore.load(new FileInputStream(keystoreFilename), passphrase);

            final var keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keystore, passphrase);

            final var sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            serverSocketFactory = sslContext.getServerSocketFactory();
        } else {
            serverSocketFactory = ServerSocketFactory.getDefault();
        }

        serverSocket = serverSocketFactory.createServerSocket(port);
        Log.info("HTTP%s server listening on port %d", enableTLS ? "S" : "", port);
    }

    public void registerRoute(
            final Object instance,
            final Method callee,
            final Endpoint endpoint,
            final Resource resource
    ) {
        routes.computeIfAbsent(resource.method(), _ -> new ArrayList<>())
              .add(new RouteBundle(instance,
                                   callee,
                                   new HTTPRoute(endpoint.value(), resource.path()),
                                   resource.accept(),
                                   resource.result()));
    }

    public <S, D> void registerConverter(
            final Type source,
            final Type destination,
            final IConverter<S, D> converter
    ) {
        converters.computeIfAbsent(source, _ -> new HashMap<>()).put(destination, converter);
    }

    public void handleRequest() throws IOException {
        final var socket = serverSocket.accept();
        executor.execute(() -> {
            try {
                if (enableTLS)
                    ((SSLSocket) socket).startHandshake();
                handleRequest(socket);
            } catch (final IOException e) {
                Log.trace(e);
            }
        });
    }

    public void start() throws IOException {
        while (!Thread.interrupted()) {
            handleRequest();
        }
    }

    @SuppressWarnings("unchecked")
    private <S, D> D convert(final S object, final Type source, final Type destination) {
        if (IConverter.isAssignable(destination, source))
            return (D) object;

        if (converters.containsKey(source) && converters.get(source).containsKey(destination))
            return ((IConverter<S, D>) converters.get(source).get(destination)).from(object);

        throw new IllegalStateException("unsupported conversion from '%s' to '%s'".formatted(source, destination));
    }

    private void handleRequest(final Socket socket) throws IOException {
        try (
                final var inputStream = socket.getInputStream();
                final var outputStream = socket.getOutputStream()
        ) {
            final var request = HTTPRequestMessage.read(inputStream);

            if (request == null) {
                Log.warning("invalid request: request is null");
                return;
            }

            Log.info("%s %s %s", request.method(), request.path(), request.protocol());

            for (final var bundle : routes.get(request.method())) {
                if (!bundle.route().matches(request.path()))
                    continue;

                final var parameterCount = bundle.callee().getParameterCount();
                final var parameters     = bundle.callee().getParameters();

                final var args = new Object[parameterCount];

                for (int i = 0; i < parameterCount; ++i) {
                    final var parameter = parameters[i];

                    final Object value;
                    if (parameter.isAnnotationPresent(Body.class)) {
                        value = request.body();
                    } else if (parameter.isAnnotationPresent(Header.class)) {
                        final var name = Objects.requireNonNull(parameter.getAnnotation(Header.class)).value();
                        value = request.headers().get(name.toLowerCase());
                    } else if (parameter.isAnnotationPresent(Path.class)) {
                        final var name = Objects.requireNonNull(parameter.getAnnotation(Path.class)).value();
                        value = bundle.route().get(request.path(), name);
                    } else if (parameter.isAnnotationPresent(Query.class)) {
                        final var name   = Objects.requireNonNull(parameter.getAnnotation(Query.class)).value();
                        final var values = request.query().computeIfAbsent(name, _ -> new ArrayList<>());
                        value = parameter.getType().isArray()
                                ? values.toArray()
                                : !values.isEmpty()
                                  ? values.getFirst()
                                  : null;
                    } else {
                        value = null;
                    }

                    if (value != null) {
                        args[i] = this.convert(value, value.getClass(), parameter.getType());
                    }
                }

                final HTTPResult<?> result;
                try {
                    final var object = bundle.callee().invoke(bundle.instance(), args);
                    final var type   = bundle.callee().getGenericReturnType();
                    result = convert(object, type, new TypeRef<HTTPResult<?>>() {
                    }.getType());
                } catch (final Exception e) {
                    Log.trace(e);
                    new HTTPResponseMessage("HTTP/1.1",
                                            500,
                                            "Internal Server Error - %s".formatted(e.getMessage()),
                                            new HashMap<>(),
                                            null,
                                            false).write(outputStream);
                    return;
                }

                final Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", bundle.result());
                headers.put("Connection", "Close");

                final var isChunked = result.getSize() < 0;
                if (isChunked)
                    headers.put("Transfer-Encoding", "chunked");
                else
                    headers.put("Content-Length", Integer.toString(result.getSize()));

                new HTTPResponseMessage("HTTP/1.1",
                                        result.getStatusCode(),
                                        result.getStatusText(),
                                        headers,
                                        result.getStream(),
                                        isChunked).write(outputStream);
                return;
            }

            notFound().write(outputStream);
        }
    }

    private static HTTPResponseMessage notFound() {
        final var bytes = "resource not found".getBytes();

        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/plain");
        headers.put("Content-Length", Integer.toString(bytes.length));
        headers.put("Connection", "Close");

        final InputStream body = new ByteArrayInputStream(bytes);

        return new HTTPResponseMessage("HTTP/1.1",
                                       404,
                                       "Not Found",
                                       headers,
                                       body,
                                       false);
    }

    @Override
    public void close() throws IOException {
        serverSocket.close();
    }
}
