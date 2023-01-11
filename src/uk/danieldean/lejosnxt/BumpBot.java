package uk.danieldean.lejosnxt;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.robotics.objectdetection.Feature;
import lejos.robotics.objectdetection.FeatureDetector;
import lejos.robotics.objectdetection.FeatureListener;
import lejos.robotics.objectdetection.TouchFeatureDetector;

/* A basic robot that drives, hits an object, and then reverses
 * and changes direction.
 *
 * Ideally the robots length needs to be less than or the same
 * as its width otherwise room corners can pose an issue.
 */
public class BumpBot implements FeatureListener, Runnable {

    /* Set to robots measurements in millimetres.
     * Tyre diameter and width show be printed on
     * the tyre side wall. Robot width will need
     * measurement.
     */
    private final static double ROBOT_WIDTH = 158;
    private final static double TYRE_DIAMETER = 43.2;
    private final static double TYRE_WIDTH = 22;

    private final Object lock = new Object();
    private Boolean objectDetected = false;
    private volatile boolean stop = false;
    private double tyreCircumference = 0;
    private double robotTrack = 0;
    private int reverseDegress = 0;
    private int turnDegrees = 0;

    public static void main(String[] args) {

        // Show message to begin.
        LCD.drawString("Press any button\n"
                + "to begin.", 0, 0);
        Button.waitForAnyPress();
        LCD.clear();

        // Initialise based on robot measurements and start.
        BumpBot bb = new BumpBot(ROBOT_WIDTH, TYRE_DIAMETER, TYRE_WIDTH);
        Thread th = new Thread(bb);
        th.start();

        // Show message to stop.
        LCD.drawString("Press enter to\n"
                + "stop.", 0, 0);
        Button.ENTER.waitForPress();

        // Set stop flag for thread to exit.
        bb.stop();

    }

    public BumpBot(double robotWidth, double tyreDiameter, double tyreWidth) {

        // Tyre circumference needed to calculate rotations.
        tyreCircumference = tyreDiameter * Math.PI;
        // Track is distance between the centreline of the wheels.
        robotTrack = robotWidth - tyreWidth;
        // Reverse half the robots width. Regardless of build this should be enough to turn.
        reverseDegress = distanceToRotationDegrees(robotWidth / 2);
        // Turn 90 degrees on the spot.
        turnDegrees = turnDegreesToRotationDegrees(90);

        /* Add listener to detect objects. Port could be set
         * in the constructor?
         */
        TouchSensor ts = new TouchSensor(SensorPort.S1);
        TouchFeatureDetector fd = new TouchFeatureDetector(ts);
        fd.addListener(this);

    }

    @Override
    public void run() {

        /* Initialise left and right motor. Ports could be set in the
         * constructor?
         */
        NXTRegulatedMotor leftMotor = new NXTRegulatedMotor(MotorPort.B);
        NXTRegulatedMotor rightMotor = new NXTRegulatedMotor(MotorPort.C);

        // Continue until stop flag is set.
        while(!stop) {

            // Go forwards until object detected flag is set.
            if(!checkObjectDetected()) {

                rightMotor.forward();
                leftMotor.forward();

            } else {

                // Stop forward drive.
                rightMotor.stop(true);
                leftMotor.stop();

                // Reverse robots width.
                rightMotor.rotate(-reverseDegress, true);
                leftMotor.rotate(-reverseDegress);

                // Random 90 degree turn left or right.
                if(Math.random() >= 0.5) {
                    rightMotor.rotate(-turnDegrees, true);
                    leftMotor.rotate(turnDegrees);
                } else {
                    rightMotor.rotate(turnDegrees, true);
                    leftMotor.rotate(-turnDegrees);
                }

                // Reset object detected flag.
                setObjectDetected(false);

            }

        }

    }

    private int distanceToRotationDegrees(double distance) {
        /* Calculate number of wheel rotations to achieve distance
         * and convert to rotation degrees.
         */
        return (int) (distance / tyreCircumference * 360);
    }

    private int turnDegreesToRotationDegrees(int turnDegrees) {
        /* Calculate number of wheel rotations to turn on the spot
         * a number of degrees. Covert rotations to degrees.
         */
        return (int) (robotTrack * Math.PI / tyreCircumference * turnDegrees);
    }

    public void stop() {
        // Set control flag to exit running loop.
        stop = true;
    }

    private boolean checkObjectDetected() {
        /* Check if an object is detected.
         * Synchronised as can be set by two threads.
         * Only read from one.
         */
        synchronized(lock) {
             return objectDetected.booleanValue();
        }
    }

    private void setObjectDetected(boolean newValue) {
        // Set object detected state. Synchronised as above.
        synchronized(lock) {
             objectDetected = newValue;
        }
    }

    @Override
    public void featureDetected(Feature feature, FeatureDetector detector) {
        // Run when an object is detected. Touch sensor so range will be zero.
        setObjectDetected(true);
    }

}
