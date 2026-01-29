package cherry;

import java.util.Scanner;

import cherry.task.Task;
import cherry.task.TaskList;


public class Ui {
    private static final String LINE = "____________________________________________________________";
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints a message with lines above and below.
     */
    public void printMessage(String message) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println(LINE);
    }

    /**
     * Prints the welcome message.
     */
    public void printWelcome() {
        String logo = """
                      ___I_
                     /\\-_--\\
                    /  \\_-__\\
             (•◡•)  |[]| [] |
            """;
        printMessage("Welcome to the task cafe!\n" + logo + "I'm cherry, how can I help you?");
    }

    /**
     * Prints a goodbye message.
     */
    public void printGoodbye() {
        printMessage("See you next time, goodbye!");
    }

    /**
     * Prints an error message.
     */
    public void printError(String message) {
        printMessage(message);
    }

    /**
     * Reads a command from the user.
     */
    public String readPrompt() {
        System.out.println("☆ Type below ☆");
        return scanner.nextLine();
    }

    /**
     * Prints the task list.
     */
    public void printList(TaskList tasks) throws CherryException {
        if (tasks.getTaskCount() == 0) {
            printMessage("No tasks yet!");
            return;
        }

        StringBuilder list = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.getTaskCount(); i += 1) {
            list.append((i + 1)).append(". ").append(tasks.getTask(i + 1).toString()).append('\n');
        }
        printMessage(list.toString().trim());
    }

    /**
     * Prints the task list with matching keywords.
     */
    public void printMatchingList(TaskList tasks) throws CherryException {
        if (tasks.getTaskCount() == 0) {
            printMessage("No tasks match your description :(");
            return;
        }

        StringBuilder list = new StringBuilder("Here are the related tasks in your list:\n");
        for (int i = 0; i < tasks.getTaskCount(); i += 1) {
            list.append((i + 1)).append(". ").append(tasks.getTask(i + 1).toString()).append('\n');
        }
        printMessage(list.toString().trim());
    }

    /**
     * Shows a task was added.
     */
    public void printTaskAdded(Task task, int totalTasks) {
        printMessage("New Task: " + task.toString()
                + "\nNow you have " + totalTasks + " tasks in the list.");
    }

    /**
     * Shows a task was marked.
     */
    public void printTaskMarked(Task task) {
        printMessage("Good job! I've marked this task as done:\n" + task.toString());
    }

    /**
     * Shows a task was unmarked.
     */
    public void printTaskUnmarked(Task task) {
        printMessage("Alright, I've unmarked this task:\n" + task.toString());
    }

    /**
     * Shows a task was deleted.
     */
    public void printTaskDeleted(Task task, int totalTasks) {
        printMessage("Alright, I've deleted this task:\n" + task.toString()
                + "\nNow you have " + totalTasks + " tasks in the list.");
    }
}
