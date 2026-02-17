package cherry.gui;

import cherry.Cherry;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controls the main GUI with cafe theme.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Cherry cherry;
    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/User.png"));
    private Image cherryImage = new Image(this.getClass().getResourceAsStream("/images/Cherry.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        userInput.setPromptText("Type your order here... (e.g., 'todo buy coffee beans')");
    }

    /**
     * Determines the message type based on response content.
     */
    private String determineMessageType(String response) {
        if (response.contains("✗")
                || response.contains("Oops!")
                || response.contains("Error")
                || response.contains("Invalid")
                || response.contains("doesn't look like")
                || response.contains("not a valid")
                || response.contains("don't see")
                || response.contains("don't recognise")
                || response.contains("Something went wrong")
                || response.contains("missing")
                || response.contains("forgot to add")
                || response.contains("No matching orders found")
                || response.contains("couldn't understand")
                || response.contains("needs details")
                || response.contains("needs a description")
                || response.contains("can't use")) {
            return "error";
        }

        if (response.startsWith("✓")
                || response.contains("Order placed!")
                || response.contains("Order complete!")
                || response.contains("Order cancelled:")
                || response.contains("Order updated!")
                || response.contains("Great work!")) {
            return "success";
        }

        if (response.contains("Are you sure")
                || response.contains("very far")
                || response.contains("already passed")) {
            return "warning";
        }

        return null;
    }

    /**
     * Injects the Cherry instance.
     */
    public void setCherry(Cherry c) {
        cherry = c;
        dialogContainer.getChildren().add(
                DialogBox.getCherryDialog(cherry.getWelcomeMessage(), cherryImage));
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Cherry's reply
     * and then appends them to the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.trim().isEmpty()) {
            return;
        }

        String response = cherry.getResponse(input);
        String messageType = determineMessageType(response);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getCherryDialog(response, cherryImage, messageType)
        );
        userInput.clear();
    }
}
