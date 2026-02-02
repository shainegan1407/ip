package cherry;

import java.io.IOException;

import cherry.command.Command;
import cherry.task.TaskList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The main class of the Cherry application.
 * Initialises all components and runs the main program loop.
 */
public class Cherry extends Application {
    private final Storage storage;
    private final Ui ui;
    private final Parser parser;
    private TaskList tasks;
    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;
    private Scene scene;
    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/User.jpg"));
    private Image cherryImage = new Image(this.getClass().getResourceAsStream("/images/Cherry.jpg"));

    /**
     * No-argument constructor required by JavaFX.
     */
    public Cherry() {
        this("./data/cherry.txt");
    }
    /**
     * Creates a new Cherry application using the given storage filepath.
     *
     * @param filePath the path to the storage file
     */
    public Cherry(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        parser = new Parser();
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException e) {
            ui.printError("Error loading tasks");
            tasks = new TaskList();
        }
    }

    /**
     * Runs the main program loop.
     * Continuously reads user input, parses commands, executes them,
     * and handles exceptions until the exit command is given.
     */
    public void run() {
        ui.printWelcome();
        boolean isExit = false;

        while (!isExit) {
            try {
                String input = ui.readPrompt();
                Command command = parser.parse(input);
                command.execute(tasks, ui, storage);
                isExit = command.getExitStatus();
            } catch (CherryException e) {
                ui.printError(e.getMessage());
            } catch (IOException e) {
                ui.printError("Storage error! " + e.getMessage());
            }
        }
    }

    /**
     * Entry point of the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        new Cherry("./data/cherry.txt").run();
    }

    @Override
    public void start(Stage stage) {
        //Setting up required components

        scrollPane = new ScrollPane();
        dialogContainer = new VBox();
        scrollPane.setContent(dialogContainer);

        userInput = new TextField();
        sendButton = new Button("Send");

        DialogBox dialogBox = new DialogBox("Hello!", userImage);
        dialogContainer.getChildren().addAll(dialogBox);

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        scene = new Scene(mainLayout);

        stage.setScene(scene);
        stage.show();
        stage.setTitle("Duke");
        stage.setResizable(false);
        stage.setMinHeight(600.0);
        stage.setMinWidth(400.0);

        mainLayout.setPrefSize(400.0, 600.0);

        scrollPane.setPrefSize(385, 535);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        scrollPane.setVvalue(1.0);
        scrollPane.setFitToWidth(true);

        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);

        userInput.setPrefWidth(325.0);

        sendButton.setPrefWidth(55.0);

        AnchorPane.setTopAnchor(scrollPane, 1.0);

        AnchorPane.setBottomAnchor(sendButton, 1.0);
        AnchorPane.setRightAnchor(sendButton, 1.0);

        AnchorPane.setLeftAnchor(userInput, 1.0);
        AnchorPane.setBottomAnchor(userInput, 1.0);
    }
}
