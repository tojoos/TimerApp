package timerApp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage timerStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("timerStage.fxml")));
        Parent root = fxmlLoader.load();
        root.setOnMousePressed(e -> root.requestFocus());
        timerStage.setOnCloseRequest(e -> {
            timerStage.hide();
            System.exit(0);
        });
        timerStage.setTitle("Timer");
        timerStage.setResizable(false);
        timerStage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("graphics\\timer_app_icon.png"))));
        timerStage.setScene(new Scene(root));
        timerStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
