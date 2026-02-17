package cherry;

import java.io.IOException;

import cherry.command.Command;
import cherry.exception.CherryException;
import cherry.parser.Parser;
import cherry.storage.Storage;
import cherry.task.TaskList;
import cherry.ui.Ui;


/**
 * Represents the main class of the Cherry application.
 * Initializes all components and runs the main program loop.
 */
public class Cherry {
    private final Storage storage;
    private final Ui ui;
    private final Parser parser;
    private TaskList tasks;
    private String commandType;

    /**
     * Creates a new Cherry instance with no-argument, as required by JavaFX.
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
     * Returns the welcome message for opening the GUI.
     */
    public String getWelcomeMessage() {
        return ui.formatWelcome();
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        try {
            Command c = parser.parse(input);
            c.execute(tasks, ui, storage);
            commandType = c.getClass().getSimpleName();
            return c.toString();
        } catch (CherryException e) {
            return "**Error: **" + e.getMessage();
        } catch (IOException e) {
            return "**Storage error! **" + e.getMessage();
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
                ui.printError("**Storage error! **" + e.getMessage());
            }
        }
    }

    /**
     * Acts as the entry point of the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        new Cherry("./data/cherry.txt").run();
    }

}
