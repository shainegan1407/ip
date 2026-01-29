package cherry.command;

import cherry.CherryException;
import cherry.Storage;
import cherry.Ui;
import cherry.task.TaskList;


public class ListCommand extends Command {
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws CherryException {
        ui.printList(tasks);
    }
}

