package cherry.command;

import java.io.IOException;
import java.util.Map;

import cherry.exception.CherryException;
import cherry.storage.Storage;
import cherry.task.Task;
import cherry.task.TaskList;
import cherry.ui.Ui;

/**
 * Represents a command which updates the fields of an existing task, as specified by the user.
 */
public class UpdateCommand extends Command {
    private final int taskIndex;
    private final Map<String, String> fields;
    /**
     * Creates a UpdateCommand with the specified task number and fields.
     */
    public UpdateCommand(int taskIndex, Map<String, String> fields) {
        this.taskIndex = taskIndex;
        this.fields = fields;
    }

    /**
     * Executes the update command by applying the new field values to the specified task,
     * printing confirmation to the user and saving the new list.
     */
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws CherryException, IOException {
        Task task = tasks.getTask(taskIndex);
        task.update(fields);
        responseMessage = ui.formatTaskUpdated(task);
        ui.printTaskUpdated(task);
        storage.save(tasks.getTasks());
    }
}
