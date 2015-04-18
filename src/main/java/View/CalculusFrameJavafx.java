package View;

import Exceptions.PositiveOnlyException;
import Model.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import java.util.Arrays;
import java.util.List;

public class CalculusFrameJavafx extends Application {

    //frame size
    public static final int WIDTH = 1500;
    public static final int HEIGHT = 800;

    //model references
    private XMLHelper xmlHelper;
    private Runway runway;
    private ThreeDVisuals threeD;
    private NotifBoard fxNotif;

    private final String airport;
    private boolean testable;
    private Stage stage;
    private Stage popUp;
    private Button okBtn;

    //Top LHS components
    private Button chgnRunway, addCustObs, compassOrient, calculate;
    private Button topDown, sideOnLeft, sideOnRight;
    private ComboBox obstaclesBox, centrelineBox;
    private TextField posFromLeft, posFromRight, BPV, centrelineDist;
    private Label obstacleLabel, posFromLeftL, posFromRightL, BPVL, centrelineDistL;

    //Value components
    private Label TORA1, TODA1, ASDA1, LDA1;
    private Label TORA2, TODA2, ASDA2, LDA2;
    private Label strip1, strip2, originalValues1, originalValues2, recValues1, recValues2;
    private Label instructions1, instructions2, land1, land2, takeOff1, takeOff2;
    private Label oTora1, oToda1, oAsda1, oLda1;
    private Label recTora1, recToda1, recAsda1, recLda1;
    private Label oTora2, oToda2, oAsda2, oLda2;
    private Label recTora2, recToda2, recAsda2, recLda2;

