package com.dragonfly.client.hardware;

import com.pi4j.io.gpio.*;

public class Rotor {

    private static final int STEPS_PER_REV = 512;
    private static final int MOTOR_SPEED = 1;
    private static final GpioController gpio = GpioFactory.getInstance();

    private GpioPinDigitalOutput p1;
    private GpioPinDigitalOutput p2;
    private GpioPinDigitalOutput p3;
    private GpioPinDigitalOutput p4;
    private int stepCounter;

    public Rotor(Pin p1, Pin p2, Pin p3, Pin p4) {
        this.p1 = gpio.provisionDigitalOutputPin(p1);
        this.p2 = gpio.provisionDigitalOutputPin(p2);
        this.p3 = gpio.provisionDigitalOutputPin(p3);
        this.p4 = gpio.provisionDigitalOutputPin(p4);
    }

    public void rotate(int deg)
    {
        int x = (int)(((double)STEPS_PER_REV * deg) / 360.0) - stepCounter;

        for (int i = 0; i < x; i++)
            clockwise();
    }

    private void reset()
    {
        while (stepCounter > 0)
            counterClockwise();
    }

    public void counterClockwise()
    {
        stepCounter--;

        p1.high();
        p2.low();
        p3.low();
        p4.low();
        delay(MOTOR_SPEED);

        p1.high();
        p2.high();
        p3.low();
        p4.low();
        delay(MOTOR_SPEED);

        p1.low();
        p2.high();
        p3.low();
        p4.low();
        delay(MOTOR_SPEED);

        p1.low();
        p2.high();
        p3.high();
        p4.low();
        delay(MOTOR_SPEED);

        p1.low();
        p2.low();
        p3.high();
        p4.low();
        delay(MOTOR_SPEED);

        p1.low();
        p2.low();
        p3.high();
        p4.high();
        delay(MOTOR_SPEED);

        p1.low();
        p2.low();
        p3.low();
        p4.high();
        delay(MOTOR_SPEED);

        p1.high();
        p2.low();
        p3.low();
        p4.high();
        delay(MOTOR_SPEED);
    }

    private void clockwise()
    {
        stepCounter++;

        p4.high();
        p3.low();
        p2.low();
        p1.low();
        delay(MOTOR_SPEED);

        p4.high();
        p3.high();
        p2.low();
        p1.low();
        delay(MOTOR_SPEED);

        p4.low();
        p3.high();
        p2.low();
        p1.low();
        delay(MOTOR_SPEED);

        p4.low();
        p3.high();
        p2.high();
        p1.low();
        delay(MOTOR_SPEED);

        p4.low();
        p3.low();
        p2.high();
        p1.low();
        delay(MOTOR_SPEED);

        p4.low();
        p3.low();
        p2.high();
        p1.high();
        delay(MOTOR_SPEED);

        p4.low();
        p3.low();
        p2.low();
        p1.high();
        delay(MOTOR_SPEED);

        p4.high();
        p3.low();
        p2.low();
        p1.high();
        delay(MOTOR_SPEED);
    }

    private void delay(int milis)
    {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
