package cherry.command;

import java.io.IOException;

import cherry.CherryException;
import cherry.Storage;
import cherry.Ui;
import cherry.task.Task;
import cherry.task.TaskList;

/**
 * Represents a command which deletes a specified task to the task list.
 */
public class DeleteCommand extends Command {
    private final int taskIndex;
    /**
     * Creates an DeleteCommand with the specified task number.
     */
    public DeleteCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /**
     * Executes the delete command by removing the specified task from the list,
     * printing confirmation to the user and saving the new list.
     */
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws CherryException, IOException {
        Task task = tasks.getTask(taskIndex);
        tasks.deleteTask(taskIndex - 1);
        responseMessage = ui.formatTaskDeleted(task, tasks.getTaskCount());
        ui.printTaskDeleted(task, tasks.getTaskCount());
        storage.save(tasks.getTasks());
    }
}
