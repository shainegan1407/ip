package cherry.command;

import java.io.IOException;

import cherry.CherryException;
import cherry.Storage;
import cherry.Ui;
import cherry.task.TaskList;

public abstract class Command {
    protected boolean isExit = false;

    /**
     * Returns exit status.
     */
    public boolean getExitStatus() {
        return isExit;
    }

    public abstract void execute(TaskList tasks, Ui ui, Storage storage)
            throws CherryException, IOException;
}
