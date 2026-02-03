package cherry;

import java.util.Scanner;

import cherry.task.Task;
import cherry.task.TaskList;

/**
 * Handles user interaction and message formatting.
 * Supports both CLI (print methods) and GUI (format methods) modes.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints a message with lines above and below (CLI mode).
     */
    public void printMessage(String message) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println(LINE);
    }

    /**
     * Prints the welcome message (CLI mode).
     */
    public void printWelcome() {
        printMessage(formatWelcome());
    }

    /**
     * Formats the welcome message (GUI mode).
     */
    public String formatWelcome() {
        String logo = """
                      ___I_
                     /\\-_--\\
                    /  \\_-__\\
             (•◡•)  |[]| [] |
            """;
        return "Welcome to the task cafe!\n" + logo + "I'm cherry, how can I help you?";
    }

    /**
     * Prints a goodbye message (CLI mode).
     */
    public void printGoodbye() {
        printMessage(formatGoodbye());
    }

    /**
     * Formats the goodbye message (GUI mode).
     */
    public String formatGoodbye() {
        return "See you next time, goodbye!";
    }

    /**
     * Prints an error message (CLI mode).
     */
    public void printError(String message) {
        printMessage(message);
    }

    /**
     * Formats an error message (GUI mode).
     */
    public String formatError(String message) {
        return "Error: " + message;
    }

    /**
     * Reads a command from the user (CLI mode only).
     */
    public String readPrompt() {
        System.out.println("☆ Type below ☆");
        return scanner.nextLine();
    }

    /**
     * Prints the task list (CLI mode).
     */
    public void printList(TaskList tasks) throws CherryException {
        printMessage(formatList(tasks));
    }

    /**
     * Formats the task list (GUI mode).
     */
    public String formatList(TaskList tasks) throws CherryException {
        if (tasks.getTaskCount() == 0) {
            return "No tasks yet!";
        }

        StringBuilder list = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.getTaskCount(); i += 1) {
            list.append((i + 1)).append(". ").append(tasks.getTask(i + 1).toString()).append('\n');
        }
        return list.toString().trim();
    }

    /**
     * Prints the task list with matching keywords (CLI mode).
     */
    public void printMatchingList(TaskList tasks) throws CherryException {
        printMessage(formatMatchingList(tasks));
    }

    /**
     * Formats the task list with matching keywords (GUI mode).
     */
    public String formatMatchingList(TaskList tasks) throws CherryException {
        if (tasks.getTaskCount() == 0) {
            return "No tasks match your description :(";
        }

        StringBuilder list = new StringBuilder("Here are the related tasks in your list:\n");
        for (int i = 0; i < tasks.getTaskCount(); i += 1) {
            list.append((i + 1)).append(". ").append(tasks.getTask(i + 1).toString()).append('\n');
        }
        return list.toString().trim();
    }

    /**
     * Shows a task was added (CLI mode).
     */
    public void printTaskAdded(Task task, int totalTasks) {
        printMessage(formatTaskAdded(task, totalTasks));
    }

    /**
     * Formats a task added message (GUI mode).
     */
    public String formatTaskAdded(Task task, int totalTasks) {
        return "New Task: " + task.toString()
                + "\nNow you have " + totalTasks + " tasks in the list.";
    }

    /**
     * Shows a task was marked (CLI mode).
     */
    public void printTaskMarked(Task task) {
        printMessage(formatTaskMarked(task));
    }

    /**
     * Formats a task marked message (GUI mode).
     */
    public String formatTaskMarked(Task task) {
        return "Good job! I've marked this task as done:\n" + task.toString();
    }

    /**
     * Shows a task was unmarked (CLI mode).
     */
    public void printTaskUnmarked(Task task) {
        printMessage(formatTaskUnmarked(task));
    }

    /**
     * Formats a task unmarked message (GUI mode).
     */
    public String formatTaskUnmarked(Task task) {
        return "Alright, I've unmarked this task:\n" + task.toString();
    }

    /**
     * Shows a task was deleted (CLI mode).
     */
    public void printTaskDeleted(Task task, int totalTasks) {
        printMessage(formatTaskDeleted(task, totalTasks));
    }

    /**
     * Formats a task deleted message (GUI mode).
     */
    public String formatTaskDeleted(Task task, int totalTasks) {
        return "Alright, I've deleted this task:\n" + task.toString()
                + "\nNow you have " + totalTasks + " tasks in the list.";
    }
}
