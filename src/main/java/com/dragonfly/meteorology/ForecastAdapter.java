package com.dragonfly.meteorology;

import com.dragonfly.meteorology.entity.Forecast;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ForecastAdapter implements JsonDeserializer<Forecast> {

    @Override
    public Forecast deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject root = jsonElement.getAsJsonObject();
        JsonObject main = root.getAsJsonObject("main");

        JsonObject weatherRoot = root.get("weather").getAsJsonArray().get(0).getAsJsonObject();
        WeatherType wtype = WeatherType.convertFromWeatherId(weatherRoot.get("id").getAsInt());
        float temp = main.get("temp").getAsFloat();
        int humidity = main.get("humidity").getAsInt();
        float windSpeed = root.get("wind").getAsJsonObject().get("speed").getAsFloat();
        long timestamp = root.get("dt").getAsLong();

        return new Forecast(wtype, humidity, temp, windSpeed, timestamp);
    }
}
