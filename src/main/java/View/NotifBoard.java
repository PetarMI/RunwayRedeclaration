package View;

import javafx.animation.FadeTransition;
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
        ScrollPane scrollPane = new ScrollPane(content);
        Label infoLabel = new Label("Click to pin/unpin");
        final HiddenSidesPane hsPane = new HiddenSidesPane();
        Scene scene = new Scene(hsPane);

        //styling
        /*TODO: Scrollpane makes the background opaque no matter what because Java
        should find workaround for this annoying error*/
//        scene.getStylesheets().add("src/main/resources/sheets/scrollbar_sheet.css");
        content.setPadding(new Insets(5,5,5,5));
        content.setFillWidth(true);
        content.setSpacing(5);
        content.setStyle("-fx-background-color:rgba(0,0,0, 0.5);");
        scrollPane.setFitToHeight(true);
        scrollPane.setMinWidth(100);
//        scrollPane.getStyleClass().add("scroll-pane");
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
        hsPane.setRight(content);
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
