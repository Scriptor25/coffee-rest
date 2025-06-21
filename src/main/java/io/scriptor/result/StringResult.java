package io.scriptor.result;

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
    public byte[] getBytes() {
        return getValue().getBytes();
    }
}
