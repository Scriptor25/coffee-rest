package io.scriptor.type;

import io.scriptor.http.result.HTTPResult;
import io.scriptor.http.result.HTTPResultString;
import io.scriptor.http.result.HTTPResultVoid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class JsonObjectResultConverter implements IConverter<JSONObject, HTTPResult<?>> {

    @Override
    public @NotNull HTTPResult<?> from(final @Nullable JSONObject source) {
        if (source == null) {
            return new HTTPResultVoid(200, "OK");
        }
        return new HTTPResultString(200, "OK", source.toString());
    }
}