    public CalculusFrameJavafx(Runway runway, String airport, Boolean testable) {
        this.runway = runway;
        this.airport = airport;
        this.testable = testable;

        xmlHelper = new XMLHelper();
        threeD = new ThreeDVisuals();
        setupLHScomponents();
        setupStripValues();
        updateObstacleComboBox();
        populateComponents();
        //updateRecValues();
        setListeners();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        stage.setTitle("Runway Redeclaration (" + airport +")");

        //main pane
        BorderPane root = new BorderPane();
        GridPane gridpane = new GridPane();
        root.setLeft(gridpane);


        //gridpane setup
        gridpane.setPadding(new Insets(5, 5, 5, 5));
        gridpane.setHgap(5);
        gridpane.setVgap(7);

        gridpane.add(chgnRunway, 1, 1);
        gridpane.add(obstacleLabel, 1, 2);
        gridpane.add(obstaclesBox, 1, 3);
        gridpane.add(addCustObs, 1, 4);

        gridpane.add(posFromLeftL, 1, 5);
        gridpane.add(posFromLeft, 1, 5);
        gridpane.add(posFromRightL, 1, 6);
        gridpane.add(posFromRight, 1, 6);
        gridpane.add(BPVL, 1, 7);
        gridpane.add(BPV, 1, 7);
        gridpane.add(centrelineDistL, 1, 8);
        gridpane.add(centrelineDist, 1, 8);
        gridpane.add(centrelineBox, 1, 8);

        gridpane.add(calculate, 1, 9);
        gridpane.add(topDown, 1, 10);
        gridpane.add(sideOnLeft, 1, 11);
        gridpane.add(sideOnRight, 1, 11);
        gridpane.add(compassOrient, 1, 12);


        //----Values
        //1st row
        gridpane.add(TORA1, 1, 16);
        gridpane.add(TODA1, 1, 16);
        gridpane.add(ASDA1, 1, 16);
        gridpane.add(LDA1, 1, 16);

        //1st column
        gridpane.add(strip1, 1, 17);
        //2nd row
        gridpane.add(originalValues1, 1, 18);
        gridpane.add(oTora1, 1, 18);
        gridpane.add(oToda1, 1, 18);
        gridpane.add(oAsda1, 1, 18);
        gridpane.add(oLda1, 1, 18);

        //3rd row
        gridpane.add(recValues1, 1, 19);
        gridpane.add(recTora1, 1, 19);
        gridpane.add(recToda1, 1, 19);
        gridpane.add(recAsda1, 1, 19);
        gridpane.add(recLda1, 1, 19);

        //4th row
        gridpane.add(instructions1, 1, 20);
        gridpane.add(land1, 1, 20);
        gridpane.add(takeOff1, 1, 20);

        //5th row
        gridpane.add(TORA2, 1, 23);
        gridpane.add(TODA2, 1, 23);
        gridpane.add(ASDA2, 1, 23);
        gridpane.add(LDA2, 1, 23);


        //first column continued
        gridpane.add(strip2, 1, 24);
        //6th row
        gridpane.add(originalValues2, 1, 25);
        gridpane.add(oTora2, 1, 25);
        gridpane.add(oToda2, 1, 25);
        gridpane.add(oAsda2, 1, 25);
        gridpane.add(oLda2, 1, 25);

        //7th row
        gridpane.add(recValues2, 1, 26);
        gridpane.add(recTora2, 1, 26);
        gridpane.add(recToda2, 1, 26);
        gridpane.add(recAsda2, 1, 26);
        gridpane.add(recLda2, 1, 26);

        //8th row
        gridpane.add(instructions2, 1, 27);
        gridpane.add(land2, 1, 27);
        gridpane.add(takeOff2, 1, 27);


        //gridpane.setOpacity();
        stage.setScene(new Scene(root, WIDTH, HEIGHT));
        stage.setResizable(true);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

    private void setupLHScomponents() {
        //initialising all the components
        chgnRunway = new Button("Change Runway");
        obstacleLabel = new Label("Obstacle: ");
        obstaclesBox = new ComboBox();
       // obstaclesBox.setPromptText("Block: H:20, L:10, W:5");
        addCustObs = new Button("Add Custom Obstacle");

        posFromLeftL = new Label("Position from left:");
        posFromLeft = new TextField();
        posFromRightL = new Label("Position from right:");
        posFromRight = new TextField();
        BPVL = new Label("Blast Allowance:");
        BPV = new TextField();
        centrelineDistL = new Label("Centreline distance:");
        centrelineDist = new TextField();
        centrelineBox = new ComboBox();
        centrelineBox.getItems().add("Above");
        centrelineBox.getItems().add("Below");
        centrelineBox.getSelectionModel().selectFirst();

        calculate = new Button("Calculate");
        topDown = new Button("Top-Down View");
        sideOnLeft = new Button("Side on Left");
        sideOnRight = new Button("Side on Right");
        compassOrient = new Button("Compass Orientation");

        //widths of all the components
        chgnRunway.setPrefWidth(240);
        obstaclesBox.setPrefWidth(240);
        addCustObs.setPrefWidth(240);
        posFromLeft.setMaxWidth(135);
        posFromRight.setMaxWidth(135);
        BPV.setMaxWidth(135);
        centrelineDist.setMaxWidth(51);
        centrelineBox.setPrefWidth(77);
        calculate.setPrefWidth(240);
        topDown.setPrefWidth(240);
        compassOrient.setPrefWidth(240);
        sideOnLeft.setPrefWidth(115);
        sideOnRight.setPrefWidth(115);

        //indenting
        posFromLeft.setTranslateX(105);
        posFromRight.setTranslateX(105);
        BPV.setTranslateX(105);
        centrelineDist.setTranslateX(105);
        centrelineBox.setTranslateX(163);
        sideOnRight.setTranslateX(125);

    }

    private void setupStripValues() {
        //top row
        TORA1 = new Label("TORA");
        TODA1 = new Label("TODA");
        ASDA1 = new Label("ASDA");
        LDA1 = new Label("LDA");

        //first column
        strip1 = new Label("Strip: 08L");
        originalValues1 = new Label("Original:");
        recValues1 = new Label("Recalculated:");
        instructions1 = new Label("Instructions:");

        TORA2 = new Label("TORA");
        TODA2 = new Label("TODA");
        ASDA2 = new Label("ASDA");
        LDA2 = new Label("LDA");

        strip2 = new Label("Strip: 26R");
        originalValues2 = new Label("Original:");
        recValues2 = new Label("Recalculated:");
        instructions2 = new Label("Instructions:");
        //end first column

        oTora1 = new Label();
        oToda1 = new Label();
        oAsda1 = new Label();
        oLda1 = new Label();

        recTora1 = new Label();
        recToda1 = new Label();
        recAsda1 = new Label();
        recLda1 = new Label();

        land1 = new Label();
        takeOff1 = new Label();

        oTora2 = new Label();
        oToda2 = new Label();
        oAsda2 = new Label();
        oLda2 = new Label();

        recTora2 = new Label();
        recToda2 = new Label();
        recAsda2 = new Label();
        recLda2 = new Label();

        land2 = new Label();
        takeOff2 = new Label();

        //indenting TORA,TODA,ASDA,LDA
        TORA1.setTranslateX(75);
        TODA1.setTranslateX(120);
        ASDA1.setTranslateX(165);
        LDA1.setTranslateX(210);

        TORA2.setTranslateX(75);
        TODA2.setTranslateX(120);
        ASDA2.setTranslateX(165);
        LDA2.setTranslateX(210);


        //indenting original values and rec values
        oTora1.setTranslateX(75);
        oToda1.setTranslateX(120);
        oAsda1.setTranslateX(165);
        oLda1.setTranslateX(210);

        recTora1.setTranslateX(75);
        recToda1.setTranslateX(120);
        recAsda1.setTranslateX(165);
        recLda1.setTranslateX(210);

        oTora2.setTranslateX(75);
        oToda2.setTranslateX(120);
        oAsda2.setTranslateX(165);
        oLda2.setTranslateX(210);

        recTora2.setTranslateX(75);
        recToda2.setTranslateX(120);
        recAsda2.setTranslateX(165);
        recLda2.setTranslateX(210);


        //indenting instructions
        land1.setTranslateX(75);
        takeOff1.setTranslateX(150);
        land2.setTranslateX(75);
        takeOff2.setTranslateX(150);
    }

    private void updateObstacleComboBox()
    {
        List<Obstacle> obstacles = xmlHelper.readObstacles();
        this.obstaclesBox.getSelectionModel().clearSelection();
        this.obstaclesBox.getItems().clear();
        for(Obstacle o : obstacles){
            System.out.println(o);
            this.obstaclesBox.getItems().add(o);
        }
        this.obstaclesBox.getSelectionModel().selectFirst();
    }


    private void populateComponents(){
        Values origValues = runway.getStrip1().getOrigVal();
        this.oTora1.setText(String.valueOf(origValues.getTora()));
        this.oToda1.setText(String.valueOf(origValues.getToda()));
        this.oAsda1.setText(String.valueOf(origValues.getAsda()));
        this.oLda1.setText(String.valueOf(origValues.getLda()));

        origValues = runway.getStrip2().getOrigVal();
        this.oTora2.setText(String.valueOf(origValues.getTora()));
        this.oToda2.setText(String.valueOf(origValues.getToda()));
        this.oAsda2.setText(String.valueOf(origValues.getAsda()));
        this.oLda2.setText(String.valueOf(origValues.getLda()));

        this.strip1.setText(runway.getStrip1().getStripId());
        this.strip2.setText(runway.getStrip2().getStripId());

    }

    private void updateRecValues() {
        Values recValues = runway.getStrip1().getRecVal();
        this.recTora1.setText(String.valueOf(recValues.getTora()));
        this.recToda1.setText(String.valueOf(recValues.getToda()));
        this.recAsda1.setText(String.valueOf(recValues.getAsda()));
        this.recLda1.setText(String.valueOf(recValues.getLda()));
        this.land1.setText(recValues.getLanding());
        this.takeOff1.setText(recValues.getTakeoff());

        recValues = runway.getStrip2().getRecVal();
        this.recTora2.setText(String.valueOf(recValues.getTora()));
        this.recToda2.setText(String.valueOf(recValues.getToda()));
        this.recAsda2.setText(String.valueOf(recValues.getAsda()));
        this.recLda2.setText(String.valueOf(recValues.getLda()));
        this.land2.setText(recValues.getLanding());
        this.takeOff2.setText(recValues.getTakeoff());
    }

    private void errorMessage(Label message){
        popUp = new Stage();
        popUp.setTitle("Error message");
        VBox vbox = new VBox(20);
        message.setFont((Font.font("",FontWeight.BOLD, 16)));
        okBtn = new Button("Ok");

        okBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popUp.close();
            }
        });
        okBtn.setTranslateX(140);
        vbox.getChildren().add(message);
        vbox.getChildren().add(okBtn);
        popUp.setScene(new Scene(vbox));
        popUp.sizeToScene();
        popUp.show();
    }


    private void setListeners() {
        chgnRunway.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            public void handle(javafx.event.ActionEvent event) {
                new BeginFrameJavafx().start(stage);
            }
        });

