package cherry.command;

import cherry.CherryException;
import cherry.Storage;
import cherry.Ui;
import cherry.task.Task;
import cherry.task.TaskList;

/**
 * Represents a command which finds all tasks containing
 * the specified keyword and displays a list of those tasks.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates an FindCommand with the specified keyword.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }
    /**
     * Executes the find command by searching for all tasks with the keyword, and
     * printing the list of those tasks to the user.
     */
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
