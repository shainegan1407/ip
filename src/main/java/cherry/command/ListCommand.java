package cherry.command;

import cherry.CherryException;
import cherry.Storage;
import cherry.Ui;
import cherry.task.TaskList;

/**
 * Represents a command which prints all tasks in the list to the user.
 */
public class ListCommand extends Command {
    /**
     * Executes the list command by printing all tasks in the list to the user.
     */
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws CherryException {
        ui.printList(tasks);
    }
}

