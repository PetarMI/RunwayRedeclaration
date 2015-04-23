package View;

import Model.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


//TODO: Manage the errors with our own exceptions?
public class BeginFrameJavafx extends Application {

    //frame size
    public static final int WIDTH = 300;
    public static final int HEIGHT = 110;

    private XMLHelper xmlHelper;
    private NotifBoard fxNotif;
    private Airport newAirport;

    private Stage stage, runwayStage;
    private GridPane runwayGridPane;
    private ComboBox airportsBox, runwayBox;
    private Button okBtn, addRunway;
    private Boolean twoStrips = false;

    //table for inputting a new runway
    private Button addNewRunway;

    private Label airportNameL;
    private TextField airportName;
    private Label runwayidL, toraL, todaL, asdaL, ldaL, displacedThreshold;
    private TextField runwayid1, tora1, toda1, asda1, lda1, displacedThreshold1;
    private TextField runwayid2, tora2, toda2, asda2, lda2, displacedThreshold2;
    private TextField runwayid3, tora3, toda3, asda3, lda3, displacedThreshold3;
    private TextField runwayid4, tora4, toda4, asda4, lda4, displacedThreshold4;



    public BeginFrameJavafx(NotifBoard fxNotif) {
        this.fxNotif = fxNotif;
      }

    public BeginFrameJavafx() {
        this.fxNotif = new NotifBoard();
    }

    public static void main(String[] args) {launch(args);}

