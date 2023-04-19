package com.example.client;

import com.google.gson.*;
import javafx.collections.ObservableList;

import java.lang.reflect.Type;

public class ObservableListAdapter implements JsonSerializer<ObservableList<?>>, JsonDeserializer<ObservableList<?>> {

    @Override
    public ObservableList<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return null;
    }

    @Override
    public JsonElement serialize(ObservableList<?> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray jsonArray = new JsonArray();
        src.forEach(element -> jsonArray.add(context.serialize(element)));
        return jsonArray;
    }
}
