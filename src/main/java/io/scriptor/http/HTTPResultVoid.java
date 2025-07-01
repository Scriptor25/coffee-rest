package io.scriptor.http;

import java.io.InputStream;

public class HTTPResultVoid extends HTTPResultBase<Void> {

    public HTTPResultVoid(final int statusCode) {
        super(statusCode);
    }

    public HTTPResultVoid(final int statusCode, final String statusText) {
        super(statusCode, statusText);
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public InputStream getStream() {
        return null;
    }
}
