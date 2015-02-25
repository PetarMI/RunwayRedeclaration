package View;

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

    public static final int WIDTH = 650;
    public static final int HEIGHT = 400;
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
    private JFormattedTextField positionFormattedTextField;
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

    public CalculusFrame(Runway runway) {
        this.runway = runway;
        this.doInitializations();
        this.setListeners();
        this.setProperties();
    }


    //TODO: restrict input to Integer (or throw errors?)
    private void doInitializations() {
        xmlHelper = new XMLHelper();
        this.positionFormattedTextField.setUI(new HintTextFieldUI("Position", true));
        this.blastAllowanceFormattedTextField.setUI(new HintTextFieldUI("Blast Allowance", true));
        this.centreJFormattedTextField.setUI(new HintTextFieldUI("Centreline distance", true));

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

        this.updateObstacleList();
//        this.updateRecValues();
    }

    private void updateRecValues() {
        Values recValues = runway.getStrip1().getRecVal();
        this.recTora1.setText(String.valueOf(recValues.getTora()));
        this.recToda1.setText(String.valueOf(recValues.getToda()));
        this.recAsda1.setText(String.valueOf(recValues.getAsda()));
        this.recLda1.setText(String.valueOf(recValues.getLda()));

        recValues = runway.getStrip2().getRecVal();
        this.recTora2.setText(String.valueOf(recValues.getTora()));
        this.recToda2.setText(String.valueOf(recValues.getToda()));
        this.recAsda2.setText(String.valueOf(recValues.getAsda()));
        this.recLda2.setText(String.valueOf(recValues.getLda()));
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
                final ObstacleFrame obstacleFrame = new ObstacleFrame();
                obstacleFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        CalculusFrame.this.updateObstacleList();
                    }
                });
            }
        });

        //TODO: integrate blast allowance into computation
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Obstacle obs = (Obstacle)obstaclesComboBox.getSelectedItem();
                int pos = Integer.parseInt(positionFormattedTextField.getText());
                int centrelineDist = Integer.parseInt(centreJFormattedTextField.getText());
                runway.addObstacle(obs, pos, centrelineDist);
                runway.recalculateValues();
                CalculusFrame.this.updateRecValues();
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
        this.setName("Select Airport and Runway");
        this.setSize(WIDTH, HEIGHT);
        this.setContentPane(mainPane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

}
