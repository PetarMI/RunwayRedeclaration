package View;

import Model.Obstacle;
import Model.Runway;
import Model.Values;
import Model.XMLHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class CalculusPage extends JFrame
{
    //frame size
    public static final int WIDTH = 675;
    public static final int HEIGHT = 350;
    //model references
    private final Runway runway;
    private XMLHelper xmlHelper;
    private boolean testable;
    //private List<Obstacle> obstacles;
    //panels
    private JPanel mainPanel;
    //Obstacle Panel components
    private JButton changeRunway;
    private JComboBox<Obstacle> obstacleComboBox;
    private JButton newObstacleButton;
    private JFormattedTextField posFromLeftText;
    private JFormattedTextField posFromRightText;
    private JFormattedTextField blastAllowanceFormattedTextField;
    private JFormattedTextField centreJFormattedTextField;
    private JComboBox<String> centrelinePositionComboBox;
    private JButton calculate;

    public CalculusPage(Runway runway, boolean testable)
    {
        this.runway = runway;
        this.xmlHelper = new XMLHelper();
        this.testable = testable;
        this.initComponents();
        this.init();
        this.setProperties();
    }

    private void init()
    {
        mainPanel.setLayout(new BorderLayout());

        //Obstacle panel
        JPanel obstaclePanel = new JPanel();
        obstaclePanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 10, 10);
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        obstaclePanel.add(this.changeRunway, c);
        c.gridy = 1;
        obstaclePanel.add(new JLabel("Obstacle:"), c);
        c.gridy = 2;
        obstaclePanel.add(this.obstacleComboBox, c);
        c.gridy = 3;
        obstaclePanel.add(this.newObstacleButton, c);
        c.gridy = 4;
        obstaclePanel.add(this.posFromLeftText, c);
        c.gridy = 5;
        obstaclePanel.add(this.posFromRightText, c);
        c.gridy = 6;
        obstaclePanel.add(this.blastAllowanceFormattedTextField, c);
        c.gridwidth = 1;
        //c.gridwidth = GridBagConstraints.RELATIVE;
        c.gridy = 7;
        c.weightx = 1.0;
        obstaclePanel.add(this.centreJFormattedTextField, c);
        c.weightx = 0.0;
        c.gridx = 1;
        obstaclePanel.add(this.centrelinePositionComboBox, c);
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 8;
        obstaclePanel.add(this.calculate, c);

        //Runway Values panel

        //adding to main panel
        this.mainPanel.add(obstaclePanel, BorderLayout.WEST);
    }

    private void initComponents()
    {
        this.mainPanel = new JPanel();
        this.changeRunway = new JButton("Change Runway");
        this.obstacleComboBox = new JComboBox<Obstacle>();
        this.updateObstacleComboBox();
        this.newObstacleButton = new JButton("Add Custom Obstacle");
        //initialize user input text fields
        this.posFromLeftText = new JFormattedTextField();
        this.posFromRightText = new JFormattedTextField();
        this.blastAllowanceFormattedTextField = new JFormattedTextField();
        this.centreJFormattedTextField = new JFormattedTextField();
        this.posFromLeftText.setUI(new HintTextField("Position from Left"));
        this.posFromRightText.setUI(new HintTextField("Position from Right"));
        this.blastAllowanceFormattedTextField.setUI(new HintTextField("Blast Allowance"));
        this.centreJFormattedTextField.setUI(new HintTextField("Centreline distance"));
        this.centrelinePositionComboBox = new JComboBox<String>(new String[]{"Above", "Below"});
        this.calculate = new JButton("Calculate");
    }

    private void setProperties()
    {
        this.setTitle("Redeclaration");
        this.setSize(WIDTH, HEIGHT);
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void updateObstacleComboBox()
    {
        List<Obstacle> obstacles = xmlHelper.readObstacles();
        this.obstacleComboBox.removeAllItems();
        for(Obstacle o : obstacles){
            System.out.println(o);
            obstacleComboBox.addItem(o);
        }
    }
}
