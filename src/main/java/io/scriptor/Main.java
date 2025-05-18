package io.scriptor;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.security.*;
import java.security.cert.CertificateException;

public class Main {

    public static void main(final String[] args)
            throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException,
                   UnrecoverableKeyException, KeyManagementException {

        final var passphrase = "12345678".toCharArray();
        final var keyStore   = KeyStore.getInstance("JKS");
        keyStore.load(ClassLoader.getSystemResourceAsStream("keystore.jks"), passphrase);

        final var keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, passphrase);

        final var sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

        final var sslServerSocketFactory = sslContext.getServerSocketFactory();

        final var port = 443;

        try (final var sslServerSocket = sslServerSocketFactory.createServerSocket(port)) {
            System.out.printf("HTTPS server listening on port %d%n", port);

            while (true) {
                final var socket = (SSLSocket) sslServerSocket.accept();
                new Thread(() -> {
                    try {
                        socket.startHandshake();
                        handleRequest(socket);
                    } catch (final SocketException e) {
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }

    private static void handleRequest(final SSLSocket socket) throws IOException {
        try (
                final var inputStream = socket.getInputStream();
                final var outputStream = socket.getOutputStream()
        ) {
            final var message = HTTPMessage.parse(inputStream);

            switch (message.method()) {
                case "GET" -> {
                    switch (message.path()) {
                        case "/hello" -> sendResponse(outputStream, 200, "text/plain", "Hello World!");
                        case "/favicon.ico" -> {
                            try (final var favicon = ClassLoader.getSystemResourceAsStream("favicon.png")) {
                                assert favicon != null;
                                sendResponse(outputStream, 200, "image/png", favicon);
                            }
                        }
                    }
                }
                default -> sendResponse(outputStream, 404, "text/plain", "Not Found");
            }
        }
    }

    private static void sendResponse(
            final OutputStream stream,
            final int status,
            final String contentType,
            final String body
    ) throws IOException {
        final var bodyBytes = body.getBytes();
        sendResponse(stream, status, contentType, bodyBytes);
    }

    private static void sendResponse(
            final OutputStream stream,
            final int status,
            final String contentType,
            final InputStream body
    ) throws IOException {
        final var buffer = new ByteArrayOutputStream();
        final var data   = new byte[4096];
        int       bytesRead;

        while ((bytesRead = body.read(data)) != -1)
            buffer.write(data, 0, bytesRead);
        buffer.flush();

        final var bodyBytes = buffer.toByteArray();
        sendResponse(stream, status, contentType, bodyBytes);
    }

    private static void sendResponse(
            final OutputStream stream,
            final int status,
            final String contentType,
            final byte[] body
    ) throws IOException {
        stream.write("HTTP/1.1 %d OK%n".formatted(status).getBytes());
        stream.write("Content-Type: %s%n".formatted(contentType).getBytes());
        stream.write("Content-Length: %s%n".formatted(body.length).getBytes());
        stream.write("Connection: close%n".formatted().getBytes());
        stream.write("%n".formatted().getBytes());
        stream.write(body);
        stream.flush();
    }

    private Main() {
    }
}
