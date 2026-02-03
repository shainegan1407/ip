package cherry.command;

import java.io.IOException;

import cherry.CherryException;
import cherry.Storage;
import cherry.Ui;
import cherry.task.Task;
import cherry.task.TaskList;

/**
 * Represents a command which marks a specified task as done.
 */
public class MarkCommand extends Command {
    private final int taskIndex;

    /**
     * Creates a MarkCommand with the specified task number.
     */
    public MarkCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /**
     * Executes the mark command by marking the specified task as done,
     * printing confirmation to the user and saving the new list.
     */
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws CherryException, IOException {
        Task task = tasks.getTask(taskIndex);
        tasks.markTask(taskIndex);
        responseMessage = ui.formatTaskMarked(task);
        ui.printTaskMarked(task);
        storage.save(tasks.getTasks());
    }
}
