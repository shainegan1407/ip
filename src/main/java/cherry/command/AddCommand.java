package cherry.command;

import java.io.IOException;

import cherry.storage.Storage;
import cherry.task.Task;
import cherry.task.TaskList;
import cherry.ui.Ui;

/**
 * Represents a command which adds a new task to the task list.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates an AddCommand with the specified task.
     */
    public AddCommand(Task task) {
        assert task != null : "Task to add should not be null";
        this.task = task;
    }

    /**
     * Executes the add command by adding the task to the task list,
     * printing confirmation to the user, and saving the updated list.
     */
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        assert tasks != null : "TaskList should not be null";
        assert ui != null : "Ui should not be null";
        assert storage != null : "Storage should not be null";
        tasks.addTask(task);
        responseMessage = ui.formatTaskAdded(task, tasks.getTaskCount());
        ui.printTaskAdded(task, tasks.getTaskCount());
        try {
            storage.save(tasks.getTasks());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
