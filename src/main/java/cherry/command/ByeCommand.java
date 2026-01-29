package cherry.command;

import cherry.Storage;
import cherry.Ui;
import cherry.task.TaskList;

public class ByeCommand extends Command {
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.printGoodbye();
        isExit = true;
    }
}

