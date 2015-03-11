package View;

import Controller.SetupListener;
import Model.Airport;
import Model.Runway;
import Model.XMLHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BeginFrame extends JFrame {

    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;

    private JComboBox airportsBox;
    private JComboBox runwayBox;
    private JButton okBtn;
    private JPanel mainPane;
    private Airport newAirport;
    private XMLHelper xmlHelper;

    //TODO: Bindings must be fixed by using the controller for communication. We must discuss the architecture.
    //TODO: Also, the main method is here just for testing purposes. We have to find a better place for it.

    public BeginFrame(){
        this.setContentPane(mainPane);
        this.doInitializations();
        this.setProperties();
    }

    private void doInitializations() {
        this.xmlHelper = new XMLHelper();
        try {
            List<String> airportNames = xmlHelper.readAllAirports();
            //ugly code that puts the airport names into the combobox's model
            ComboBoxModel<String> model = new DefaultComboBoxModel<String>(airportNames.toArray(new String[airportNames.size()]));
            airportsBox.setModel(model);
            updateRunwayBox((String) airportsBox.getItemAt(0));
        } catch (Exception e) {
            JOptionPane.showInputDialog(null, "Error", "Some information couldn't be read from the cached airports");
            e.printStackTrace();
        }
    }

    //TODO: Manage the errors with our own exceptions?
    public void setListeners(SetupListener listener1, SetupListener listener2) {
        listener1.useThis(new Object[]{airportsBox});
        airportsBox.addActionListener(listener1);
        listener2.useThis(new Object[]{runwayBox});
        okBtn.addActionListener(listener2);
    }

    public void updateRunwayBox(String newAirportName) {
        //rebuild the selected file name so it can load the xml
        String fileName = newAirportName + ".xml";
        try {
            newAirport = xmlHelper.readAirport(fileName);
            List<String> runways = newAirport.getRunwayIds();
            runwayBox.setModel(new DefaultComboBoxModel(runways.toArray(new String[runways.size()])));
        } catch (Exception e1) {
            JOptionPane.showInputDialog(null, "Error", "Some information couldn't be read from the cached obstacles");
            e1.printStackTrace();
        }
    }

    private void setProperties() {
        this.setSize(WIDTH, HEIGHT);
        this.setVisible(true);
    }

}