/*
        //obstacle frame
        addCustObs.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                List<TextInputDialog>inputs = Arrays.asList(
                        new TextInputDialog("Height"),
                        new TextInputDialog("Width"),
                        new TextInputDialog("Length")
                );
            }
        });
*/


        calculate.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            public void handle(javafx.event.ActionEvent event) {
                Obstacle obs = (Obstacle)obstaclesBox.getSelectionModel().getSelectedItem();
                try {
                    int posFromRightTemp = Integer.parseInt(posFromRight.getText());
                    int posFromLeftTemp = Integer.parseInt(posFromLeft.getText());
                    int centrelineDistTemp = Integer.parseInt(centrelineDist.getText());
                    if (centrelineDistTemp < 0)
                    {
                        throw new PositiveOnlyException();
                    }
                    int blastAllowance = Integer.parseInt(BPV.getText());
                    runway.addObstacle(obs, posFromLeftTemp, posFromRightTemp, centrelineDistTemp,
                            centrelineBox.getSelectionModel().getSelectedItem().toString());
                    runway.recalculateValues(blastAllowance);
                    updateRecValues();

                    //viewPane.remove(threeD);
                    //threeD = new ThreeDVisuals();
                    //viewPane.add(threeD);
                    Platform.runLater(new Runnable() {
                        public void run() {
                            Platform.setImplicitExit(false);
                            threeD.init(runway);
                        }
                    });
                }catch (NumberFormatException e1){
                    if(!testable) {
                        Action response = Dialogs.create()
                                .title("Error message")
                                .message("One or more of the inputs are incorrect.")
                                .lightweight()
                                .showWarning();
                    }
                }
                catch (PositiveOnlyException e1) {
                    if(!testable) {
                        Action response = Dialogs.create()
                                .title("Error message")
                                .message("Distance from centreline must be greater than 0.")
                                .lightweight()
                                .showWarning();
                    }
                }
            }
        });


        //----3D view buttons----
        topDown.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            public void handle(javafx.event.ActionEvent event) {
                threeD.setCompassOrientation(false);
            }
        });

        sideOnLeft.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            public void handle(javafx.event.ActionEvent event) {
                threeD.setHorizontalRotation(ThreeDVisuals.PLAIN_ANGLE);
                threeD.setVerticalRotation(ThreeDVisuals.SIDE_VIEW_ANGLE);
                threeD.setZRotation(ThreeDVisuals.PLAIN_ANGLE);
            }
        });

        sideOnRight.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            public void handle(javafx.event.ActionEvent event) {
                threeD.setHorizontalRotation(ThreeDVisuals.PLAIN_ANGLE);
                threeD.setVerticalRotation(ThreeDVisuals.SIDE_SECOND_VIEW_ANGLE);
                threeD.setZRotation(ThreeDVisuals.PLAIN_ANGLE);
            }
        });

        compassOrient.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            public void handle(javafx.event.ActionEvent event) {
                threeD.setCompassOrientation(true);
            }
        });
    }
}

