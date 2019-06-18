package com.dragonfly.meteorology;

public enum WeatherType {
    CLEAR, CLOUDY, DRIZZLE, RAIN, STORM, SNOW;

    public static WeatherType convertFromWeatherId(int weatherId) {
        if (weatherId == 800)
            return WeatherType.CLEAR;

        weatherId /= 100;

        switch (weatherId) {
            case 2:
                return WeatherType.STORM;
            case 3:
                return WeatherType.DRIZZLE;
            case 5:
                return WeatherType.RAIN;
            case 6:
                return WeatherType.SNOW;

            // Case 7 refers to atmospheric phenomena such as mist, smoke, tornado's, etc
            // As the system currently does not support any of these events, we simply
            // report them as clouy
            case 7:
                return WeatherType.CLOUDY;
            case 8:
                return WeatherType.CLOUDY;
            default:
                return null;
        }
    }
}
