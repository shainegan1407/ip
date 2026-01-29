package cherry;

import java.io.IOException;

import cherry.command.Command;
import cherry.task.TaskList;

/**
 * The main class of the Cherry application.
 * Initialises all components and runs the main program loop.
 */
public class Cherry {
    private final Storage storage;
    private final Ui ui;
    private final Parser parser;
    private TaskList tasks;

    /**
     * Creates a new Cherry application using the given storage filepath.
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
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        new Cherry("./data/cherry.txt").run();
    }
}
