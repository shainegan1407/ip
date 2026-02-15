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
     *
     * @param tasks   the task list (not used)
     * @param ui      the user interface to display help message
     * @param storage the storage (not used)
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws IOException {
        ui.printHelp();
        responseMessage = ui.formatHelp();
    }

    /**
     * Returns false as this is not an exit command.
     *
     * @return false
     */
    @Override
    public boolean getExitStatus() {
        return false;
    }
}
