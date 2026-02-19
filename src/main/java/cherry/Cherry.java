package cherry;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
     * Constructs a Cherry instance.
     * Determines the jar location and sets storage path relative to it.
     */
    public Cherry() {
        ui = new Ui();
        parser = new Parser();
        Storage tempStorage;
        try {
            String dataPath = getDataPath();
            tempStorage = new Storage(dataPath);
            tasks = new TaskList(tempStorage.load());
        } catch (IOException e) {
            ui.printError("Init error: " + e.getMessage());
            tempStorage = new Storage(getDataPath());
            tasks = new TaskList();
        }
        storage = tempStorage;
    }

    /**
     * Determines the correct data file path relative to the jar location.
     * If running from a jar, places data/ next to the jar.
     * If running from IDE, uses ./data/ in the project root.
     */
    private String getDataPath() {
        try {
            // Get the location of the running jar or class files
            File jarFile = new File(Cherry.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI());

            Path dataDir;
            if (jarFile.isFile()) {
                // Running from jar - place data folder next to jar
                dataDir = jarFile.getParentFile().toPath().resolve("data");
            } else {
                // Running from IDE - use project root
                dataDir = Paths.get("data");
            }

            return dataDir.resolve("cherry.txt").toString();
        } catch (URISyntaxException e) {
            // Fallback to current directory if jar location can't be determined
            return "./data/cherry.txt";
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
        new Cherry().run();
    }

}
