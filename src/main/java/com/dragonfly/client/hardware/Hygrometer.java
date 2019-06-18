package com.dragonfly.client.hardware;

import com.dragonfly.util.SerialInterface;

import java.io.IOException;

public class Hygrometer {

    // Read from sensor 5 times and then average the values
    private static final int SENSOR_READS = 5;
    // Wait 100ms per each sensor read
    private static final int SENSOR_DELAY_PER_READ = 100;

    private int sector;

    public Hygrometer(int sector)
    {
        this.sector = sector;
    }

    public float readHumidity() throws IOException {
        return readAverageFromSensor();
    }

    private float readAverageFromSensor() throws IOException {
        float humidity = 0.0f;

        for (int i = 0; i < SENSOR_READS; i++) {
            humidity += readFromSensor();
            try {
                Thread.sleep(SENSOR_DELAY_PER_READ);
            } catch (InterruptedException e) {}
        }

        return humidity / SENSOR_READS;
    }

    private float readFromSensor() throws IOException {
        return Float.parseFloat(SerialInterface.send("2" + sector, 1));
    }
}
