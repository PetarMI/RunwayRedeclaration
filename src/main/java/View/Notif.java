package View;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Notif extends HBox {


    public static final String SYNC_IMAGE = "file:src/main/resources/assets/sync_icon.png";
    public static final String PRINT_IMAGE = "file:src/main/resources/assets/print_icon.png";
    public static final String RUNWAY_IMAGE = "file:src/main/resources/assets/runway_icon.png";
    public static final String SYNC_TITLE = "Recalculated Values";
    public static final String PRINT_TITLE = "Printed Results";
    public static final String BREAKDOWN_TITLE = "Displayed Breakdown\nof Calculations";
    public static final String RUNWAY_TITLE ="Changed Runway";


    //when calling this constructor, please provide the title and imageUrl only by using
    //the final static variables above;
    //descriptions are given by you, and are not a requirement

    public Notif(String title, String imageUrl){
        this(title, imageUrl, "");
    }

    public Notif(String title, String imageUrl,String description){

        ImageView imageView = new ImageView(new Image(imageUrl));
        Label titleLbl = new Label(title);
        Label descrLbl = new Label(description);
        VBox textBox = new VBox(titleLbl, descrLbl);
        this.setCenterShape(true);
        this.setPadding(new Insets(5,5,5,5));


        this.setStyle("-fx-background-color:rgba(255,255,255, 1);" +
                "-fx-border-width: 20px;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);"
                );
        titleLbl.setStyle( "-fx-font-size: 14;" +
                "-fx-font-weight: bold;");
        descrLbl.setStyle("-fx-font-color: rgba(240,240,240,1);" +
                "-fx-font-size: 12;" +
                "-fx-font-weight: lighter;");

        this.getChildren().addAll(imageView,textBox);
    }
}
