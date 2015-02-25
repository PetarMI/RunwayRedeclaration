//package View;
//
//import Controller.Controller;
//import Model.Obstacle;
//import Model.Runway;
//import Model.Strip;
//import Model.Values;
//
//import javax.swing.*;
//import java.awt.*;
//
///**
// * Created by PetarMI 17.02.2015
// * The temporary GUI for the project
// * Used for show and tell for Increment 1
// * To be discarded after that
// */
//public class AppTempPage extends JFrame
//{
//    //reference to controller -IMPORTANT PART
//    private Controller controller;
//    //choose the available airports and runways
//    private JComboBox<String> airports;
//    private JComboBox<String> runways;
//    //button for obstacles
//    private JButton addObstacle;
//    private JButton removeObstacle;
//    private JTextField obstacleName;
//    //fields to display information
//    private JTextField airportName;
//    private JTextField runwayID;
//    private JTextField originalTora;
//    private JTextField originalToda;
//    private JTextField originalAsda;
//    private JTextField originalLda;
//    private JTextField updatedTora;
//    private JTextField updatedToda;
//    private JTextField updatedAsda;
//    private JTextField updatedLda;
//
//    public AppTempPage()
//    {
//        this.airports = new JComboBox<String>();
//        this.runways = new JComboBox<String>();
//        this.addObstacle = new JButton("Add obstacle");
//        this.removeObstacle = new JButton("Remove");
//        this.obstacleName = new JTextField("None", 10);
//        this.obstacleName.setEditable(false);
//        //set values and settings to the text fields
//        this.airportName = new JTextField("None", 10);
//        this.airportName.setEditable(false);
//        this.airportName.setForeground(Color.RED);
//        this.runwayID = new JTextField("None", 10);
//        this.runwayID.setEditable(false);
//        this.runwayID.setForeground(Color.RED);
//        this.originalTora = new JTextField(6);
//        this.originalTora.setEditable(false);
//        this.originalToda = new JTextField(6);
//        this.originalToda.setEditable(false);
//        this.originalAsda = new JTextField(6);
//        this.originalAsda.setEditable(false);
//        this.originalLda = new JTextField(6);
//        this.originalLda.setEditable(false);
//        this.updatedTora = new JTextField(6);
//        this.updatedTora.setEditable(false);
//        this.updatedToda = new JTextField(6);
//        this.updatedToda.setEditable(false);
//        this.updatedAsda = new JTextField(6);
//        this.updatedAsda.setEditable(false);
//        this.updatedLda = new JTextField(6);
//        this.updatedLda.setEditable(false);
//    }
//
//    public static void main(String[] args)
//    {
//        /*AppTempPage atp = new AppTempPage();
//        atp.init();*/
//
//        /*Values str1Vals = new Values(3902, 3902, 3902, 3595);
//        Strip str1 = new Strip("09L/27R", 9, "L", str1Vals);
//
//        Values str2Vals = new Values(3884, 3962, 3884, 3884);
//        Strip str2 = new Strip("jfglks", 27, "R", str2Vals);*/
//
//        Values str1Vals = new Values(3660, 3660, 3660, 3353);
//        Strip str1 = new Strip("09L/27R", 9, "L", str1Vals);
//
//        Values str2Vals = new Values(3660, 3660, 3660, 3660);
//        Strip str2 = new Strip("jfglks", 27, "R", str2Vals);
//
//        Runway runway = new Runway("09L/27R", str1, str2);
//        runway.addObstacle(new Obstacle("b", 1, 15, 1, "b"), 456, 60);
////        runway.recalculateValues();
//        System.out.println(str1.getOrigVal().toString());
//        System.out.println(str1.getRecVal().toString());
//        System.out.println(str2.getOrigVal().toString());
//        System.out.println(str2.getRecVal().toString());
//    }
//
//    public void init()
//    {
//        //boring GUI declarations
//        JPanel mainPanel = new JPanel();
//        this.setContentPane(mainPanel);
//        mainPanel.setLayout(new GridBagLayout());
//        GridBagConstraints c = new GridBagConstraints();
//
//        c.insets = new Insets(0, 0, 10, 10);
//        c.gridwidth = 2;
//        mainPanel.add(new JLabel("Current Airport:"), c);
//        c.gridy = 1;
//        mainPanel.add(new JLabel("Current Runway"), c);
//        c.gridwidth = 1;
//        c.gridx = 2;
//        c.gridy = 0;
//        mainPanel.add(airportName, c);
//        c.gridy = 1;
//        mainPanel.add(runwayID, c);
//        c.gridy = 0;
//        c.gridx = 3;
//        c.gridwidth = 2;
//        mainPanel.add(airports, c);
//        c.gridy = 1;
//        mainPanel.add(runways, c);
//        c.gridy = 2;
//        c.gridx = 0;
//        c.gridwidth = 1;
//        mainPanel.add(new JLabel("TORA"), c);
//        c.gridx = 1;
//        mainPanel.add(new JLabel("TODA"), c);
//        c.gridx = 2;
//        mainPanel.add(new JLabel("ASDA"), c);
//        c.gridx = 3;
//        mainPanel.add(new JLabel("LDA"), c);
//        //fields for originals
//        c.gridy = 3;
//        c.gridx = 0;
//        mainPanel.add(originalTora, c);
//        c.gridx = 1;
//        mainPanel.add(originalToda, c);
//        c.gridx = 2;
//        mainPanel.add(originalAsda, c);
//        c.gridx = 3;
//        mainPanel.add(originalLda, c);
//        //fields for obstacle
//        c.gridy = 4;
//        c.gridx = 0;
//        mainPanel.add(new JLabel("Obstacle:"), c);
//        c.gridx = 1;
//        mainPanel.add(obstacleName, c);
//        c.gridx = 2;
//        mainPanel.add(addObstacle, c);
//        c.gridx = 3;
//        mainPanel.add(removeObstacle, c);
//        //fields for updated
//        c.gridy = 5;
//        c.gridx = 0;
//        mainPanel.add(updatedTora, c);
//        c.gridx = 1;
//        mainPanel.add(updatedToda, c);
//        c.gridx = 2;
//        mainPanel.add(updatedAsda, c);
//        c.gridx = 3;
//        mainPanel.add(updatedLda, c);
//
//        this.pack();
//        this.setVisible(true);
//    }
//
//    public void setController(Controller contr)
//    {
//        this.controller = contr;
//    }
//}
