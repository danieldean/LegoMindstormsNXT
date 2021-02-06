package uk.danieldean;

import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.ColorSensor.Color;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.SensorPort;

/* A more advanced line follower that should be able to
 * be adjusted to suit most tracks. This is similar to a
 * program I wrote to control a simulated robot using
 * RobotLab whilst doing a module at the Open University.
 */
public class LineFollowerV2 {

    /* A boolean is used for turn direction but
     * for clarity these constants are used to give
     * actual left and right values.
     */
    private final static boolean LEFT = true;
    private final static boolean RIGHT = false;
    /* Seek in number of loops which depends on the
     * execution speed so it is difficult to gauge.
     */
    private final static int MAX_SEEK = 80;
    /* Power set in percent. Straight power will
     * be used whenever the robot detect the line.
     * Turns will start at 10% and increase 1% at
     * a time each loop. Any value too high could
     * result in the robot overshooting the line.
     */
    private final static int STRAIGHT_POWER = 80;
    private final static int MAX_TURN_POWER = 40;

    private static int seekCount = 0;
    private static boolean isSeeking = false;
    private static boolean turnDirection = LEFT;
    private static int turnPower = 10;

    public static void main(String[] args) {

        LCD.drawString("Press any button\n"
                + "to begin.", 0, 0);
        Button.waitForAnyPress();
        LCD.clear();

        LCD.drawString("Press enter to\n"
                + "stop.", 0, 0);

        ColorSensor cs = new ColorSensor(SensorPort.S1);
        int colour;

        NXTMotor leftMotor = new NXTMotor(MotorPort.B);
        NXTMotor rightMotor = new NXTMotor(MotorPort.C);

        while(Button.ENTER.isUp()) {

            colour = cs.getColor().getColor();

            if(colour == Color.BLACK || colour == Color.BLUE || colour == Color.GREEN
                    || colour == Color.RED || colour == Color.YELLOW) {
                resetSeekCount();
                resetTurnPower();
                rightMotor.forward();
                rightMotor.setPower(STRAIGHT_POWER);
                leftMotor.forward();
                leftMotor.setPower(STRAIGHT_POWER);
            } else {
                checkSeekCount();
                if(turnDirection == LEFT) {
                    leftMotor.stop(); // This could be .backwards() instead but results in very jittery turning.
                    rightMotor.forward();
                } else {
                    leftMotor.forward();
                    rightMotor.stop(); // As above.
                }
                adjustTurnPower();
                leftMotor.setPower(turnPower);
                rightMotor.setPower(turnPower);
            }

        }

        leftMotor.stop();
        rightMotor.stop();

    }

    private static void checkSeekCount() {
        seekCount++;
        if(!isSeeking) {
            if(seekCount > MAX_SEEK) {
                isSeeking = true;
                if(turnDirection == LEFT) {
                    turnDirection = RIGHT;
                } else {
                    turnDirection = LEFT;
                }
            }
        }
    }

    private static void resetSeekCount() {
        seekCount = 0;
        isSeeking = false;
    }

    private static void adjustTurnPower() {
        if(turnPower < MAX_TURN_POWER) {
            turnPower++;
        }
    }

    private static void resetTurnPower() {
        turnPower = 10;
    }

}
