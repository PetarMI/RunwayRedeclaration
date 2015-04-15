package View;

import javafx.animation.FadeTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.controlsfx.control.HiddenSidesPane;

public class NotifBoard extends JFXPanel {

    VBox content;

    public NotifBoard(){
        content = new VBox();
        final ScrollPane scrollPane = new ScrollPane(content);
        Label infoLabel = new Label("Click to pin/unpin");
        final HiddenSidesPane hsPane = new HiddenSidesPane();
        Scene scene = new Scene(hsPane);
        //styling
        scene.getStylesheets().add(this.getClass().getResource("/scrollbar.css").toExternalForm());
        content.setPadding(new Insets(5, 5, 5, 5));
        content.setFillWidth(true);
        content.setSpacing(5);
        content.setStyle("-fx-background-color:rgba(0,0,0, 0.5);");
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        //make the scrollbar always focus on the end of the list
        DoubleProperty wProperty = new SimpleDoubleProperty();
        wProperty.bind(content.heightProperty());
        wProperty.addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {

                scrollPane.setVvalue(scrollPane.getVmax());
            }
        }) ;

        infoLabel.setStyle("-fx-font-style: italic;" +
                "-fx-font-weight: bolder;");

        content.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(hsPane.getPinnedSide() != null) {
                    hsPane.setPinnedSide(null);
                }
                else hsPane.setPinnedSide(Side.RIGHT);
            }
        });

        hsPane.setContent(new Label("Drag the mouse to the right for notifications ->"));
        hsPane.setRight(scrollPane);
        content.getChildren().addAll(infoLabel, new Notif(Notif.PRINT_TITLE, Notif.PRINT_IMAGE),
                new Notif(Notif.RUNWAY_TITLE, Notif.RUNWAY_IMAGE, "to Heathrow 09L/27R"));
        this.setScene(scene);
    }

    public void addNotif(Notif notif){
        FadeTransition ft = new FadeTransition(Duration.millis(800), notif);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
        content.getChildren().add(notif);
    }
}
