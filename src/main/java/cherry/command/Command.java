package cherry.command;

import cherry.*;
import cherry.task.*;

import java.io.IOException;

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
