package cherry.command;

import java.io.IOException;

import cherry.storage.Storage;
import cherry.task.TaskList;
import cherry.ui.Ui;

/**
 * Represents a command to display help information.
 */
public class HelpCommand extends Command {

    /**
     * Executes the help command by displaying available commands.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws IOException {
        ui.printHelp();
        responseMessage = ui.formatHelp();
    }

    /**
     * Returns false as this is not an exit command.
     */
    @Override
    public boolean getExitStatus() {
        return false;
    }
}
