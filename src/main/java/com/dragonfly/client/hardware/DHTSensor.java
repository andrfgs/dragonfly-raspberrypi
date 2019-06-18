package com.dragonfly.client.hardware;

import com.dragonfly.util.SerialInterface;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.serial.Serial;

import java.io.IOException;

public class DHTSensor {
    private static final int SENSOR_READ_INTERVAL = 4; // Read sensor every 4 seconds
    private static final double CORRECTED_VALUE_WEIGHT = 0.3;
    private static final GpioController gpio = GpioFactory.getInstance();

    private float temperature;
    private float temperatureDeviation;
    private float humidity;
    private float humidityDeviation;
    private long lastRead;

    public DHTSensor() throws IOException {
        temperatureDeviation = 0;
        humidityDeviation = 0;
        readDHTFromSerial();
        lastRead = System.currentTimeMillis();
    }

    /**
     * Calibrate sensor by recording the discrepancy between the sensor
     * readings and an external (quite reliable) source, such as a
     * metereologic forecast. The given value is supposed to be an
     * average of all the area, so a certain weight will be attributed.
     * Still, the sensor's readings are more taken into consideration than
     * the given value.
     * @param temperature
     * @param humidity
     */
    public void calibrate(float temperature, float humidity)
    {
        temperatureDeviation = this.temperature - temperature;
        humidityDeviation = this.humidity - humidity;
    }

    public float getTemperature() throws IOException {
        if (System.currentTimeMillis() - lastRead >= SENSOR_READ_INTERVAL * 1000)
            readDHTFromSerial();

        float correctedValue = temperature - temperatureDeviation;
        return (int)Math.round(correctedValue * CORRECTED_VALUE_WEIGHT + temperature * (1-CORRECTED_VALUE_WEIGHT));
    }

    public float getHumidity() throws IOException {
        if (System.currentTimeMillis() - lastRead >= SENSOR_READ_INTERVAL * 1000)
            readDHTFromSerial();

        float correctedValue = humidity - humidityDeviation;
        return (int)Math.round(correctedValue * CORRECTED_VALUE_WEIGHT + humidity * (1-CORRECTED_VALUE_WEIGHT));
    }

    private void readDHTFromSerial() throws IOException {
        String resp = SerialInterface.send("1", 2);

        String[] data = resp.split("\\n");
        temperature = Float.parseFloat(data[0]);
        humidity = Float.parseFloat(data[1]);
    }
}
