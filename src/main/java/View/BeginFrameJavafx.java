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

    private Stage stage;
    private ComboBox airportsBox, runwayBox;
    private Button okBtn;
    private Airport newAirport;
    private XMLHelper xmlHelper;
    private NotifBoard fxNotif;

    public BeginFrameJavafx(NotifBoard fxNotif) {
        this.fxNotif = fxNotif;
    }

    public BeginFrameJavafx() {
        this.fxNotif = new NotifBoard();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        stage = primaryStage;
        setup(primaryStage);
        setUpListeners();

        VBox root = new VBox();
        root.getChildren().add(airportsBox);
        root.getChildren().add(runwayBox);
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(okBtn);

        Scene myScene = new Scene(root, 300, 100);
        stage.setScene(myScene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

    private void setup(Stage primaryStage){
        primaryStage.setTitle("Select Airport & Runway");

        airportsBox = new ComboBox();
        runwayBox = new ComboBox();
        okBtn = new Button("Ok");
        airportsBox.setPrefSize(300, 35);
        runwayBox.setPrefSize(300, 35);
        okBtn.setPrefSize(60, 30);


        this.xmlHelper = new XMLHelper();
        try {
            List<String> airportNames = xmlHelper.readAllAirports();
            airportsBox.getItems().addAll(airportNames);        //adds all the airports to the ComboBox
            airportsBox.getSelectionModel().selectFirst();      //shows the default value as the first item.
            updateRunwayBox((String) airportsBox.getSelectionModel().getSelectedItem());
            runwayBox.getSelectionModel().selectFirst();
            System.out.println("Airport: " + airportsBox.getSelectionModel().getSelectedItem());
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
                //new CalculusFrame(runway, airport, false);
                fxNotif.addNotif(new Notif(Notif.RUNWAY_TITLE, Notif.RUNWAY_IMAGE, runway.getRunwayId() + " at " + airport));
                new CalculusFrameJavafx(runway, airport, fxNotif, false).start(stage);
                }
        });
        airportsBox.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String selectedAirport = airportsBox.getSelectionModel().getSelectedItem().toString();
                updateRunwayBox(selectedAirport);
                runwayBox.getSelectionModel().selectFirst();
            }
        });
    }
    private void updateRunwayBox(String newAirportName) {
        //rebuild the selected file name so it can load the xml
        String fileName = newAirportName + ".xml";
        try {
            runwayBox.getSelectionModel().clearSelection();
            runwayBox.getItems().clear();
            newAirport = xmlHelper.readAirport(fileName);
            List<String> runways = newAirport.getRunwayIds();
            runwayBox.getItems().addAll(runways);
            //runwayBox.setModel(new DefaultComboBoxModel(runways.toArray(new String[runways.size()])));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}