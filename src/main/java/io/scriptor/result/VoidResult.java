package io.scriptor.result;

import java.io.InputStream;

public class VoidResult extends ResultBase<Void> {

    public VoidResult(final int statusCode) {
        super(statusCode);
    }

    public VoidResult(final int statusCode, final String statusText) {
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
