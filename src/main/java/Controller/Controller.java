package Controller;

import Exceptions.FieldEmptyException;
import Exceptions.PositiveOnlyException;
import Model.Airport;
import Model.Obstacle;
import Model.Runway;
import Model.XMLHelper;
import View.BeginFrame;
import View.CalculusFrame;
import View.ObstacleFrame;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

/**
 * Author(s) Petar, Metin, Tom, Ed
 * Controller class for handling interactions between Model and View
 */
public class Controller {

    private Airport currentAirport;
    private List<String> availableAirports;
    private XMLHelper xmlHelper;
    private Runway runway;
    private static boolean testable = true;
    private static BeginFrame beginFrame;
    private static ObstacleFrame obsFrame;
    private static CalculusFrame calcFrame;
    private static SetupListener snrl, btcl, nol, aol;
    private static ActionListener ctbl, btol;
    private static WindowAdapter ocl;

    public Controller() {
        xmlHelper = new XMLHelper();
        loadAirports();
        initListeners();
        makeBeginFrame();
    }

    public static void main (String[] args)
    {
        new Controller();
    }

    private void initListeners() {
        snrl = new SelectNewRunwayListener();
        btcl = new BeginToCalculusListener();
        ocl = new ObstacleClosedListener();
        nol = new NewObstacleListener();
        ctbl = new CalculusToBeginListener();
        btol = new CalculusToObstacleListener();
        aol = new AddObstacleListener();
    }

    private void makeBeginFrame() {
        beginFrame = new BeginFrame();
        beginFrame.setListeners(snrl, btcl);
    }
    private void makeObstacleFrame() {
        obsFrame = new ObstacleFrame(testable);
        obsFrame.setListeners(nol, ocl);
    }
    private void makeCalculusFrame() {
        calcFrame = new CalculusFrame(runway, testable);
        calcFrame.setListeners(ctbl, btol, aol);
    }

    //from ObstacleFrame
    class ObstacleClosedListener extends WindowAdapter
    {
        public void windowClosing(WindowEvent e) {
            makeCalculusFrame();
            calcFrame.updateObstacleList();
            obsFrame.dispose();
        }
    }
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
                //ObstacleFrame.this.dispose();
            } catch (NumberFormatException e1) {
                if (!testable) {
                    JOptionPane.showMessageDialog(obsFrame, "Height, width and length must be a number.");
                    e1.printStackTrace();
                }
            } catch (FieldEmptyException e1) {
                if (!testable) {
                    JOptionPane.showMessageDialog(obsFrame, "Name field cannot be empty.");
                    e1.printStackTrace();
                }
            } catch (PositiveOnlyException e1) {
                if (!testable) {
                    JOptionPane.showMessageDialog(obsFrame, "Height, width and length must be greater than 0.");
                    e1.printStackTrace();
                }
            }
        }
    }

    //from CalculusFrame
    //changeRunwayButton
    class CalculusToBeginListener implements  ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcFrame.dispose();
                makeBeginFrame();
            }
        }
    //newObstacleButton
    class CalculusToObstacleListener implements  ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcFrame.updateObstacleList();
                calcFrame.dispose();
                makeObstacleFrame();
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
                    if (centrelineDist < 0)
                    {
                        throw new PositiveOnlyException();
                    }
                    int blastAllowance = Integer.parseInt(blastAllowanceFormattedTextField.getText());
                    runway.addObstacle(obs, posFromLeft, posFromRight, centrelineDist);
                    runway.recalculateValues(blastAllowance);
                    calcFrame.updateRecValues();
                } catch (NumberFormatException e1) {
                    if (!testable) {
                        optionsPane.showMessageDialog(calcFrame, "One or more inputted values are not accepted.");
                        e1.printStackTrace();
                    }
                }catch (PositiveOnlyException e1) {
                    if(!testable) {
                        JOptionPane.showMessageDialog(calcFrame, "Distance from centreline must be greater than 0.");
                        e1.printStackTrace();
                    }
                }
            }
        }
    //TODO: Manage the errors with our own exceptions?
    //From BeginFrame
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
                beginFrame.updateRunwayBox(selectedAirport);
            }
        }
        //okBtn
        class BeginToCalculusListener extends SetupListener {
            private JComboBox runwayBox,airportsBox;
            private boolean setup = false;
            public void useThis(Object[] stuff) {
                setup = true;
                runwayBox = (JComboBox) stuff[0];
                airportsBox = (JComboBox) stuff[1];
            }
            @Override
            public void actionPerformed(ActionEvent e) {
                if (setup = false){
                   System.out.println("You haven't set up the BeginToCalculusListener");
                }
                try {
                    currentAirport = xmlHelper.readAirport(airportsBox.getSelectedItem().toString().concat((".xml")));
                    runway = currentAirport.getRunway((String) runwayBox.getSelectedItem());
                } catch (ParserConfigurationException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (SAXException e1) {
                    e1.printStackTrace();
                }
                beginFrame.dispose();
                makeCalculusFrame();
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
