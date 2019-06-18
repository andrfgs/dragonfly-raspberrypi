package com.dragonfly.meteorology.entity;

import com.dragonfly.meteorology.WeatherType;

public class Forecast {

    private WeatherType type;
    private int humidity;
    private float temperature;
    private float windSpeed;
    private long timestamp;

    public Forecast(WeatherType type, int humidity, float temperature, float windSpeed, long timestamp) {
        this.type = type;
        this.humidity = humidity;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.timestamp = timestamp;
    }

    public WeatherType getType() {
        return type;
    }

    public int getHumidity() {
        return humidity;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public long getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "Forecast{" +
                "type=" + type +
                ", humidity=" + humidity +
                ", temperature=" + temperature +
                ", windSpeed=" + windSpeed +
                ", timestamp=" + timestamp +
                '}';
    }
}
