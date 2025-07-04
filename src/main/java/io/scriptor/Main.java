package io.scriptor;

import io.github.cdimascio.dotenv.Dotenv;
import io.scriptor.annotation.Endpoint;
import io.scriptor.annotation.Resource;
import io.scriptor.http.HTTPServer;
import io.scriptor.loader.Loader;
import io.scriptor.log.Log;
import io.scriptor.type.IConverter;
import io.scriptor.type.TypeRef;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.WildcardType;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static io.scriptor.type.IConverter.isAssignable;
import static io.scriptor.type.IConverter.isInBounds;

public class Main {

    public static void main(final String[] args)
            throws IOException,
                   KeyStoreException,
                   CertificateException,
                   NoSuchAlgorithmException,
                   UnrecoverableKeyException,
                   KeyManagementException,
                   ClassNotFoundException {

        final var env = Dotenv.configure().filename(".env.local").load();

        final var enableTLS          = Integer.parseInt(env.get("ENABLE_TLS", "0")) != 0;
        final var port               = Integer.parseInt(env.get("PORT", enableTLS ? "443" : "80"));
        final var keystoreFilename   = env.get("KEYSTORE");
        final var keystorePassphrase = env.get("KEYSTORE_PASSPHRASE");

        try (final var server = new HTTPServer(port, enableTLS, keystoreFilename, keystorePassphrase)) {

            final var loader = new Loader("io.scriptor");
            loader.stream()
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

            loader.stream()
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
                      Log.info("converter [%s -> %s]", interfaceType, source, destination);
                      server.registerConverter(source, destination, instance);
                  });

            server.start();
        }
    }

    private Main() {
    }
}
