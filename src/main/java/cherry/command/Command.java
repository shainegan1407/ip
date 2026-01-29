package cherry.command;

import java.io.IOException;

import cherry.CherryException;
import cherry.Storage;
import cherry.Ui;
import cherry.task.TaskList;

/**
 * Represents an abstract command that can be executed.
 * Concrete subclasses implement specific commands.
 */
public abstract class Command {
    protected boolean isExit = false;

    /**
     * Returns whether this command signals program exit.
     */
    public boolean getExitStatus() {
        return isExit;
    }

    /**
     * Executes the command using the given task list, UI, and storage.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage)
            throws CherryException, IOException;
}
