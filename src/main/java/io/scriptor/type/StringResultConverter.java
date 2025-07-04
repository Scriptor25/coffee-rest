package io.scriptor.type;

import io.scriptor.http.HTTPResult;
import io.scriptor.http.HTTPResultString;

public class StringResultConverter implements IConverter<String, HTTPResult<?>> {

    @Override
    public HTTPResult<?> from(final String source) {
        return new HTTPResultString(200, "OK", source);
    }
}
