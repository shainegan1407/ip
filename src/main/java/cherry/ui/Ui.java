package cherry.ui;

import java.util.Scanner;

import cherry.exception.CherryException;
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
        String logo = """
                      ___I_
                     /\\-_--\\
                    /  \\_-__\\
             (•◡•)  |[]| [] |
            """;
        printMessage("Welcome to the task cafe!\n" + logo + "I'm Cherry, how can I help you?");
    }

    /**
     * Formats the welcome message (GUI mode).
     */
    public String formatWelcome() {
        return "Welcome to the task cafe! I'm Cherry, how can I help you?";
    }
    /**
     * Prints the help message (CLI mode).
     */
    public void printHelp() {
        printMessage(formatHelp());
    }

    /**
     * Formats the help message (GUI mode).
     */
    @SuppressWarnings("checkstyle:Regexp")
    public String formatHelp() {
        return """
            Here are the all commands you can use:
                        
            Adding Tasks:
            1. todo DESCRIPTION - Add a todo task
              Example: todo read book

            2. deadline DESCRIPTION /by DATE - Add a deadline 
              Example: deadline return book /by 2025-12-31
              Note: Dates must be in yyyy-MM-dd format
            
            3. event DESCRIPTION /from START /to END - Add an event
              Example: event meeting /from 2pm /to 4pm
            
            Managing Tasks:
            1. list - Show all tasks
            2. find KEYWORD - Find tasks by keyword
            3. mark INDEX - Mark task as done
            4. unmark INDEX - Mark task as not done
            5. update INDEX [/desc DESC] [/by DATE] [/from TIME] [/to TIME]
              Example: update 1 /desc new description
            6. duplicate INDEX - Duplicate a task
            7. delete INDEX - Delete a task
            Note: Task index starts from 1
            
            Other:
            1. help - Show this help message
            2. bye - Exit the application
            
            Tip: All commands are case-insensitive
            """;
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

    /**
     * Shows a task was edited (CLI mode).
     */
    public void printTaskUpdated(Task task) {
        printMessage(formatTaskUpdated(task));
    }

    /**
     * Formats a task edited message (GUI mode).
     */
    public String formatTaskUpdated(Task task) {
        return "Ok! I've edited this task:\n" + task.toString();
    }

}

