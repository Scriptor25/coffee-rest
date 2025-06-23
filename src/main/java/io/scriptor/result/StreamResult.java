package io.scriptor.result;

import java.io.InputStream;

public class StreamResult extends ResultBase<InputStream> {

    public StreamResult(final int statusCode) {
        super(statusCode);
    }

    public StreamResult(final int statusCode, final String statusText) {
        super(statusCode, statusText);
    }

    public StreamResult(final int statusCode, final String statusText, final InputStream value) {
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
