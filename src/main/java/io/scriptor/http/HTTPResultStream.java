package io.scriptor.http;

import java.io.InputStream;

public class HTTPResultStream extends HTTPResultBase<InputStream> {

    public HTTPResultStream(final int statusCode) {
        super(statusCode);
    }

    public HTTPResultStream(final int statusCode, final String statusText) {
        super(statusCode, statusText);
    }

    public HTTPResultStream(final int statusCode, final String statusText, final InputStream value) {
        super(statusCode, statusText, value);
    }

    @Override
    public int getSize() {
        return -1;
    }

    @Override
    public InputStream getStream() {
        return getValue();
    }
}
