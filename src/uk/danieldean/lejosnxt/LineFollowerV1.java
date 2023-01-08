package uk.danieldean.lejosnxt;

import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.ColorSensor.Color;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

public class LineFollowerV1 {

    public static void main(String[] args) {

        LCD.drawString("Press any button\n"
                + "to begin.", 0, 0);
        Button.waitForAnyPress();
        LCD.clear();

        LCD.drawString("Press enter to\n"
                + "stop.", 0, 0);

        ColorSensor cs = new ColorSensor(SensorPort.S1);
        int colour;

        while(Button.ENTER.isUp()) {

            colour = cs.getColor().getColor();

            if(colour == Color.BLACK || colour == Color.BLUE || colour == Color.GREEN
                    || colour == Color.RED || colour == Color.YELLOW) {
                // Drive forward.
                Motor.B.forward();
                Motor.C.forward();
            } else {
                // Turn left (or right)?
                Motor.B.stop(true);
                //Motor.C.stop(true);
            }

        }

    }

}
