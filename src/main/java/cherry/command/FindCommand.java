package cherry.command;

import cherry.CherryException;
import cherry.Storage;
import cherry.Ui;
import cherry.task.Task;
import cherry.task.TaskList;

public class FindCommand extends Command {
    private final String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws CherryException {
        TaskList newList = new TaskList();
        for (int i = 0; i < tasks.getTaskCount(); i += 1) {
            Task task = tasks.getTask(i + 1);
            if (task.hasKeyword(this.keyword)) {
                newList.addTask(task);
            }
        }
        ui.printMatchingList(newList);
    }
}
