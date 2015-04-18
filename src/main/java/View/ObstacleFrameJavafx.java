package View;

import Exceptions.FieldEmptyException;
import Exceptions.PositiveOnlyException;
import Model.Obstacle;
import Model.XMLHelper;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import org.controlsfx.dialog.Dialogs;

public class ObstacleFrameJavafx {

    //frame size
    private static final int WIDTH = 550;
    private static final int HEIGHT = 200;
    private Stage obstacleStage;
    private TextField nameT, heightT, widthT, lengthT;

    private Label nameL,heightL,widthL,lengthL,descriptionL;
    private TextArea descriptionT;
    private Button add;
    private boolean testable;

    public ObstacleFrameJavafx(Boolean testable)
    {
        this.testable = testable;
        this.init();
        this.setListeners();
    }

    public void init()
    {
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

        obstacleGridpane.add(nameL,1,1);
        obstacleGridpane.add(nameT,1,1);
        obstacleGridpane.add(heightL,1,2);
        obstacleGridpane.add(heightT,1,2);
        obstacleGridpane.add(widthL,1,3);
        obstacleGridpane.add(widthT,1,3);
        obstacleGridpane.add(lengthL,1,4);
        obstacleGridpane.add(lengthT,1,4);
        obstacleGridpane.add(descriptionL,1,1);
        obstacleGridpane.add(descriptionT,1,1,1,4);
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


        obstacleStage.setScene(new Scene(obstacleGridpane, WIDTH, HEIGHT));
        obstacleStage.setResizable(true);
        obstacleStage.sizeToScene();
        obstacleStage.centerOnScreen();
        obstacleStage.setResizable(false);
        obstacleStage.show();
    }

    private void setListeners() {
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String name = nameT.getText();

                    Integer height = Integer.parseInt(heightT.getText());
                    int width = Integer.parseInt(widthT.getText());
                    int length = Integer.parseInt(lengthT.getText());
                    String description = descriptionT.getText();

                    if(name.equals("")){
                        throw new FieldEmptyException();
                    }
                    if((width <= 0) || (height <= 0) || (length <= 0)){
                        throw new PositiveOnlyException();
                    }
                    XMLHelper xmlHelper = new XMLHelper();
                    xmlHelper.addObstacleXML(new Obstacle(name, width, height , length, description));
                    obstacleStage.close();
                }catch (NumberFormatException e1){
                    if(!testable) {
                        Dialogs.create()
                                .title("Error message")
                                .masthead("One or more of the inputed values are in the wrong format.")
                                .message("Height, width and length must be a number.")
                                .lightweight()
                                .showWarning();
                        //JOptionPane.showMessageDialog(ObstacleFrame.this, "Height, width and length must be a number.");
                    }
                } catch (FieldEmptyException e1) {
                    if(!testable) {
                        Dialogs.create()
                                .title("Error message")
                                .message("\"Name field cannot be empty.")
                                .lightweight()
                                .showWarning();
                        //JOptionPane.showMessageDialog(ObstacleFrame.this, "Name field cannot be empty.");
                    }
                } catch (PositiveOnlyException e1) {
                    if(!testable) {
                        Dialogs.create()
                                .title("Error message")
                                .masthead("One or more of the inputed values are in the wrong format.")
                                .message("\"Height, width and length must be greater than 0.")
                                .lightweight()
                                .showWarning();
                        //JOptionPane.showMessageDialog(ObstacleFrame.this, "Height, width and length must be greater than 0.");
                    }
                }
            }
        });
    }
}
