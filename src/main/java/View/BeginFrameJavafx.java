package View;

import Model.Airport;
import Model.Runway;
import Model.XMLHelper;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;


//TODO: Manage the errors with our own exceptions?
public class BeginFrameJavafx extends Application {

    private ComboBox airportsBox;
    private ComboBox runwayBox;
    private Button okBtn;
    private Airport newAirport;
    private XMLHelper xmlHelper;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        setup(primaryStage);
        setUpListeners();

        VBox root = new VBox();
        root.getChildren().add(airportsBox);
        root.getChildren().add(runwayBox);
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(okBtn);
        primaryStage.setScene(new Scene(root, 300, 100));
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    private void setup(Stage primaryStage){
        primaryStage.setTitle("Select Airport & Runway");

        airportsBox = new ComboBox();
        runwayBox = new ComboBox();
        okBtn = new Button();
        airportsBox.setPrefSize(300, 35);
        runwayBox.setPrefSize(300, 35);
        okBtn.setPrefSize(60, 30);
        okBtn.setText("Ok");

        this.xmlHelper = new XMLHelper();
        try {
            List<String> airportNames = xmlHelper.readAllAirports();
            airportsBox.getItems().addAll(airportNames);        //adds all the airports to the ComboBox
            airportsBox.getSelectionModel().selectFirst();      //shows the default value as the first item.
            updateRunwayBox((String) airportsBox.getSelectionModel().getSelectedItem());
            runwayBox.getSelectionModel().selectFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpListeners()
    {
        okBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Runway runway = newAirport.getRunway((String) runwayBox.getSelectionModel().getSelectedItem());
                String airport = (String) airportsBox.getSelectionModel().getSelectedItem();
                new CalculusFrame(runway, airport, false);
            }
        });
    }
    private void updateRunwayBox(String newAirportName) {
        //rebuild the selected file name so it can load the xml
        String fileName = newAirportName + ".xml";
        try {
            newAirport = xmlHelper.readAirport(fileName);
            List<String> runways = newAirport.getRunwayIds();
            runwayBox.getItems().addAll(runways);
            //runwayBox.setModel(new DefaultComboBoxModel(runways.toArray(new String[runways.size()])));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}