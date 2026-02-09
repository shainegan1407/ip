package cherry.command;

import cherry.exception.CherryException;
import cherry.storage.Storage;
import cherry.task.TaskList;
import cherry.ui.Ui;

/**
 * Represents a command which prints all tasks in the list to the user.
 */
public class ListCommand extends Command {
    /**
     * Executes the list command by printing all tasks in the list to the user.
     */
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws CherryException {
        responseMessage = ui.formatList(tasks);
        ui.printList(tasks);
    }
}

