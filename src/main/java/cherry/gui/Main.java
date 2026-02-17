package cherry.gui;

import java.io.IOException;

import cherry.Cherry;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Cherry using FXML with cafe theme.
 */
public class Main extends Application {

    private final Cherry cherry = new Cherry();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            Image appIcon = new Image(getClass().getResourceAsStream("/images/Cherry.png"));

            // Set minimum and preferred window sizes
            stage.setMinHeight(450);
            stage.setMinWidth(400);
            stage.setHeight(600);
            stage.setWidth(400);

            stage.setScene(scene);
            stage.getIcons().add(appIcon);
            stage.setTitle("Cherry Café ☕ - Task Manager");

            MainWindow controller = fxmlLoader.getController();
            controller.setCherry(cherry);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
