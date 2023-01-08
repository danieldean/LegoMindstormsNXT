package uk.danieldean.lejosnxt;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class TouchSensorTest {

    public static void main(String[] args) {

        TouchSensor ts1 = new TouchSensor(SensorPort.S1);
        TouchSensor ts2 = new TouchSensor(SensorPort.S2);

        boolean tsOneIsPressed;
        boolean tsTwoIsPressed;

        while(Button.ENTER.isUp()) {

            tsOneIsPressed = ts1.isPressed();
            tsTwoIsPressed = ts2.isPressed();

            LCD.clear();
            LCD.drawString("TS1 Pressed: " + toYesNo(tsOneIsPressed) + "\n"
                    + "TS2 Pressed: " + toYesNo(tsTwoIsPressed) + "\n", 0, 0);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // Do nothing.
            }

        }

    }

    public static String toYesNo(boolean value) {
        if(value) {
            return "Yes";
        } else {
            return "No";
        }
    }

}
