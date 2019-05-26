package com.dragonfly.entity;

public class SensorLog extends Log {
    private float temperature;
    private float humidity;
    private boolean wasJustWatered;
    private int sector;

    public SensorLog() {}

    public SensorLog(int unitID, float temperature, float humidity, boolean wasJustWatered, int sector) {
        super.timestamp = System.currentTimeMillis();
        super.unitID = unitID;
        this.temperature = temperature;
        this.humidity = humidity;
        this.wasJustWatered = wasJustWatered;
        this.sector = sector;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public boolean isWasJustWatered() {
        return wasJustWatered;
    }

    public int getSector() {
        return sector;
    }
}
