package io.scriptor.result;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StringResult extends ResultBase<String> {

    public StringResult(final int statusCode) {
        super(statusCode);
    }

    public StringResult(final int statusCode, final String statusText) {
        super(statusCode, statusText);
    }

    public StringResult(final int statusCode, final String statusText, final String value) {
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
