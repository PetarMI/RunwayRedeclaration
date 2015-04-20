package View;

import javafx.animation.FadeTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class NotifBoard extends ScrollPane {

    VBox content;


    public NotifBoard(){
        content = new VBox();

        Label infoLabel = new Label("Click to pin/unpin");
        //styling
        this.getStylesheets().add(this.getClass().getResource("/scrollbar.css").toExternalForm());
        content.setPadding(new Insets(5, 5, 5, 5));
        content.setFillWidth(true);
        content.setSpacing(5);
        content.setStyle("-fx-background-color:rgba(0,0,0, 0.5);");
        this.setFitToHeight(true);
        this.setFitToWidth(true);
        //make the scrollbar always focus on the end of the list
        DoubleProperty wProperty = new SimpleDoubleProperty();
        wProperty.bind(content.heightProperty());
        wProperty.addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {

                NotifBoard.this.setVvalue(NotifBoard.this.getVmax());
            }
        }) ;

        infoLabel.setStyle("-fx-font-style: italic;" +
                "-fx-font-weight: bolder;");

        content.getChildren().addAll(infoLabel, new Notif(Notif.PRINT_TITLE, Notif.PRINT_IMAGE),
                new Notif(Notif.RUNWAY_TITLE, Notif.RUNWAY_IMAGE, "to Heathrow 09L/27R"));
        this.setContent(content);
    }

    public void addNotif(Notif notif){
        FadeTransition ft = new FadeTransition(Duration.millis(800), notif);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
        content.getChildren().add(notif);
    }
}
