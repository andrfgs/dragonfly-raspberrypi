package com.dragonfly.util;

import com.google.common.base.Charsets;
import com.pi4j.io.serial.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SerialInterface {

    private static boolean isInitialized = false;
    private static final Serial serial = SerialFactory.createInstance();

    public static void initialize() throws IOException {
        if (isInitialized) return;

        SerialConfig config = new SerialConfig();

        // set default serial settings (device, baud rate, flow control, etc)
        //
        // by default, use the DEFAULT com port on the Raspberry Pi (exposed on GPIO header)
        // NOTE: this utility method will determine the default serial port for the
        //       detected platform and board/model.  For all Raspberry Pi models
        //       except the 3B, it will return "/dev/ttyAMA0".  For Raspberry Pi
        //       model 3B may return "/dev/ttyS0" or "/dev/ttyAMA0" depending on
        //       environment configuration.
        config.device("/dev/ttyS0")
                .baud(Baud._9600)
                .dataBits(DataBits._8)
                .parity(Parity.NONE)
                .stopBits(StopBits._1)
                .flowControl(FlowControl.NONE);

        serial.open(config);

        /*serial.addListener(new SerialDataEventListener() {
            @Override
            public void dataReceived(SerialDataEvent event) {

                // NOTE! - It is extremely important to read the data received from the
                // serial port.  If it does not get read from the receive buffer, the
                // buffer will continue to grow and consume memory.
                try {
                    byte[] data = event.getBytes();
                    int ctr = 0;
                    StringBuilder str = new StringBuilder();
                    while (data[ctr] != 10)
                        str.append(Character.toString((char)data[ctr++]));

                    System.out.println(str.toString());
                    lastResponse = str.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/

        isInitialized = true;
    }

    public static String send(String sendData, int lines) throws IOException {
        serial.write(sendData);

        InputStream is = serial.getInputStream();
        byte[] data = getAllBytes(is);
        int ctr = 0;
        int linectr = 0;

        StringBuilder str = new StringBuilder();
        // For each line, read until line finishes
        while (linectr < lines) {
            if (data[ctr] == 10) linectr++;

            if (linectr < lines)
                str.append(Character.toString((char) data[ctr++]));
        }

        return str.toString();
    }

    public static void closeStreams() throws IOException {
        serial.getInputStream().close();
        serial.getOutputStream().close();
    }

    private static byte[] getAllBytes(InputStream is) throws IOException {
        byte[] bufer = new byte[1024];
        is.read(bufer);
        return bufer;

    }

}
