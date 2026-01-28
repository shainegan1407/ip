import java.io.IOException;

public class MarkCommand extends Command{
    private final int taskIndex;

    public MarkCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws CherryException, IOException {
        Task task = tasks.getTask(taskIndex);
        tasks.markTask(taskIndex);
        ui.printTaskMarked(task);
        storage.save(tasks.getTasks());
    }
}