/*
package View;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class CalculusFrameJavafx extends Application {

    private Button chgnRunway, addCustObs, compassOrient, calculate;
    private Button topDown, sideOnLeft, sideOnRight;
    private ComboBox obstaclesBox, centrelineBox;
    private TextField posFromLeft, posFromRight, BPV, centrelineDist;
    private Label obstacleLabel,posFromLeftL, posFromRightL, BPVL, centrelineDistL;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        setup(primaryStage);

        BorderPane root = new BorderPane();                   //main pane

        GridPane gridpane = new GridPane();
        HBox obstHbox1 = new HBox();
        HBox centrelineHbox2 = new HBox();
        HBox viewHbox3 = new HBox();

        root.setLeft(gridpane);

        //gridpane setup
        gridpane.setPadding(new Insets(5, 5, 5, 5));
        gridpane.setHgap(5);
        gridpane.setVgap(7);

        gridpane.add(chgnRunway, 1, 1);

        gridpane.add(obstHbox1, 1, 2);
        obstHbox1.setSpacing(10);
        obstHbox1.getChildren().add(obstacleLabel);
        obstHbox1.getChildren().add(obstaclesBox);

        gridpane.add(addCustObs, 1, 3);
        gridpane.add(posFromLeft, 1, 15);
        gridpane.add(posFromRight, 1, 16);
        gridpane.add(BPV, 1, 17);

        gridpane.add(centrelineHbox2, 1, 18);
        centrelineHbox2.setSpacing(10);
        centrelineHbox2.getChildren().add(centrelineDist);
        centrelineHbox2.getChildren().add(centrelineBox);

        gridpane.add(calculate, 1, 19);
        gridpane.add(topDown, 1, 20);

        gridpane.add(viewHbox3, 1, 21);
        viewHbox3.setSpacing(10);
        viewHbox3.getChildren().add(sideOnLeft);
        viewHbox3.getChildren().add(sideOnRight);

        gridpane.add(compassOrient, 1,22);

        //gridpane.setOpacity();



        primaryStage.setScene(new Scene(root, 1500, 800));
        primaryStage.setResizable(true);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    private void setup(Stage primaryStage) {
        primaryStage.setTitle("PUT AIRPORT NAME HERE");

        chgnRunway = new Button("Change Runway");
        chgnRunway.setPrefWidth(200);

        //Obstacle Hbox components
        obstacleLabel = new Label("Obstacle: ");
        obstacleLabel.setFont(new Font("Cambria", 14));
        obstacleLabel.setTranslateY(7);
        obstaclesBox = new ComboBox();
        obstacleLabel.setPrefWidth(65);
        obstaclesBox.setPrefWidth(125);

        addCustObs = new Button("Add Custom Obstacle");
        posFromLeft = new TextField("Position from left");
        posFromRight = new TextField("Position from right");
        BPV = new TextField("Blast Allowance");
        addCustObs.setPrefWidth(200);
        posFromLeft.setPrefWidth(200);
        posFromRight.setPrefWidth(200);
        BPV.setPrefWidth(200);

        //Centreline Hbox components
        centrelineDist = new TextField("Centreline distance");
        centrelineBox = new ComboBox();
        centrelineDist.setPrefWidth(125);
        centrelineBox.setPrefWidth(65);


        calculate = new Button("Calculate");
        topDown = new Button("Top-Down View");
        compassOrient = new Button("Compass Orientation");
        calculate.setPrefWidth(200);
        topDown.setPrefWidth(200);
        compassOrient.setPrefWidth(200);

        //View Hbox components
        sideOnLeft = new Button("Side on Left");
        sideOnRight= new Button("Side on Right");
        sideOnLeft.setPrefWidth(95);
        sideOnRight.setPrefWidth(95);




    }
}

 */