package View;

import Controller.Controller;
import Model.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created 17.02.2015
 *A main method just because
 */
public class Main
{
    public static void main(String[] args) throws Exception
    {
        Values str1Vals = new Values(3902, 3902, 3902, 3595);
        Strip str1 = new Strip("09L", 9, "L", str1Vals, 306);

        Values str2Vals = new Values(3884, 3962, 3884, 3884);
        Strip str2 = new Strip("27R", 27, "R", str2Vals, 0);

        Runway runway = new Runway("09L/27R", str1, str2);
        runway.addObstacle(new Obstacle("b", 1, 12, 1, "b"), -50, 3646, 0, "");

        PrintHelper ph = new PrintHelper();
        ph.print(runway, "Aa", "asd");
        ph.print(runway, "Aa", "asd");
        /*System.out.println(runway.getStrip1().viewCalculationBreakdown());
        System.out.println(runway.getStrip2().viewCalculationBreakdown());
        runway.recalculateValues(300);
        System.out.println(runway.getStrip1().getRecVal());
        System.out.println(runway.getStrip2().getRecVal());
        System.out.println(runway.getStrip1().viewCalculationBreakdown());
        System.out.println(runway.getStrip2().viewCalculationBreakdown());
        runway.addObstacle(new Obstacle("b", 1, 20, 1, "b"), 3546, 50, 0, "");
        runway.recalculateValues(300);
        System.out.println(runway.getStrip1().getRecVal());
        System.out.println(runway.getStrip2().getRecVal());
        System.out.println(runway.getStrip1().viewCalculationBreakdown());
        System.out.println(runway.getStrip2().viewCalculationBreakdown());*/

        /*XMLHelper xmlHelper = new XMLHelper();
        try {
            xmlHelper.addRunway("Glasgow.xml", runway);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }*/
    }
}
