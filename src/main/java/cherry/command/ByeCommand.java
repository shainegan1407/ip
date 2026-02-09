package cherry.command;

import cherry.storage.Storage;
import cherry.task.TaskList;
import cherry.ui.Ui;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;

/**
 * Represents a command to exit the application.
 */
public class ByeCommand extends Command {
    /**
     * Executes the bye command by printing a goodbye message.
     * and setting the exit status to true.
     */
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.printGoodbye();
        responseMessage = ui.formatGoodbye();
        isExit = true;
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> Platform.exit());
        delay.play();
    }
}

