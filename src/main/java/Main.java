import com.dragonfly.client.hardware.Rotor;
import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.Gpio;

public class Main {


    public static void main(String[] args) throws InterruptedException {
        Gpio.wiringPiSetupGpio();

        Rotor r = new Rotor(RaspiPin.GPIO_14, RaspiPin.GPIO_15, RaspiPin.GPIO_18, RaspiPin.GPIO_23);

        while (true) {
            r.counterClockwise();
            Thread.sleep(1);
        }
    }
}
