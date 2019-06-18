
import com.dragonfly.client.PlantationController;
import com.dragonfly.client.hardware.Pump;
import com.dragonfly.client.hardware.Rotor;
import com.dragonfly.util.SerialInterface;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final GpioController gpio = GpioFactory.getInstance();
    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void main(String[] args) throws InterruptedException, IOException {
        LOG.setLevel(Level.INFO);

        SerialInterface.initialize();
        Gpio.wiringPiSetupGpio();
        LOG.info("Initialized wiring pi.");

        Rotor r = new Rotor(RaspiPin.GPIO_04, RaspiPin.GPIO_17, RaspiPin.GPIO_27, RaspiPin.GPIO_22);
        Pump p = new Pump();
        PlantationController pc = new PlantationController(r, p, true);

        while (true)
        {
            LOG.info("Executing update.");
            pc.update();
            Thread.sleep(1000 * 60 * 6); // Update every 6 minutes
        }

    }

    private static void shutdown()
    {
        try
        {
            SerialInterface.closeStreams();
            for (Thread t : Thread.getAllStackTraces().keySet())
            {
                if (t.getName().equals("main"))
                    continue;

                if (t.getState()==Thread.State.RUNNABLE)
                t.interrupt();
            }
            gpio.shutdown();
        }
        catch (Exception e)
        {}
    }


}
