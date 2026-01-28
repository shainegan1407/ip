public class ListCommand extends Command {
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws CherryException {
        ui.printList(tasks);
    }
}

