import java.io.IOException;

public class UnmarkCommand extends Command{
    private final int taskIndex;

    public UnmarkCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws CherryException, IOException {
        Task task = tasks.getTask(taskIndex);
        tasks.unmarkTask(taskIndex);
        ui.printTaskUnmarked(task);
        storage.save(tasks.getTasks());
    }
}
