import java.io.IOException;

public class DeleteCommand extends Command {
    private final int taskIndex;

    public DeleteCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws CherryException, IOException {
        Task task = tasks.getTask(taskIndex);
        tasks.deleteTask(taskIndex -1);
        ui.printTaskDeleted(task, tasks.getTaskCount());
        storage.save(tasks.getTasks());
    }
}
