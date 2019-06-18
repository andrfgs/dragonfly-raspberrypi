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
        this.stepCounter = 0;
        this.p1 = gpio.provisionDigitalOutputPin(p1);
        this.p2 = gpio.provisionDigitalOutputPin(p2);
        this.p3 = gpio.provisionDigitalOutputPin(p3);
        this.p4 = gpio.provisionDigitalOutputPin(p4);
    }

    public void rotate(int deg)
    {
        int x = (int)(((double)STEPS_PER_REV * Math.abs(deg)) / 360.0) - stepCounter;

        if (deg < 0)
            for (int i = 0; i < x; i++)
                clockwise();
        else
            for (int i = 0; i < x; i++)
                counterClockwise();
    }

    public void reset()
    {
        // When rotating back the motor to its initial position we have two options:
        // -we either rotate the inverse amount of steps we are currently at
        // -or we keep rotating in the same direction till we go back to the initial pos
        // We wish the option that minimizes the amount of steps to reset the motor.
        // Let a denote the first option and b denote the second
        int a = -stepCounter;
        int b = stepCounter >= 0 ? STEPS_PER_REV - stepCounter : -STEPS_PER_REV + stepCounter;

        // We now wish the closest value to zero, as that means the minimum steps
        int minStepsToReset = Math.abs(a) < Math.abs(b) ? a : b;

        // If number of steps is negative, rotate clockwise, if positive, rotate counterclockwise
        boolean clockwise = minStepsToReset < 0;
        while (stepCounter != 0) {
            if (clockwise)
                clockwise();
            else
                counterClockwise();
            delay(2);
        }
    }

    public void turnOffPins() {
        p1.low();
        p2.low();
        p3.low();
        p4.low();
    }

    public void counterClockwise()
    {
        stepCounter = (stepCounter + 1) % STEPS_PER_REV;

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
        stepCounter = (stepCounter - 1) % STEPS_PER_REV;

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
