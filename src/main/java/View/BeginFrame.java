package View;

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
    private JPanel mainPane;
    private JButton okBtn;
    private Airport newAirport;
    private XMLHelper xmlHelper;

    //TODO: Bindings must be fixed by using the controller for communication. We must discuss the architecture.
    //TODO: Also, the main method is here just for testing purposes. We have to find a better place for it.

    public static void main (String []args){
        new BeginFrame();
    }

    public BeginFrame(){
        this.doInitializations();
        this.setListeners();
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
    private void setListeners() {
        airportsBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String selectedAirport = airportsBox.getSelectedItem().toString();
                updateRunwayBox(selectedAirport);
            }
        });
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Runway runway = newAirport.getRunway((String) runwayBox.getSelectedItem());
                new CalculusFrame(runway, false);
                BeginFrame.this.dispose();
            }
        });
    }

    private void updateRunwayBox(String newAirportName) {
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
        this.setTitle("Select Airport and Runway");
        this.setSize(WIDTH, HEIGHT);
        //TODO: Minimum/Maximum size or don't allow it to be resized?
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setContentPane(mainPane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

}
