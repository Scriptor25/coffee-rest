package io.scriptor.type;

import io.scriptor.http.HTTPResult;
import io.scriptor.http.HTTPResultString;
import org.json.JSONObject;

public class JSONObjectResultConverter implements IConverter<JSONObject, HTTPResult<?>> {

    @Override
    public HTTPResult<?> from(final JSONObject source) {
        return new HTTPResultString(200, "OK", source.toString());
    }
}
