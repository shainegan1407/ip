package cherry.command;

import java.io.IOException;

import cherry.Storage;
import cherry.Ui;
import cherry.task.Task;
import cherry.task.TaskList;


public class AddCommand extends Command {
    private final Task task;

    public AddCommand(Task task) {
        this.task = task;
    }

    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.addTask(task);
        ui.printTaskAdded(task, tasks.getTaskCount());
        try {
            storage.save(tasks.getTasks());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
