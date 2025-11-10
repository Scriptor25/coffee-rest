package io.scriptor;

import io.scriptor.annotation.Endpoint;
import io.scriptor.annotation.Resource;
import io.scriptor.http.HTTPServer;
import io.scriptor.loader.Loader;
import io.scriptor.log.Log;
import io.scriptor.type.IConverter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.StreamSupport;

public class Main {

    @Contract("_, !null -> !null")
    public static String getenv(final @NotNull String key, final @Nullable String value) {
        final var entry = System.getenv(key);
        return entry == null ? value : entry;
    }

    public static void main(final @NotNull String @NotNull [] args)
            throws IOException,
                   KeyStoreException,
                   CertificateException,
                   NoSuchAlgorithmException,
                   UnrecoverableKeyException,
                   KeyManagementException {

        final var enableTLS          = Integer.parseInt(getenv("ENABLE_TLS", "0")) != 0;
        final var port               = Integer.parseInt(getenv("PORT", enableTLS ? "443" : "80"));
        final var keystoreFilename   = getenv("KEYSTORE", null);
        final var keystorePassphrase = getenv("KEYSTORE_PASSPHRASE", null);

        try (final var server = new HTTPServer(port, enableTLS, keystoreFilename, keystorePassphrase)) {

            final var loader = new Loader("");

            StreamSupport
                    .stream(loader.spliterator(), false)
                    .filter(clazz -> clazz.isAnnotationPresent(Endpoint.class))
                    .forEach(clazz -> {
                        if (Arrays.stream(clazz.getConstructors())
                                  .noneMatch(constructor -> constructor.getParameterCount() == 0)) {
                            Log.severe("endpoint class '%s' does not have a default constructor", clazz);
                            return;
                        }

                        final Object instance;
                        try {
                            instance = clazz.getConstructor().newInstance();
                        } catch (final Exception e) {
                            Log.trace(e);
                            return;
                        }

                        final var endpoint = Objects.requireNonNull(clazz.getAnnotation(Endpoint.class));

                        Arrays.stream(clazz.getMethods())
                              .filter(method -> method.isAnnotationPresent(Resource.class))
                              .forEach(method -> {
                                  final var resource = Objects.requireNonNull(method.getAnnotation(Resource.class));

                                  Log.info("route %s%s %s %s %s",
                                           endpoint.value(),
                                           resource.path(),
                                           resource.method(),
                                           resource.accept(),
                                           resource.result());
                                  server.registerRoute(instance, method, endpoint, resource);
                              });
                    });

            StreamSupport
                    .stream(loader.spliterator(), false)
                    .filter(clazz -> Arrays.asList(clazz.getInterfaces()).contains(IConverter.class))
                    .forEach(clazz -> {
                        if (Arrays.stream(clazz.getConstructors())
                                  .noneMatch(constructor -> constructor.getParameterCount() == 0)) {
                            Log.severe("converter class '%s' does not have a default constructor", clazz);
                            return;
                        }

                        final IConverter<?, ?> instance;
                        try {
                            instance = (IConverter<?, ?>) clazz.getConstructor().newInstance();
                        } catch (final Exception e) {
                            Log.trace(e);
                            return;
                        }

                        final var interfaceType = (ParameterizedType) clazz.getGenericInterfaces()[
                                Arrays.stream(clazz.getInterfaces())
                                      .toList()
                                      .indexOf(IConverter.class)
                                ];
                        final var source      = interfaceType.getActualTypeArguments()[0];
                        final var destination = interfaceType.getActualTypeArguments()[1];
                        Log.info("converter [ %s -> %s ]", source, destination);
                        server.registerConverter(source, destination, instance);
                    });

            server.start();
        }
    }

    private Main() {
    }
}
