package io.scriptor.type;

import io.scriptor.http.HTTPResult;
import io.scriptor.http.HTTPResultStream;

import java.io.InputStream;

public class StreamResultConverter implements IConverter<InputStream, HTTPResult<?>> {

    @Override
    public HTTPResult<?> from(final InputStream source) {
        return new HTTPResultStream(200, "OK", source);
    }
}
