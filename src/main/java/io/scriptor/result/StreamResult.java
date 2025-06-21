package io.scriptor.result;

import java.io.IOException;
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
    public byte[] getBytes() throws IOException {
        return getValue().readAllBytes();
    }
}
