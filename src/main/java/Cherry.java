import java.io.IOException;

public class Cherry {
    private final Storage storage;
    private final Ui ui;
    private final Parser parser;
    private TaskList tasks;


    public Cherry(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        parser = new Parser();
        tasks = new TaskList(storage.load());
    }

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

    public static void main(String[] args) {
        new Cherry("./data/cherry.txt").run();
    }
}
