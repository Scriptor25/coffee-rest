package io.scriptor.http;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class HTTPResultString extends HTTPResult<String> {

    public HTTPResultString(final int statusCode) {
        super(statusCode);
    }

    public HTTPResultString(final int statusCode, final String statusText) {
        super(statusCode, statusText);
    }

    public HTTPResultString(final int statusCode, final String statusText, final String value) {
        super(statusCode, statusText, value);
    }

    @Override
    public int getSize() {
        return getValue().getBytes().length;
    }

    @Override
    public InputStream getStream() {
        return new ByteArrayInputStream(getValue().getBytes());
    }
}
