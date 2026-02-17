package cherry.gui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a TextFlow containing formatted text from the speaker.
 */
public class DialogBox extends HBox {
    @FXML
    private TextFlow dialog;
    @FXML
    private ImageView displayPicture;

    private final String rawText;
    private String messageType;

    /**
     * Displays the message given by string alongside the given image as an icon.
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

        this.rawText = text;
        this.messageType = null;

        renderText();
        displayPicture.setImage(img);
        applyImageShadow();
    }

    /**
     * Parses rawText and rebuilds the TextFlow children with correct style classes.
     * Segments wrapped in **double asterisks** are rendered bold.
     * Message type color classes are applied at the same time as text creation,
     * ensuring styles are never set on stale nodes.
     */
    private void renderText() {
        dialog.getChildren().clear();

        String[] parts = rawText.split("\\*\\*", -1);

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].isEmpty()) {
                continue;
            }

            Text segment = new Text(parts[i]);
            segment.getStyleClass().add("dialog-text");

            // Odd-indexed parts sit between ** markers — render bold
            if (i % 2 == 1) {
                segment.getStyleClass().add("dialog-text-bold");
            }

            // Apply message type color class at creation time
            if (messageType != null) {
                switch (messageType) {
                case "error":
                    segment.getStyleClass().add("error-text");
                    break;
                case "success":
                    segment.getStyleClass().add("success-text");
                    break;
                case "warning":
                    segment.getStyleClass().add("warning-text");
                    break;
                default:
                    break;
                }
            }

            dialog.getChildren().add(segment);
        }
    }

    /**
     * Sets the message type, updates the TextFlow bubble style,
     * and re-renders text nodes so color classes are applied correctly.
     */
    private void applyMessageStyle(String messageType) {
        this.messageType = messageType;

        // Update TextFlow bubble background/border
        dialog.getStyleClass().removeAll("error-flow", "success-flow", "warning-flow");

        if (messageType != null) {
            switch (messageType.toLowerCase()) {
            case "error":
                dialog.getStyleClass().add("error-flow");
                break;
            case "success":
                dialog.getStyleClass().add("success-flow");
                break;
            case "warning":
                dialog.getStyleClass().add("warning-flow");
                break;
            default:
                break;
            }
        }

        // Re-render so Text nodes get the correct color style class
        renderText();
    }

    /**
     * Applies drop shadow effect to the dialog box.
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
        dialog.getStyleClass().add("reply-flow");
    }

    /**
     * Creates a dialog box on the left side of the screen for Cherry with message type styling.
     */
    public static DialogBox getCherryDialog(String text, Image img, String messageType) {
        var db = new DialogBox(text, img);
        db.flip();
        db.applyMessageStyle(messageType);
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
