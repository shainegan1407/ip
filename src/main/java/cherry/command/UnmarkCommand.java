package cherry.command;

import java.io.IOException;

import cherry.CherryException;
import cherry.Storage;
import cherry.Ui;
import cherry.task.Task;
import cherry.task.TaskList;

/**
 * Represents a command which unmarks a specified task as done.
 */
public class UnmarkCommand extends Command {
    private final int taskIndex;

    /**
     * Creates a UnmarkCommand with the specified task number.
     */
    public UnmarkCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }
    /**
     * Executes the unmark command by unmarking the specified task as done,
     * printing confirmation to the user and saving the new list.
     */
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws CherryException, IOException {
        Task task = tasks.getTask(taskIndex);
        tasks.unmarkTask(taskIndex);
        ui.printTaskUnmarked(task);
        storage.save(tasks.getTasks());
    }
}
