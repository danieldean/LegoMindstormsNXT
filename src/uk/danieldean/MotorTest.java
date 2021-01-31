package uk.danieldean;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;

public class MotorTest {

    public static void main(String[] args) {

        LCD.drawString("Press any button\n"
                + "to begin motor \n"
                + "test.", 0, 0);
        Button.waitForAnyPress();
        LCD.clear();

        LCD.drawString("In progress...", 0, 0);
        Motor.A.rotate(360);
        Motor.B.rotate(360);
        Motor.C.rotate(360);
        LCD.clear();

        LCD.drawString("Motor test \n"
                + "complete. Press \n"
                + "any button to \n"
                + "exit.", 0, 0);
        Button.waitForAnyPress();

    }

}
