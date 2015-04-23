package View;

import Exceptions.FieldEmptyException;
import Exceptions.PositiveOnlyException;
import Model.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.dialog.Dialogs;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CalculusFrameJavafx extends Application {

    //frame size
    public static final double WIDTH =  Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final double HEIGHT =  Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 10;

    //model references
    private XMLHelper xmlHelper;
    private Runway runway;
    private ThreeDVisuals threeD;
    private NotifBoard fxNotif;

    private String airport;
    private boolean testable;
    private Stage stage;
    private BorderPane root;
    private GridPane gridpane;
    private HiddenSidesPane hsPane;

    private MenuBar menuBar;
    private Menu file, calculations, colorSchemeMenu, appMenu;

    //Top LHS components
    private Button chgnRunway, addCustObs, compassOrient, calculate;
    private Button topDown, sideOnLeft, sideOnRight;
    private ComboBox<Obstacle> obstaclesBox;
    private ComboBox<String> centrelineBox;
    private TextField posFromLeft, posFromRight, BPV, centrelineDist;
    private Label obstacleLabel, posFromLeftL, posFromRightL, BPVL, centrelineDistL;


    //Value components
    private Label TORA1, TODA1, ASDA1, LDA1;
    private Label TORA2, TODA2, ASDA2, LDA2;
    private Label strip1, strip2, originalValues1, originalValues2, recValues1, recValues2;
    private Label instructions1, instructions2, land1, land2, takeOff1, takeOff2;
    private Circle land1Color, land2Color, takeOff1Color, takeOff2Color;
    private Label oTora1, oToda1, oAsda1, oLda1;
    private Label recTora1, recToda1, recAsda1, recLda1;
    private Label oTora2, oToda2, oAsda2, oLda2;
    private Label recTora2, recToda2, recAsda2, recLda2;

    //ObstacleFrameJavafx
    private Stage obstacleStage;
    private TextField nameT, heightT, widthT, lengthT;

    private Label nameL, heightL, widthL, lengthL, descriptionL;
    private TextArea descriptionT;
    private Button add;
    private int colorScheme = 0;
    private double colorCircleRadius = 5;

    public CalculusFrameJavafx() {
        updateObstacleComboBox();
    }

    public CalculusFrameJavafx(Runway runway, String airport, NotifBoard fxNotif, Boolean testable) {
        this.runway = runway;
        this.airport = airport;
        this.testable = testable;
        this.fxNotif = fxNotif;
        xmlHelper = new XMLHelper();
        menuBar();
        setupLHScomponents();
        setupStripValues();
        updateObstacleComboBox();
        populateComponents();
        setListeners();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {

        this.stage = primaryStage;
        stage.setTitle("Runway Redeclaration (" + airport + ")");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });

        //main pane
        root = new BorderPane();
        gridpane = new GridPane();
        hsPane = new HiddenSidesPane();
        root.setLeft(gridpane);
        root.setTop(menuBar);
        hsPane.setContent(root);
        hsPane.setRight(fxNotif);
        hsPane.setTriggerDistance(200);




        //gridpane setup
        gridpane.setPadding(new Insets(5, 5, 5, 5));
        gridpane.setHgap(5);
        gridpane.setVgap(7);

        gridpane.add(chgnRunway, 1, 1);
        gridpane.add(obstaclesBox, 1, 3);
        gridpane.add(obstacleLabel, 1, 2);
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
        gridpane.add(land1Color, 1, 20);
        gridpane.add(takeOff1, 1, 20);
        gridpane.add(takeOff1Color, 1, 20);

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
        gridpane.add(land2Color, 1, 27);
        gridpane.add(takeOff2, 1, 27);
        gridpane.add(takeOff2Color, 1, 27);

        stage.setScene(new Scene(hsPane, WIDTH, HEIGHT));
        stage.setResizable(true);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.show();
    }

    private void menuBar() {
        //Export item
        MenuItem exportItem = new MenuItem("Export");
        exportItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Optional<String> response = Dialogs.create()
                        .title("Export Settings")
                        .message("Export file to:")
                        .showTextInput();

                String filename = "";
                if (response.isPresent())
                {
                    filename = response.get();
                    try
                    {
                        PrintHelper.print(runway, airport, filename);
                        fxNotif.addNotif(new Notif(Notif.PRINT_TITLE, Notif.PRINT_IMAGE));
                    }
                    catch (FileNotFoundException exp) {
                        Dialogs.create()
                                .title("Error message")
                                .message("Invalid file name.")
                                .lightweight()
                                .showWarning();
                    }
                    catch (IOException exc) {
                        Dialogs.create()
                                .title("Error message")
                                .message("Could not create file.\nTryAgain")
                                .lightweight()
                                .showWarning();
                    }
                }
            }
        });

        //Exit item
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Warning Exit");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to exit?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    System.exit(0);
                }if (result.get() == ButtonType.CANCEL){
                    alert.close();
                }
            }
        });

        //Calculations simple and complex
        ToggleGroup toggleGroup = new ToggleGroup();

        RadioMenuItem simpleCalcsItem = new RadioMenuItem("Simple Calculations \t(without width and length)");
        simpleCalcsItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                runway.setSimpleCalculations();
            }
        });

        RadioMenuItem setComplexCalculations = new RadioMenuItem("Complex Calculations \t(with width and length)");
        setComplexCalculations.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                runway.setComplexCalculations();
            }
        });
        simpleCalcsItem.setToggleGroup(toggleGroup);
        setComplexCalculations.setToggleGroup(toggleGroup);
        //selecting first toggle
        toggleGroup.selectToggle(simpleCalcsItem);


        //Creates a new Stage (window) to display the calculation breakdown
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        MenuItem viewBreakDown = new MenuItem("View breakdown");
        viewBreakDown.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage breakdownStage = new Stage();
                VBox breakdownVbox = new VBox(5);

                String strip1Breakdown = runway.getStrip1().viewCalculationBreakdown();
                String strip2Breakdown = runway.getStrip2().viewCalculationBreakdown();

                TextArea str1Calcs = new TextArea();
                str1Calcs.setEditable(false);
                ScrollPane strip1CalculationsPane = new ScrollPane(str1Calcs);

                TextArea str2Calcs = new TextArea();
                str2Calcs.setEditable(false);
                ScrollPane strip2CalculationsPane = new ScrollPane(str2Calcs);

                str1Calcs.setText(strip1Breakdown);
                str2Calcs.setText(strip2Breakdown);

                breakdownVbox.getChildren().addAll(strip1CalculationsPane, strip2CalculationsPane);

                breakdownStage.setTitle("Calculation Breakdown");
                breakdownStage.setScene(new Scene(breakdownVbox));
                breakdownStage.sizeToScene();
                breakdownStage.centerOnScreen();
                breakdownStage.setResizable(false);
                breakdownStage.show();
                fxNotif.addNotif(new Notif(Notif.BREAKDOWN_TITLE, Notif.BREAKDOWN_IMAGE, "for " + runway.getObstacle().getName()));
            }
        });

        MenuItem helpItem = new MenuItem("Help");
        helpItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    File helpPDF = new File("src/main/resources/help.pdf");
                    Desktop.getDesktop().open(helpPDF);
                } catch (IOException ex) {
                    Dialogs.create()
                            .title("Error message")
                            .masthead("No file found to open pdf")
                            .message("Please install a program that can view pdfs.")
                            .lightweight()
                            .showWarning();
                }
            }
        });

        final MenuItem fullscreenItem = new MenuItem("Toggle Fullscreen");
        fullscreenItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    stage.setFullScreen(!stage.isFullScreen());
                }catch(Exception e){};
            }
        });


        //Color scheme selection
        ToggleGroup toggleColorSchemeGroup = new ToggleGroup();

        RadioMenuItem color1 = new RadioMenuItem("Default Colors");
        color1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                colorScheme = threeD.NORMAL_COLORS;
                changeColorScheme();
                fxNotif.addNotif(new Notif(Notif.COLORSCHEME_TITLE, Notif.COLORSCHEME_IMAGE, "Normal Colors"));
                try {
                    threeD = new ThreeDVisuals(WIDTH - gridpane.getWidth(), gridpane.getHeight());
                    threeD.init(runway, threeD.NORMAL_COLORS);

                    root.setCenter(threeD);
                }catch(Exception e){};
            }
        });

        RadioMenuItem color2 = new RadioMenuItem("Protaniopia Colors");
        color2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                colorScheme = threeD.PROTANOPIA_COLORS;
                changeColorScheme();
                fxNotif.addNotif(new Notif(Notif.COLORSCHEME_TITLE, Notif.COLORSCHEME_IMAGE, "Protanopia"));
                try {
                    threeD = new ThreeDVisuals(WIDTH - gridpane.getWidth(), gridpane.getHeight());
                    threeD.init(runway, threeD.PROTANOPIA_COLORS);
                    root.setCenter(threeD);
                }catch (Exception e){};
            }
        });

        RadioMenuItem color3 = new RadioMenuItem("Deuteranipia Colors");
        color3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                colorScheme = threeD.DEUTERANOPIA_COLORS;
                changeColorScheme();
                fxNotif.addNotif(new Notif(Notif.COLORSCHEME_TITLE, Notif.COLORSCHEME_IMAGE, "Deuteranopia"));
                try {
                    threeD = new ThreeDVisuals(WIDTH - gridpane.getWidth(), gridpane.getHeight());
                    threeD.init(runway, threeD.DEUTERANOPIA_COLORS);
                    root.setCenter(threeD);
                }catch (Exception e){};
            }
        });

        RadioMenuItem color4 = new RadioMenuItem("Tritanopia Colors");
        color4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                colorScheme = threeD.TRITANOPIA_COLORS;
                changeColorScheme();
                fxNotif.addNotif(new Notif(Notif.COLORSCHEME_TITLE, Notif.COLORSCHEME_IMAGE, "Tritanopia"));
                try {
                    threeD = new ThreeDVisuals( WIDTH - gridpane.getWidth(), gridpane.getHeight());
                    threeD.init(runway, threeD.TRITANOPIA_COLORS);
                    root.setCenter(threeD);
                }catch (Exception e){};
            }
        });


        color1.setToggleGroup(toggleColorSchemeGroup);
        color2.setToggleGroup(toggleColorSchemeGroup);
        color3.setToggleGroup(toggleColorSchemeGroup);
        color4.setToggleGroup(toggleColorSchemeGroup);
        //selecting first toggle
        toggleColorSchemeGroup.selectToggle(color1);

        menuBar = new MenuBar();
        file = new Menu("File");
        appMenu = new Menu("Application");
        calculations = new Menu("Calculations");
        colorSchemeMenu = new Menu("Color Scheme");

        menuBar.getMenus().addAll(appMenu, file, calculations, colorSchemeMenu);
        appMenu.getItems().addAll(fullscreenItem, exitItem);
        file.getItems().addAll(exportItem, helpItem);
        calculations.getItems().addAll(simpleCalcsItem, setComplexCalculations, separatorMenuItem, viewBreakDown);
        colorSchemeMenu.getItems().addAll(color1,color2,color3,color4);
    }

    private void changeColorScheme(){
        land1Color.setFill(ThreeDVisuals.SLOPE2_COLOR[colorScheme]);
        land2Color.setFill(ThreeDVisuals.SLOPE4_COLOR[colorScheme]);
        takeOff1Color.setFill(ThreeDVisuals.SLOPE1_COLOR[colorScheme]);
        takeOff2Color.setFill(ThreeDVisuals.SLOPE3_COLOR[colorScheme]);
    }
    private void setupLHScomponents() {
        //initialising all the components
        chgnRunway = new Button("Change Runway");
        obstacleLabel = new Label("Obstacle: ");
        obstaclesBox = new ComboBox<Obstacle>();
        //obstaclesBox.setPromptText("Block: H:20, L:10, W:5");
        addCustObs = new Button("Add Custom Obstacle");

        posFromLeftL = new Label("Position from left:");
        posFromLeft = new TextField();
        posFromRightL = new Label("Position from right:");
        posFromRight = new TextField();
        BPVL = new Label("Blast Allowance:");
        BPV = new TextField();
        centrelineDistL = new Label("Centreline distance:");
        centrelineDist = new TextField();
        centrelineBox = new ComboBox<String>();
        centrelineBox.getItems().add("Above");
        centrelineBox.getItems().add("Below");
        centrelineBox.getSelectionModel().selectFirst();

        calculate = new Button("Calculate");
        topDown = new Button("Top-Down View");
        sideOnLeft = new Button("Side on Left");
        sideOnRight = new Button("Side on Right");
        compassOrient = new Button("Compass Orientation");


        //widths of all the components
        chgnRunway.setPrefWidth(260);
        obstaclesBox.setPrefWidth(260);
        addCustObs.setPrefWidth(260);
        posFromLeft.setMaxWidth(150);
        posFromRight.setMaxWidth(150);
        BPV.setMaxWidth(150);
        centrelineDist.setMaxWidth(51);
        centrelineBox.setPrefWidth(97);
        calculate.setPrefWidth(260);
        topDown.setPrefWidth(260);
        compassOrient.setPrefWidth(260);
        sideOnLeft.setPrefWidth(125);
        sideOnRight.setPrefWidth(130);

        //indenting
        obstacleLabel.setMaxWidth(260);
        posFromLeft.setTranslateX(110);
        posFromRight.setTranslateX(110);
        BPV.setTranslateX(110);
        centrelineDist.setTranslateX(110);
        centrelineBox.setTranslateX(163);
        sideOnRight.setTranslateX(130);
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
        land1Color = new Circle(colorCircleRadius, ThreeDVisuals.SLOPE2_COLOR[colorScheme]);
        takeOff1 = new Label();
        takeOff1Color = new Circle(colorCircleRadius, ThreeDVisuals.SLOPE1_COLOR[colorScheme]);

        oTora2 = new Label();
        oToda2 = new Label();
        oAsda2 = new Label();
        oLda2 = new Label();

        recTora2 = new Label();
        recToda2 = new Label();
        recAsda2 = new Label();
        recLda2 = new Label();

        land2 = new Label();
        land2Color = new Circle(colorCircleRadius, ThreeDVisuals.SLOPE4_COLOR[colorScheme]);
        takeOff2 = new Label();
        takeOff2Color = new Circle(colorCircleRadius, ThreeDVisuals.SLOPE3_COLOR[colorScheme]);


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
        land1Color.setTranslateX(150);
        takeOff1.setTranslateX(165);
        takeOff1Color.setTranslateX(255);
        land2.setTranslateX(75);
        land2Color.setTranslateX(150);
        takeOff2.setTranslateX(165);
        takeOff2Color.setTranslateX(255);

        land1Color.setStyle("-fx-effect: innershadow( gaussian, rgba( 0, 0, 0, 0.5 ), 1, 1, 1, 1.5 );");
        land2Color.setStyle("-fx-effect: innershadow( gaussian, rgba( 0, 0, 0, 0.5 ), 1, 1, 1, 1.5 );");
        takeOff1Color.setStyle("-fx-effect: innershadow( gaussian, rgba( 0, 0, 0, 0.5 ), 1, 1, 1, 1.5 );");
        takeOff2Color.setStyle("-fx-effect: innershadow( gaussian, rgba( 0, 0, 0, 0.5 ), 1, 1, 1, 1.5 );");
    }




    private void obstacleFrameJavafx() {
        obstacleStage = new Stage();
        obstacleStage.setTitle("Obstacle settings");

        GridPane obstacleGridpane = new GridPane();
        obstacleGridpane.setPadding(new javafx.geometry.Insets(5, 5, 5, 5));
        obstacleGridpane.setHgap(5);
        obstacleGridpane.setVgap(7);

        //labels
        nameL = new Label("Name:");
        heightL = new Label("Height:");
        widthL = new Label("Width:");
        lengthL = new Label("Length:");
        descriptionL = new Label("Description:");

        //components
        nameT = new TextField();
        heightT = new TextField();
        widthT = new TextField();
        lengthT = new TextField();
        descriptionT = new TextArea();
        add = new Button("Add Obstacle");

        obstacleGridpane.add(nameL, 1, 1);
        obstacleGridpane.add(nameT, 1, 1);
        obstacleGridpane.add(heightL, 1, 2);
        obstacleGridpane.add(heightT, 1, 2);
        obstacleGridpane.add(widthL, 1, 3);
        obstacleGridpane.add(widthT, 1, 3);
        obstacleGridpane.add(lengthL, 1, 4);
        obstacleGridpane.add(lengthT, 1, 4);
        obstacleGridpane.add(descriptionL, 1, 1);
        obstacleGridpane.add(descriptionT, 1, 1, 1, 4);
        obstacleGridpane.add(add, 1, 5);


        nameT.setMaxWidth(150);
        heightT.setMaxWidth(150);
        widthT.setMaxWidth(150);
        lengthT.setMaxWidth(150);
        descriptionT.setMaxWidth(150);
        descriptionT.setPrefHeight(90);
        add.setPrefWidth(90);

        nameT.setTranslateX(62);
        heightT.setTranslateX(62);
        widthT.setTranslateX(62);
        lengthT.setTranslateX(62);
        descriptionL.setTranslateX(240);
        descriptionT.setTranslateX(330);
        add.setTranslateX(62);
        add.setTranslateY(30);


        obstacleStage.setScene(new Scene(obstacleGridpane, 550, 200));
        obstacleStage.setResizable(true);
        obstacleStage.sizeToScene();
        obstacleStage.centerOnScreen();
        obstacleStage.setResizable(false);
        obstacleStage.show();
    }

    //listeners for the  Custom Obstacle stage
    private void obstacleFrameListeners() {
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String name = nameT.getText();

                    Integer height = Integer.parseInt(heightT.getText());
                    int width = Integer.parseInt(widthT.getText());
                    int length = Integer.parseInt(lengthT.getText());
                    String description = descriptionT.getText();

                    if (name.equals("")) {
                        throw new FieldEmptyException();
                    }
                    if ((width <= 0) || (height <= 0) || (length <= 0)) {
                        throw new PositiveOnlyException();
                    }
                    XMLHelper xmlHelper = new XMLHelper();
                    xmlHelper.addObstacleXML(new Obstacle(name, width, height, length, description));

                    //updates the new entry value to the combobox
                    updateObstacleComboBox();
                    obstaclesBox.getSelectionModel().selectLast();
                    fxNotif.addNotif(new Notif(Notif.NEWOBSTACLE_TITLE, Notif.NEWOBSTACLE_IMAGE, name));
                    obstacleStage.close();
                } catch (NumberFormatException e1) {
                    if (!testable) {
                        Dialogs.create()
                                .title("Error message")
                                .masthead("One or more of the inputed values are in the wrong format.")
                                .message("Height, width and length must be a number.")
                                .lightweight()
                                .showWarning();
                    }
                } catch (FieldEmptyException e1) {
                    if (!testable) {
                        Dialogs.create()
                                .title("Error message")
                                .message("\"Name field cannot be empty.")
                                .lightweight()
                                .showWarning();
                    }
                } catch (PositiveOnlyException e1) {
                    if (!testable) {
                        Dialogs.create()
                                .title("Error message")
                                .masthead("One or more of the inputed values are in the wrong format.")
                                .message("\"Height, width and length must be greater than 0.")
                                .lightweight()
                                .showWarning();
                    }
                }
            }
        });
    }

    private void updateObstacleComboBox() {
        List<Obstacle> obstacles = xmlHelper.readObstacles();
        this.obstaclesBox.getItems().clear();
        for (Obstacle o : obstacles) {
            this.obstaclesBox.getItems().add(o);
        }
        this.obstaclesBox.getSelectionModel().selectFirst();
        obstacleLabel.setText("Obstacle: " + this.obstaclesBox.getSelectionModel().getSelectedItem().getName()
                + "  H: " + this.obstaclesBox.getSelectionModel().getSelectedItem().getHeight() + "m"
                + "  L:" + this.obstaclesBox.getSelectionModel().getSelectedItem().getLength() + "m"
                + "  W:" + this.obstaclesBox.getSelectionModel().getSelectedItem().getWidth() + "m");

    }


    private void populateComponents() {
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


    private void setListeners() {
        if(fxNotif == null)
            System.out.println("bolock");
        fxNotif.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (hsPane.getPinnedSide() != null) {
                    hsPane.setPinnedSide(null);
                } else hsPane.setPinnedSide(Side.RIGHT);
            }
        });

        chgnRunway.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            public void handle(javafx.event.ActionEvent event) {
                new BeginFrameJavafx(fxNotif).start(stage);
            }
        });

        this.obstaclesBox.valueProperty().addListener(new ChangeListener<Obstacle>() {
            @Override
            public void changed(ObservableValue<? extends Obstacle> observable, Obstacle oldValue, Obstacle newValue) {
                if (!obstaclesBox.getItems().isEmpty()) {
                    obstacleLabel.setText("Obstacle: " + obstaclesBox.getSelectionModel().getSelectedItem().getName()
                            + "  H: " + obstaclesBox.getSelectionModel().getSelectedItem().getHeight() + "m"
                            + "  L:" + obstaclesBox.getSelectionModel().getSelectedItem().getLength() + "m"
                            + "  W:" + obstaclesBox.getSelectionModel().getSelectedItem().getWidth() + "m");
                    gridpane.getChildren().remove(obstacleLabel);
                    gridpane.add(obstacleLabel, 1, 2);
//                    fxNotif.addNotif(new Notif(Notif.OBSTACLE_TITLE, Notif.OBSTACLE_IMAGE,obstaclesBox.getSelectionModel().getSelectedItem().getName() ));
                }
            }
        });

        addCustObs.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                obstacleFrameJavafx();
                obstacleFrameListeners();
            }
        });

        calculate.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            public void handle(javafx.event.ActionEvent event) {
                Obstacle obs = (Obstacle) obstaclesBox.getSelectionModel().getSelectedItem();
                try {
                    int posFromRightTemp = Integer.parseInt(posFromRight.getText());
                    int posFromLeftTemp = Integer.parseInt(posFromLeft.getText());
                    int centrelineDistTemp = Integer.parseInt(centrelineDist.getText());
                    int blastAllowance = Integer.parseInt(BPV.getText());
                    if ((centrelineDistTemp < 0) || (blastAllowance < 0)) {
                        throw new PositiveOnlyException();
                    }
                    runway.addObstacle(obs, posFromLeftTemp, posFromRightTemp, centrelineDistTemp,
                            centrelineBox.getSelectionModel().getSelectedItem());
                    runway.recalculateValues(blastAllowance);
                    updateRecValues();

                    //viewPane.remove(threeD);
                    threeD = new ThreeDVisuals(WIDTH - gridpane.getWidth(), gridpane.getHeight());
                    threeD.init(runway, colorScheme);
                    root.setCenter(threeD);
                    fxNotif.addNotif(new Notif(Notif.SYNC_TITLE, Notif.SYNC_IMAGE));
                    fxNotif.addNotif(new Notif(Notif.SYNC_TITLE, Notif.SYNC_IMAGE));
                } catch (NumberFormatException e1) {
                    if (!testable) {
                        Dialogs.create()
                                .title("Error message")
                                .masthead("One or more of the inputed values are in the wrong format.")
                                .message("- Position from Left and Right should be integers." +
                                        "\n- Centreline distance and Blast protection value" +
                                        "\n  should be positive integers.")
                                .lightweight()
                                .showWarning();
                    }
                } catch (PositiveOnlyException e1) {
                    if (!testable) {
                        Dialogs.create()
                                .title("Error message")
                                .masthead("Error with Centreline distance/Blast protection allowance.")
                                .message("- Both values should be positive integers.")
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