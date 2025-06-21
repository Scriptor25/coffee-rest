package io.scriptor.result;

public class VoidResult extends ResultBase<Void> {

    public VoidResult(final int statusCode) {
        super(statusCode);
    }

    public VoidResult(final int statusCode, final String statusText) {
        super(statusCode, statusText);
    }

    @Override
    public byte[] getBytes() {
        return new byte[0];
    }
}
