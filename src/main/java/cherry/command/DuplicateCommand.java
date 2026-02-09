package cherry.command;

import java.io.IOException;

import cherry.exception.CherryException;
import cherry.storage.Storage;
import cherry.task.Task;
import cherry.task.TaskList;
import cherry.ui.Ui;


/**
 * Represents a command which duplicates the specified task to the task list.
 */
public class DuplicateCommand extends Command {
    private final int taskIndex;

    /**
     * Creates an DuplicateCommand with the specified task index.
     */
    public DuplicateCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /**
     * Executes the add command by adding the task to the task list,
     * printing confirmation to the user, and saving the updated list.
     */
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws CherryException, IOException {
        assert tasks != null : "TaskList should not be null";
        assert ui != null : "Ui should not be null";
        assert storage != null : "Storage should not be null";
        Task task = tasks.getTask(taskIndex);
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
