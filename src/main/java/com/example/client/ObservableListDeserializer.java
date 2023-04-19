package com.example.client;

import com.google.gson.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.lang.reflect.Type;

public class ObservableListDeserializer implements JsonDeserializer<ObservableList<String>> {
    @Override
    public ObservableList<String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray jsonArray = json.getAsJsonArray();
        ObservableList<String> list = FXCollections.observableArrayList();
        for (JsonElement element : jsonArray) {
            String value = element.getAsString();
            list.add(value);
        }
        return list;
    }
}
