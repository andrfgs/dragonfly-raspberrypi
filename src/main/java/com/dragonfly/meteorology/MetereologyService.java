package com.dragonfly.meteorology;

import com.dragonfly.util.HTTPResponse;
import com.dragonfly.util.HTTPUtils;
import com.dragonfly.meteorology.entity.Forecast;
import com.google.gson.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides meteorology forecasts and current meteorology from OpenWeatherMap, an
 * open source meteorology API
 */
public class MetereologyService {

    private static final String API_KEY = "8a7fda216d28ca091391a1a2218ce4d9";
    private static final String CITY_ID = "3170647"; // For now, we hardcode Pisa, eventually we can allow the user to choose his location
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().registerTypeAdapter(Forecast.class, new ForecastAdapter()).create();

    public static List<Forecast> getForecasts() throws IOException {
        HTTPResponse r = HTTPUtils.get("http://api.openweathermap.org/data/2.5/forecast?id=" + CITY_ID + "&units=metric&appid=" + API_KEY);
        if (r.getHttpCode() != 200)
            return null;

        JsonParser parser = new JsonParser();
        JsonElement e1 = parser.parse(r.getContent());
        List<Forecast> l = new LinkedList<>();
        for (JsonElement forecast : e1.getAsJsonObject().getAsJsonArray("list"))
            l.add(gson.fromJson(forecast, Forecast.class));

        return l;
    }

    public static Forecast getCurrentWeather() throws IOException {
        HTTPResponse r = HTTPUtils.get("http://api.openweathermap.org/data/2.5/weather?id=" + CITY_ID + "&units=metric&appid=" + API_KEY);
        if (r.getHttpCode() != 200)
            return null;

        JsonParser parser = new JsonParser();
        return gson.fromJson(parser.parse(r.getContent()), Forecast.class);
    }

    /**
     * Returns whether its going to rain in the next 24h and also how intense it will be.
     * @return
     */
    public static ForecastResult isItGoingToRainInNext24h() throws IOException {
        // Each forecast is a forecast every 3 hours for the next 5 days,
        // as such, select only the first 8 forecasts
        List<Forecast> forecasts = getForecasts();
        forecasts.sort((Comparator.comparingLong(Forecast::getTimestamp)));

        int ctr = 0;
        int noRainCtr = 0;
        int drizzleCtr = 0;
        int rainCtr = 0;
        int stormCtr = 0;
        for (Forecast f : forecasts)
        {
            if (ctr > 8) break;
            else ctr++;

            switch (f.getType())
            {
                case CLEAR:
                case CLOUDY:
                    noRainCtr++;
                    break;
                case DRIZZLE:
                    drizzleCtr++;
                    break;
                case RAIN:
                    rainCtr++;
                    break;
                case STORM:
                case SNOW:
                    stormCtr++;
                    break;
            }
        }

        if (stormCtr > 0) return ForecastResult.STORM_PREDICTED;
        if (rainCtr > 0) return ForecastResult.RAIN_PREDICTED;
        if (drizzleCtr > 0) return ForecastResult.DRIZZLE_PREDICTED;

        return ForecastResult.NO_RAIN_PREDICTED;
    }
}
