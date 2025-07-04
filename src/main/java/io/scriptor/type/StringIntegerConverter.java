package io.scriptor.type;

public class StringIntegerConverter implements IConverter<String, Integer> {

    @Override
    public Integer from(final String source) {
        return Integer.parseInt(source);
    }
}
