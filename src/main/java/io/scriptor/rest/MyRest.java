package io.scriptor.rest;

import io.scriptor.annotation.Endpoint;
import io.scriptor.annotation.Resource;
import io.scriptor.http.HTTPMethod;
import io.scriptor.log.Log;
import io.scriptor.result.ResultBase;
import io.scriptor.result.StreamResult;
import io.scriptor.result.StringResult;
import io.scriptor.result.VoidResult;

import java.io.InputStream;
import java.util.Objects;

@Endpoint("/")
public class MyRest {

    @Resource(value = "hello", result = "text/plain")
    public ResultBase<String> getHello() {
        return new StringResult(200, "OK", "Hello World!");
    }

    @Resource(value = "favicon.[ext]", result = "image/svg+xml")
    public ResultBase<InputStream> getFavicon(final String ext) {
        final var stream = Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("favicon.svg"));
        return new StreamResult(200, "OK", stream);
    }

    @Resource(value = "message/[from]/[to]", method = HTTPMethod.POST, accept = "text/plain")
    public ResultBase<Void> postMessage(final String from, final String to, final String body) {
        Log.info("message (from %s to %s): %s", from, to, body);
        return new VoidResult(200, "OK");
    }
}
