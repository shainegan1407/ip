package cherry.gui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a label containing text from the speaker.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    /**
     * Displays the message give by string alongside the given image as an icon.
     */
    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        dialog.getStyleClass().add("label");
        displayPicture.setImage(img);
        clipImageToCircle();
        applyImageShadow();
    }
    /**
     * Clips the display picture to a circular shape.
     */
    private void clipImageToCircle() {
        // Get the center point (assuming square image)
        double radius = displayPicture.getFitWidth() / 2;
        Circle clip = new Circle(radius, radius, radius);
        displayPicture.setClip(clip);
    }

    /**
     * Applies drop shadow effect to the image container.
     */
    private void applyImageShadow() {
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(99, 99, 99, 0.2));
        shadow.setRadius(8);
        shadow.setOffsetX(0);
        shadow.setOffsetY(2);
        this.setEffect(shadow);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }

    /**
     * Creates a dialog box on the left side of the screen for Cherry.
     */
    public static DialogBox getCherryDialog(String text, Image img, String commandType) {
        var db = new DialogBox(text, img);
        db.flip();
        return db;
    }
    /**
     * Creates a dialog box on the left side of the screen for Cherry.
     */
    public static DialogBox getCherryDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        return db;
    }
    /**
     * Creates a dialog box on the right side of the screen for the user.
     */
    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img);
    }
}
