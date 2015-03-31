package View;

import Model.Calculations;

import javax.swing.*;
import java.awt.*;

/**
 * Window to view Calculation breakdown
 */
public class BreakdownFrame extends JFrame
{
    private String strip1Breakdown;
    private String strip2Breakdown;

    public BreakdownFrame(String str1, String str2)
    {
        this.strip1Breakdown = str1;
        this.strip2Breakdown = str2;
        this.init();
        this.setProperties();
    }

    public void init()
    {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        this.setContentPane(panel);

        JTextArea str1Calcs = new JTextArea(20, 55);
        str1Calcs.setEditable(false);
        JScrollPane strip1CalculationsPane = new JScrollPane(str1Calcs);

        JTextArea str2Calcs = new JTextArea(20, 55);
        str1Calcs.setEditable(false);
        JScrollPane strip2CalculationsPane = new JScrollPane(str2Calcs);

        str1Calcs.setText(this.strip1Breakdown);
        str2Calcs.setText(this.strip2Breakdown);

        panel.add(strip1CalculationsPane);
        panel.add(strip2CalculationsPane);
    }

    private void setProperties()
    {
        this.setTitle("Calculation breakdown");
        this.pack();
        this.setVisible(true);
    }
}