    public void start(Stage primaryStage) {
        stage = primaryStage;
        runwayStage = new Stage();
        setup(primaryStage);
        setUpListeners();

        VBox root = new VBox(5);
        HBox hbox = new HBox();

        hbox.getChildren().addAll(okBtn, addRunway);
        root.getChildren().add(airportsBox);
        root.getChildren().add(runwayBox);
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(hbox);

        stage.setScene(new Scene(root, WIDTH, HEIGHT));
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
        addRunway = new Button("Add New Runway");

        airportsBox.setPrefSize(300, 35);
        runwayBox.setPrefSize(300, 35);
        okBtn.setPrefSize(60, 35);
        addRunway.setPrefSize(110, 35);
        addRunway.setTranslateX(130);



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



    private void obstacleFrameJavafx(Integer numOfStrips) {
        runwayStage.setTitle("Runway settings");
        runwayGridPane = new GridPane();

        runwayGridPane.setPadding(new javafx.geometry.Insets(5, 5, 5, 5));
        runwayGridPane.setHgap(5);
        runwayGridPane.setVgap(5);


        runwayGridPane.add(new Label("1"), 1, 3);
        runwayGridPane.add(new Label("2"), 1, 4);

        addNewRunway = new Button("Add Runway");
        airportNameL = new Label("Airport name: ");
        airportName = new TextField();
        airportName.setTranslateX(80);
        airportName.setMaxWidth(120);



        //adding label headers
        runwayidL = new Label("Runway Designation");
        todaL = new Label("TORA (m)");
        toraL = new Label("TODA (m)");
        asdaL = new Label("ASDA (m)");
        ldaL =  new Label("LDA (m)");
        displacedThreshold = new Label("Displaced Threshold (m)");


        runwayGridPane.add(runwayidL, 2, 1);
        runwayGridPane.add(todaL, 3, 1);
        runwayGridPane.add(toraL, 4, 1);
        runwayGridPane.add(asdaL, 5, 1);
        runwayGridPane.add(ldaL, 6, 1);
        runwayGridPane.add(displacedThreshold, 7, 1);

        runwayid1 = new TextField();
        tora1 = new TextField();
        toda1 = new TextField();
        asda1 = new TextField();
        lda1 = new TextField();
        displacedThreshold1 = new TextField();

        runwayid2 = new TextField();
        tora2 = new TextField();
        toda2 = new TextField();
        asda2 = new TextField();
        lda2 = new TextField();
        displacedThreshold2 = new TextField();



        runwayGridPane.add(runwayid1, 2, 3);
        runwayGridPane.add(tora1, 3, 3);
        runwayGridPane.add(toda1, 4, 3);
        runwayGridPane.add(asda1, 5, 3);
        runwayGridPane.add(lda1, 6, 3);
        runwayGridPane.add(displacedThreshold1, 7, 3);

        runwayGridPane.add(runwayid2, 2, 4);
        runwayGridPane.add(toda2, 3, 4);
        runwayGridPane.add(tora2, 4, 4);
        runwayGridPane.add(asda2, 5, 4);
        runwayGridPane.add(lda2, 6, 4);
        runwayGridPane.add(displacedThreshold2, 7, 4);



        if (numOfStrips == 2)
        {
            this.twoStrips = true;
            runwayGridPane.add(new Label("3"), 1, 5);
            runwayGridPane.add(new Label("4"), 1, 6);

            runwayid3 = new TextField();
            tora3 = new TextField();
            toda3 = new TextField();
            asda3 = new TextField();
            lda3 = new TextField();
            displacedThreshold3 = new TextField();

            runwayid4 = new TextField();
            tora4 = new TextField();
            toda4 = new TextField();
            asda4 = new TextField();
            lda4 = new TextField();
            displacedThreshold4 = new TextField();

            runwayGridPane.add(runwayid3, 2, 5);
            runwayGridPane.add(toda3, 3, 5);
            runwayGridPane.add(tora3, 4, 5);
            runwayGridPane.add(asda3, 5, 5);
            runwayGridPane.add(lda3, 6, 5);
            runwayGridPane.add(displacedThreshold3, 7, 5);

            runwayGridPane.add(runwayid4, 2, 6);
            runwayGridPane.add(tora4, 3, 6);
            runwayGridPane.add(toda4, 4, 6);
            runwayGridPane.add(asda4, 5, 6);
            runwayGridPane.add(lda4, 6, 6);
            runwayGridPane.add(displacedThreshold4, 7, 6);
        }


        runwayGridPane.add(airportNameL, 2, 8);
        runwayGridPane.add(airportName, 2, 8, 2, 1);
        addNewRunway.setPrefSize(120, 40);
        runwayGridPane.add(addNewRunway, 2, 8);
        addNewRunway.setTranslateX(215);


        runwayStage.setScene(new Scene(runwayGridPane));
        runwayStage.setResizable(true);
        runwayStage.sizeToScene();
        runwayStage.centerOnScreen();
        runwayStage.setResizable(true);
        runwayStage.show();
    }



    private void setUpListeners()
    {
        okBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Runway runway = newAirport.getRunway((String) runwayBox.getSelectionModel().getSelectedItem());
                String airport = (String) airportsBox.getSelectionModel().getSelectedItem();
                fxNotif.addNotif(new Notif(Notif.RUNWAY_TITLE, Notif.RUNWAY_IMAGE, runway.getRunwayId() + " at " + airport));
                new CalculusFrameJavafx(runway, airport, fxNotif, false).start(stage);
                if(runwayStage.isShowing()){
                    runwayStage.close();
                }
            }
        });

        addRunway.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                List<Integer> choices = new ArrayList<Integer>();
                choices.add(1);
                choices.add(2);

                Optional<Integer> response = Dialogs.create()
                        .title("Runway Settings")
                        .message("Please select number of airport strips:")
                        .showChoices(choices);

                if (response.isPresent()) {
                    obstacleFrameJavafx(response.get());
                    newRunwayListeners();
                }
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

    private void newRunwayListeners()
    {
        addNewRunway.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try{
                    String newAirportName = airportName.getText();

                    String runwayidTemp1 = runwayid1.getText();
                    int TORA1 = Integer.parseInt(tora1.getText());
                    int TODA1 = Integer.parseInt(toda1.getText());
                    int ASDA1 = Integer.parseInt(asda1.getText());
                    int LDA1 = Integer.parseInt(lda1.getText());
                    int threshold1 = Integer.parseInt(displacedThreshold1.getText());

                    String runwayidTemp2 = runwayid2.getText();
                    int TORA2 = Integer.parseInt(tora2.getText());
                    int TODA2 = Integer.parseInt(toda2.getText());
                    int ASDA2 = Integer.parseInt(asda2.getText());
                    int LDA2 = Integer.parseInt(lda2.getText());
                    int threshold2 = Integer.parseInt(displacedThreshold2.getText());

                    Values values1 = new Values(TORA1, TODA1, ASDA1, LDA1);
                    Values values2 = new Values(TORA2, TODA2, ASDA2, LDA2);

                    Strip strip1 = new Strip(runwayidTemp1,
                            Integer.parseInt(runwayidTemp1.substring(0,2)),
                            runwayidTemp1.substring(2,3),
                            values1,
                            threshold1);

                    Strip strip2 = new Strip(runwayidTemp2,
                            Integer.parseInt(runwayidTemp2.substring(0,2)),
                            runwayidTemp2.substring(2,3),
                            values2,
                            threshold2);

                    Runway runway1 = new Runway(runwayidTemp1, strip1, strip2);

                    //xmlHelper.addRunway(newAirportName, runway1);

                    if(twoStrips)
                    {
                        String runwayidTemp3 = runwayid1.getText();
                        int TORA3 = Integer.parseInt(tora3.getText());
                        int TODA3 = Integer.parseInt(toda3.getText());
                        int ASDA3 = Integer.parseInt(asda3.getText());
                        int LDA3 = Integer.parseInt(lda3.getText());
                        int threshold3 = Integer.parseInt(displacedThreshold1.getText());

                        String runwayidTemp4 = runwayid2.getText();
                        int TORA4 = Integer.parseInt(tora4.getText());
                        int TODA4 = Integer.parseInt(toda4.getText());
                        int ASDA4 = Integer.parseInt(asda4.getText());
                        int LDA4 = Integer.parseInt(lda4.getText());
                        int threshold4 = Integer.parseInt(displacedThreshold2.getText());


                        Values values3 = new Values(TORA3, TODA3, ASDA3, LDA3);
                        Values values4 = new Values(TORA4, TODA4, ASDA4, LDA4);

                        Strip strip3 = new Strip(runwayidTemp3,
                                Integer.parseInt(runwayidTemp3.substring(0,2)),
                                runwayidTemp3.substring(2,3),
                                values3,
                                threshold3);

                        Strip strip4 = new Strip(runwayidTemp4,
                                Integer.parseInt(runwayidTemp4.substring(0,2)),
                                runwayidTemp4.substring(2,3),
                                values4,
                                threshold4);

                        Runway runway2 = new Runway(runwayidTemp1, strip1, strip2);
                    }


                } catch (NumberFormatException e1) {
                    Dialogs.create()
                            .title("Error message")
                            .masthead("One or more of the inputed values are in the wrong format.")
                            .message("Follow the below guidelines" +
                                    "\n- Runway designator should be two numbers followed by a letter ('09L')." +
                                    "\n- TORA, TODA, ASDA, LDA & Displaced Threshold should be positive integers" +
                                    "\n- Specify the Airport name.")
                            .showWarning();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

    }
}