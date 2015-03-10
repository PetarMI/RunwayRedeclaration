package Controller;

import Exceptions.FieldEmptyException;
import Exceptions.PositiveOnlyException;
import Model.Airport;
import Model.Obstacle;
import Model.Runway;
import Model.XMLHelper;
import View.BeginPanel;
import View.CalculusPanel;
import View.ObstaclePanel;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

/**
 * Author(s) Petar, Metin, Tom
 * Controller class for handling interactions between Model and View
 */
public class Controller {

    private Airport currentAirport;
    private List<String> availableAirports;
    private XMLHelper xmlHelper;
    private Runway runway;
    private static boolean testable;
    private static JFrame everythingHolder;
    private static JPanel cardHolder;
    private static CardLayout cards;
    private static BeginPanel beginPanel;
    private static ObstaclePanel obsPanel;
    private static CalculusPanel calcPanel;

    public Controller() {
        xmlHelper = new XMLHelper();
        loadAirports();
        init();
    }

    public static void main (String[] args)
    {
        new Controller();
    }

    private void init()
    {
        testable = true;

        cards = new CardLayout();
        cardHolder = new JPanel(cards);
        everythingHolder = new JFrame();
        everythingHolder.setLayout(cards);

        SetupListener snrl = new SelectNewRunwayListener();
        SetupListener btcl = new BeginToCalculusListener();
        beginPanel = new BeginPanel(snrl, btcl);

        SetupListener nol = new NewObstacleListener();
        obsPanel = new ObstaclePanel(testable, nol);

        ActionListener ctbl = new CalculusToBeginListener();
        ActionListener btol = new BeginToObstacleListener();
        SetupListener aol = new AddObstacleListener();
        calcPanel = new CalculusPanel(null, testable, ctbl, btol, aol);

        cardHolder.add(beginPanel, "begin");
        cardHolder.add(obsPanel, "obstacles");
        cardHolder.add(calcPanel, "calculations");

        everythingHolder.setContentPane(cardHolder);
        everythingHolder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        everythingHolder.setVisible(true);

        /*obsPanel*/everythingHolder.setTitle("Add obstacle");
        /*beginPanel*/everythingHolder.setTitle("Select Airport and Runway");
        /*calcPanel*/everythingHolder.setTitle("Redeclaration");
    }

    //from ObstaclePanel
    class NewObstacleListener extends  SetupListener {
        private JTextField nameTextField, heightTextField, widthTextField, lengthTextField;
        private  JTextArea descriptionTextArea;
        private boolean setup = false;

        public void useThis(Object[] stuff) {
            setup = true;
            nameTextField = (JTextField) stuff[0];
            heightTextField = (JTextField) stuff[1];
            widthTextField = (JTextField) stuff[2];
            lengthTextField = (JTextField) stuff[3];
            descriptionTextArea = (JTextArea) stuff[4];
        }

        public void actionPerformed(ActionEvent e) {
            try {
                if (setup = false){
                    System.out.println("You haven't set up the NewObstacleListener");
                }
                String name = nameTextField.getText();

                Integer height = Integer.parseInt(heightTextField.getText());
                int width = Integer.parseInt(widthTextField.getText());
                int length = Integer.parseInt(lengthTextField.getText());
                String description = descriptionTextArea.getText();
                if (name.equals("")) {
                    throw new FieldEmptyException();
                }
                if ((width <= 0) || (height <= 0) || (length <= 0)) {
                    throw new PositiveOnlyException();
                }
                XMLHelper xmlHelper = new XMLHelper();
                xmlHelper.addObstacleXML(new Obstacle(name, width, height, length, description));
                //ObstaclePanel.this.dispose();
            } catch (NumberFormatException e1) {
                if (!testable) {
                    JOptionPane.showMessageDialog(obsPanel, "Height, width and length must be a number.");
                    e1.printStackTrace();
                }
            } catch (FieldEmptyException e1) {
                if (!testable) {
                    JOptionPane.showMessageDialog(obsPanel, "Name field cannot be empty.");
                    e1.printStackTrace();
                }
            } catch (PositiveOnlyException e1) {
                if (!testable) {
                    JOptionPane.showMessageDialog(obsPanel, "Height, width and length must be greater than 0.");
                    e1.printStackTrace();
                }
            }
        }
    }

    //from CalculusPanel
        //changeRunwayButton
    class CalculusToBeginListener implements  ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                cards.show(cardHolder, "begin");
            }
        }
        //newObstacleButton
    class BeginToObstacleListener implements  ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcPanel.updateObstacleList();
                cards.show(cardHolder, "obstacles");
            }
        }

        //calculateButton
    class AddObstacleListener extends SetupListener {
            private JComboBox obstaclesComboBox;
            private JTextField posFromRightText, posFromLeftText, centreJFormattedTextField, blastAllowanceFormattedTextField;
            private JOptionPane optionsPane;
            private boolean setup = false;

            public void useThis(Object[] stuff) {
                setup = true;
                obstaclesComboBox = (JComboBox) stuff[0];
                posFromRightText = (JTextField) stuff[1];
                posFromLeftText = (JTextField) stuff[2];
                centreJFormattedTextField = (JTextField) stuff[3];
                blastAllowanceFormattedTextField = (JTextField) stuff[4];
                optionsPane = (JOptionPane) stuff[5];
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (setup = false){
                    System.out.println("You haven't setup the AddObstacleListener");
                }
                Obstacle obs = (Obstacle) obstaclesComboBox.getSelectedItem();
                try {
                    int posFromRight = Integer.parseInt(posFromRightText.getText());
                    int posFromLeft = Integer.parseInt(posFromLeftText.getText());
                    int centrelineDist = Integer.parseInt(centreJFormattedTextField.getText());
                    int blastAllowance = Integer.parseInt(blastAllowanceFormattedTextField.getText());
                    runway.addObstacle(obs, posFromLeft, posFromRight, centrelineDist);
                    runway.recalculateValues(blastAllowance);
                    calcPanel.updateRecValues();
                } catch (NumberFormatException e1) {
                    if (!testable) {
                        optionsPane.showMessageDialog(calcPanel, "One or more inputted values are not accepted.");
                        e1.printStackTrace();
                    }
                }
            }
        }
    //TODO: Manage the errors with our own exceptions?
    // From BeginPanel
            //airportsBox
    class SelectNewRunwayListener extends  SetupListener {
        private JComboBox airportsBox;
        private boolean setup = false;

        public void useThis (Object[] stuff) {
            setup = true;
            airportsBox = (JComboBox) stuff[0];
        }
            @Override
            public void actionPerformed(ActionEvent e) {
                if (setup = false){
                    System.out.println("You haven't set up the SelectNewRunwayListener");
                }
                String selectedAirport = airportsBox.getSelectedItem().toString();
                beginPanel.updateRunwayBox(selectedAirport);
            }
        }

        //okBtn
        class BeginToCalculusListener extends SetupListener {
            private JComboBox runwayBox;
            private boolean setup = false;
            public void useThis(Object[] stuff) {
                setup = true;
                runwayBox = (JComboBox) stuff[0];
            }
            @Override
            public void actionPerformed(ActionEvent e) {
                if (setup = false){
                   System.out.println("You haven't set up the BeginToCalculusListener");
                }
                Runway runway = currentAirport.getRunway((String) runwayBox.getSelectedItem());
            }
        }

    public void setCurrentAirport(String airportName){
        try {
            this.currentAirport = this.xmlHelper.readAirport(airportName);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    private void loadAirports(){
        try {
            this.availableAirports = xmlHelper.readAllAirports();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}
