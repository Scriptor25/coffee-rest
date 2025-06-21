package io.scriptor.http;

import io.scriptor.annotation.Endpoint;
import io.scriptor.annotation.Parameter;
import io.scriptor.annotation.Resource;
import io.scriptor.log.Log;
import io.scriptor.result.ResultBase;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.*;

public class HTTPServer implements AutoCloseable {

    private record RouteBundle(
            Object instance,
            Method callee,
            Route route,
            HTTPMethod method,
            String accept,
            String result
    ) {
    }

    private final int port;
    private final boolean enableTLS;
    private final String keystoreFilename;
    private final String keystorePassphrase;

    private final ServerSocket serverSocket;

    private final List<RouteBundle> routes = new ArrayList<>();

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

        this.port = port;
        this.enableTLS = enableTLS;
        this.keystoreFilename = keystoreFilename;
        this.keystorePassphrase = keystorePassphrase;

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
        routes.add(new RouteBundle(instance,
                                   callee,
                                   new Route(endpoint.value(), resource.value()),
                                   resource.method(),
                                   resource.accept(),
                                   resource.result()));
    }

    public void handleRequest() throws IOException {
        final var socket = serverSocket.accept();
        new Thread(() -> {
            try {
                if (enableTLS)
                    ((SSLSocket) socket).startHandshake();
                handleRequest(socket);
            } catch (final IOException | InvocationTargetException | IllegalAccessException e) {
                Log.trace(e);
            }
        }).start();
    }

    private void handleRequest(final Socket socket)
            throws IOException, InvocationTargetException, IllegalAccessException {
        try (
                final var inputStream = socket.getInputStream();
                final var outputStream = socket.getOutputStream()
        ) {
            final var request = HTTPRequestMessage.read(inputStream);

            Log.info("%s %s %s", request.method(), request.path(), request.protocol());

            for (final var bundle : routes) {
                if (bundle.method() != request.method())
                    continue;
                if (!bundle.route().matches(request.path()))
                    continue;

                final var parameterCount = bundle.callee().getParameterCount();
                final var parameters     = bundle.callee().getParameters();

                final var args = new Object[parameterCount];

                for (int i = 0; i < parameterCount; ++i) {
                    final var parameter = parameters[i].isAnnotationPresent(Parameter.class)
                                          ? Objects.requireNonNull(parameters[i].getAnnotation(Parameter.class)).value()
                                          : parameters[i].getName();
                    args[i] = switch (parameter) {
                        case "headers" -> request.headers();
                        case "body" -> request.body();
                        default -> bundle.route().get(request.path(), parameter);
                    };
                }

                final var result = (ResultBase<?>) bundle.callee().invoke(bundle.instance(), args);

                final var bytes = result.getBytes();

                final Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", bundle.result());
                headers.put("Content-Length", Integer.toString(bytes.length));
                headers.put("Connection", "Close");

                final var body = new ByteArrayInputStream(bytes);

                new HTTPResponseMessage("HTTP/1.1",
                                        result.getStatusCode(),
                                        result.getStatusText(),
                                        headers,
                                        body).write(outputStream);
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

        return new HTTPResponseMessage("HTTP/1.1", 404, "Not Found", headers, body);
    }

    @Override
    public void close() throws IOException {
        serverSocket.close();
    }
}
