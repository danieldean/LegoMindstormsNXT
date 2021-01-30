package uk.danieldean;

import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.ColorSensor.Color;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;

public class ColourSensorTest {

    public static void main(String[] args) {

        ColorSensor cs = new ColorSensor(SensorPort.S1);

        while(Button.ENTER.isUp()) {
            Color c = cs.getColor();
            LCD.clear();
            LCD.drawString("Red: " + c.getRed() + "\nGreen: " + c.getGreen() + "\nBlue: " + c.getBlue() + "\nColour: " +
                    Colour.getColourName(c.getColor()), 0, 0);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // Do nothing.
            }
        }

    }

    public enum Colour {

        NONE (-1, "None"),
        RED (0, "Red"),
        GREEN (1, "Green"),
        BLUE (2, "Blue"),
        YELLOW (3, "Yellow"),
        MAGNETA (4, "Magneta"),
        ORANGE (5, "Orange"),
        WHITE (6, "White"),
        BLACK (7, "Black"),
        PINK (8, "Pink"),
        GREY (9, "Grey"),
        LIGHT_GREY (10, "Light Grey"),
        DARK_GREY (11, "Dark Grey"),
        CYAN (12, "Cyan");

        private final int id;
        private final String name;

        Colour(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public static String getColourName(int id) {
            for(Colour c : Colour.values()) {
                if(c.id == id) {
                    return c.name;
                }
            }
            return null;
        }

    }

}
