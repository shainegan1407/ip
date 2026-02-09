package cherry.command;

import java.io.IOException;

import cherry.exception.CherryException;
import cherry.storage.Storage;
import cherry.task.TaskList;
import cherry.ui.Ui;

/**
 * Represents an abstract command that can be executed.
 * Concrete subclasses implement specific commands.
 */
public abstract class Command {
    protected boolean isExit = false;
    protected String responseMessage = "";

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
    @Override
    public String toString() {
        return responseMessage;
    }
}
