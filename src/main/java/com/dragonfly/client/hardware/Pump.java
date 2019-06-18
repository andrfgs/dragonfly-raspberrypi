package com.dragonfly.client.hardware;

import com.dragonfly.util.SerialInterface;

import java.io.IOException;

public class Pump {


    public void turnOn() throws IOException {
        SerialInterface.send("3", 1);
    }

    public void turnOff() throws IOException {
        SerialInterface.send("4", 1);
    }
}
