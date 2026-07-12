package io.scriptor.rest;

import io.scriptor.annotation.Endpoint;
import io.scriptor.annotation.Resource;

import java.io.InputStream;

@Endpoint("/")
public class RootRest {

    @Resource(path = "favicon.[]", result = "image/svg+xml")
    public InputStream getFavicon() {
        return ClassLoader.getSystemResourceAsStream("favicon.svg");
    }
}
