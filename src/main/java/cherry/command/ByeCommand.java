package cherry.command;

import cherry.storage.Storage;
import cherry.task.TaskList;
import cherry.ui.Ui;

/**
 * Represents a command to exit the application.
 */
public class ByeCommand extends Command {
    /**
     * Executes the bye command by printing a goodbye message.
     * and setting the exit status to true.
     */
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.printGoodbye();
        responseMessage = ui.formatGoodbye();
        isExit = true;
    }
}

