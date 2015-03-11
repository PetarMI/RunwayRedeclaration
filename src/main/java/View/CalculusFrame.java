package View;

import Exceptions.PositiveOnlyException;
import Model.Obstacle;
import Model.Runway;
import Model.Values;
import Model.XMLHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class CalculusFrame extends JFrame{

    //public static final int WIDTH = 675;
    public static final int WIDTH = 1000;
    //public static final int HEIGHT = 400;
    public static final int HEIGHT = 600;
    private XMLHelper xmlHelper;
    private final Runway runway;
    private JPanel mainPane;
    private JButton changeRunwayButton;
    private JComboBox obstaclesComboBox;
    private JButton newObstacleButton;
    private JRadioButton topDownViewRadioButton;
    private JRadioButton sideViewRadioButton;
    private JButton resetOrientationButton;
    private JButton calculateButton;
    private JFormattedTextField posFromLeftText;
    private JFormattedTextField blastAllowanceFormattedTextField;
    private JFormattedTextField centreJFormattedTextField;
    private JLabel origTora2;
    private JLabel recTora2;
    private JLabel origToda2;
    private JLabel recToda2;
    private JLabel origAsda2;
    private JLabel recAsda2;
    private JLabel origLda2;
    private JLabel recLda2;
    private JLabel origTora1;
    private JLabel recTora1;
    private JLabel origToda1;
    private JLabel recToda1;
    private JLabel origAsda1;
    private JLabel recAsda1;
    private JLabel origLda1;
    private JLabel recLda1;
    private JPanel strip2Panel;
    private JPanel strip1Panel;
    private JTextField posFromRightText;
    private JComboBox<String> centrelinePosComboBox;
    private JLabel str1Landing;
    private JLabel str1Takeoff;
    private JLabel str2Landing;
    private JLabel str2Takeoff;
    private JPanel infoPane;
    private JPanel displayPane;
    private JPanel calcPane;
    private JPanel viewPane;
    private JOptionPane optionsPane;
    private boolean testable;

    public CalculusFrame(Runway runway, boolean testable) {
        this.runway = runway;
        this.testable = testable;
        this.doInitializations();
        this.setListeners();
        this.setProperties();
    }


    //TODO: restrict input to Integer (or throw errors?)
    private void doInitializations() {
        xmlHelper = new XMLHelper();
        optionsPane = new JOptionPane();
        this.posFromLeftText.setUI(new HintTextField("Position from Left"));
        this.posFromRightText.setUI(new HintTextField("Position from Right"));
        this.blastAllowanceFormattedTextField.setUI(new HintTextField("Blast Allowance"));
        this.centreJFormattedTextField.setUI(new HintTextField("Centreline distance"));
        this.centrelinePosComboBox.addItem("Above");
        this.centrelinePosComboBox.addItem("Below");

        Values origValues = runway.getStrip1().getOrigVal();
        this.origTora1.setText(String.valueOf(origValues.getTora()));
        this.origToda1.setText(String.valueOf(origValues.getToda()));
        this.origAsda1.setText(String.valueOf(origValues.getAsda()));
        this.origLda1.setText(String.valueOf(origValues.getLda()));

        origValues = runway.getStrip2().getOrigVal();
        this.origTora2.setText(String.valueOf(origValues.getTora()));
        this.origToda2.setText(String.valueOf(origValues.getToda()));
        this.origAsda2.setText(String.valueOf(origValues.getAsda()));
        this.origLda2.setText(String.valueOf(origValues.getLda()));

        this.strip1Panel.setBorder(BorderFactory.createTitledBorder((runway.getStrip1().getStripId())));
        this.strip2Panel.setBorder(BorderFactory.createTitledBorder((runway.getStrip2().getStripId())));
        this.sideViewRadioButton.setVisible(false);
        this.topDownViewRadioButton.setVisible(false);
        this.resetOrientationButton.setVisible(false);
        this.updateObstacleList();
    }

    private void updateRecValues() {
        Values recValues = runway.getStrip1().getRecVal();
        this.recTora1.setText(String.valueOf(recValues.getTora()));
        this.recToda1.setText(String.valueOf(recValues.getToda()));
        this.recAsda1.setText(String.valueOf(recValues.getAsda()));
        this.recLda1.setText(String.valueOf(recValues.getLda()));
        this.str1Landing.setText(recValues.getLanding());
        this.str1Takeoff.setText(recValues.getTakeoff());

        recValues = runway.getStrip2().getRecVal();
        this.recTora2.setText(String.valueOf(recValues.getTora()));
        this.recToda2.setText(String.valueOf(recValues.getToda()));
        this.recAsda2.setText(String.valueOf(recValues.getAsda()));
        this.recLda2.setText(String.valueOf(recValues.getLda()));
        this.str2Landing.setText(recValues.getLanding());
        this.str2Takeoff.setText(recValues.getTakeoff());
    }

    private void setListeners() {
        changeRunwayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BeginFrame();
                CalculusFrame.this.dispose();
            }
        });
        newObstacleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final ObstacleFrame obstacleFrame = new ObstacleFrame(false);
                obstacleFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        CalculusFrame.this.updateObstacleList();
                    }
                });
            }
        });

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Obstacle obs = (Obstacle)obstaclesComboBox.getSelectedItem();
                try {
                    int posFromRight = Integer.parseInt(posFromRightText.getText());
                    int posFromLeft = Integer.parseInt(posFromLeftText.getText());
                    int centrelineDist = Integer.parseInt(centreJFormattedTextField.getText());
                    if (centrelineDist < 0)
                    {
                        throw new PositiveOnlyException();
                    }
                    int blastAllowance = Integer.parseInt(blastAllowanceFormattedTextField.getText());
                    runway.addObstacle(obs, posFromLeft, posFromRight, centrelineDist);
                    runway.recalculateValues(blastAllowance);
                    CalculusFrame.this.updateRecValues();
                }catch (NumberFormatException e1){
                    if(!testable) {
                        optionsPane.showMessageDialog(CalculusFrame.this, "One or more inputted values are not accepted.");
                        e1.printStackTrace();
                    }
                }
                //TODO remove print stack trace
                catch (PositiveOnlyException e1) {
                    if(!testable) {
                        JOptionPane.showMessageDialog(CalculusFrame.this, "Distance from centreline must be greater than 0.");
                        e1.printStackTrace();
                    }
                }

            }
        });
    }

    private void updateObstacleList(){
        List<Obstacle> obstacles = xmlHelper.readObstacles();
        obstaclesComboBox.removeAllItems();
        for(Obstacle o : obstacles){
            System.out.println(o);
            obstaclesComboBox.addItem(o);
        }
    }

    private void setProperties() {
        this.setTitle("Redeclaration");
        this.setSize(WIDTH, HEIGHT);
        //TODO: Minimum/Maximum size or don't allow it to be resized?
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setContentPane(mainPane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }


    public void pressCalculate(){
        calculateButton.doClick();
    }

    public void setPosFromLeft(String s){
        posFromLeftText.setText(s);
    }
    public void setPosFromRight(String s){
        posFromRightText.setText(s);
    }
    public void setBlastAllowance(String s){
        blastAllowanceFormattedTextField.setText(s);
    }

    public void setCentreLineDist(String s){
        centreJFormattedTextField.setText(s);
    }

}
