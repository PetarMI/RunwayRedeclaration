package View;

import Controller.Controller;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created 17.02.2015
 *A main method just because
 */
public class Main
{
    public static void main(String[] args)
    {
        Values str1Vals = new Values(3902, 3902, 3902, 3595);
        Strip str1 = new Strip("09L", 9, "L", str1Vals, 306);

        Values str2Vals = new Values(3884, 3962, 3884, 3884);
        Strip str2 = new Strip("27R", 27, "R", str2Vals, 0);

        Runway runway = new Runway("09L/27R", str1, str2);
        runway.addObstacle(new Obstacle("b", 4, 12, 5, "b"), -50, 3464, 0, "");
        runway.recalculateValues(300);
        try {
            PrintHelper.print(runway, "Heathrow", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
