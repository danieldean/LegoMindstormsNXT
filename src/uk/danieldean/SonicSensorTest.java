package uk.danieldean;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class SonicSensorTest {

    public static void main(String[] args) {

        UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
        int distance;

        while(Button.ENTER.isUp()) {
            distance = us.getDistance();
            LCD.clear();
            LCD.drawString("Distance: " + distance + "cm", 0, 0);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // Do nothing.
            }
        }

    }

}
