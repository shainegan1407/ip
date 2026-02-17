package cherry.ui;

import java.util.Scanner;

import cherry.exception.CherryException;
import cherry.task.Deadline;
import cherry.task.Event;
import cherry.task.Task;
import cherry.task.TaskList;

/**
 * Handles user interaction and message formatting with cafe theme.
 * Supports both CLI (print methods) and GUI (format methods) modes.
 */
public class Ui {
    private static final String LINE =
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━";
    private static final String DIVIDER = "┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈";
    private static final String COFFEE = "☕";
    private static final String MENU = "📋";
    private static final String CHECK = "✓";
    private static final String CIRCLE = "○";
    private static final String CROSS = "✗";

    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints a message with separator lines above and below (CLI mode).
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
     * Prints the help message (CLI mode).
     */
    public void printHelp() {
        printMessage(formatHelp());
    }

    /**
     * Prints a goodbye message (CLI mode).
     */
    public void printGoodbye() {
        printMessage(formatGoodbye());
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
        System.out.println(COFFEE + " Your order: ");
        return scanner.nextLine();
    }

    /**
     * Prints the task list (CLI mode).
     */
    public void printList(TaskList tasks) throws CherryException {
        printMessage(formatList(tasks));
    }

    /**
     * Prints the matching task list (CLI mode).
     */
    public void printMatchingList(TaskList tasks) throws CherryException {
        printMessage(formatMatchingList(tasks));
    }

    /**
     *  Prints a task added confirmation (CLI mode).
     */
    public void printTaskAdded(Task task, int totalTasks) {
        printMessage(formatTaskAdded(task, totalTasks));
    }

    /**
     * Prints a task marked confirmation (CLI mode).
     */
    public void printTaskMarked(Task task) {
        printMessage(formatTaskMarked(task));
    }

    /**
     *  Prints a task unmarked confirmation (CLI mode).
     */
    public void printTaskUnmarked(Task task) {
        printMessage(formatTaskUnmarked(task));
    }

    /**
     * Prints a task deleted confirmation (CLI mode).
     */
    public void printTaskDeleted(Task task, int totalTasks) {
        printMessage(formatTaskDeleted(task, totalTasks));
    }

    /**
     * Prints a task updated confirmation (CLI mode).
     */
    public void printTaskUpdated(Task task) {
        printMessage(formatTaskUpdated(task));
    }

    /**
     * Formats the welcome message (GUI mode).
     */
    public String formatWelcome() {
        return COFFEE + formatAsBold("Welcome to Cherry's Task Café!") + COFFEE + "\n"
                + "Your cozy corner for managing life's orders.\n\n"
                + "Type 'help' to see the full menu " + MENU;
    }

    /**
     * Formats the help message (GUI mode).
     */
    public String formatHelp() {
        return MENU + formatAsBold("CHERRY'S CAFÉ MENU") + MENU + "\n\n"
                + COFFEE + formatAsBold("PLACING ORDERS (Adding Tasks):\n")
                + "  1. todo DESCRIPTION\n"
                + "     └─ Example: todo buy coffee beans\n\n"
                + "  2. deadline DESCRIPTION /by DATE\n"
                + "     └─ Example: deadline submit report /by 2025-12-31\n"
                + "     └─ Note: Date format must be yyyy-MM-dd\n\n"
                + "  3. event DESCRIPTION /from START /to END\n"
                + "     └─ Example: event team meeting /from 2pm /to 4pm\n\n"
                + COFFEE + formatAsBold("MANAGING ORDERS (Task Management):\n")
                + "  4. list - View your complete order list\n"
                + "  5. find KEYWORD - Search for specific orders\n"
                + "  6. mark INDEX - Mark an order as complete\n"
                + "  7. unmark INDEX - Mark an order as incomplete\n"
                + "  8. update INDEX [/desc DESC] [/by DATE] [/from TIME] [/to TIME]\n"
                + "  9. duplicate INDEX - Create a copy of an order\n"
                + "  10. delete INDEX - Remove an order\n\n"
                + COFFEE + formatAsBold("OTHER:\n")
                + "  • help - Show this menu\n"
                + "  • bye - Close the café\n\n"
                + formatAsBold("💡Tips:\n")
                + "  • All commands are case-insensitive\n"
                + "  • Task numbering starts from 1\n"
                + "  • Invalid dates will be caught\n";
    }

    /**
     * Formats the goodbye message (GUI mode).
     */
    public String formatGoodbye() {
        return COFFEE + " Thanks for visiting Cherry's Café! " + COFFEE + "\n"
                + "Your orders are saved and ready for next time.\n";
    }

    /**
     * Formats the task list as a cafe order list (GUI mode).
     */
    public String formatList(TaskList tasks) throws CherryException {
        if (tasks.getTaskCount() == 0) {
            return MENU + " Your order list is empty!\n"
                    + "Ready to place your first order?\n"
                    + "Try: 'todo buy coffee beans' ";
        }

        StringBuilder menu = new StringBuilder();
        menu.append(MENU)
                .append(formatAsBold("YOUR CAFÉ ORDER LIST"))
                .append(MENU)
                .append("\n");

        int readyCount = 0;
        int preparingCount = 0;

        for (int i = 0; i < tasks.getTaskCount(); i += 1) {
            Task task = tasks.getTask(i + 1);
            menu.append(formatRow(i + 1, task));

            if (i < tasks.getTaskCount() - 1) {
                menu.append(DIVIDER).append("\n");
            }

            if (task.isDone()) {
                readyCount += 1;
            } else {
                preparingCount += 1;
            }
        }

        menu.append("\n");
        menu.append(String.format("Total: %d orders │ %s %d ready │ %s %d preparing",
                tasks.getTaskCount(), CHECK, readyCount, CIRCLE, preparingCount));

        return menu.toString();
    }

    /**
     * Formats the matching task list (GUI mode).
     */
    public String formatMatchingList(TaskList tasks) throws CherryException {
        if (tasks.getTaskCount() == 0) {
            return CROSS + formatAsBold(" No matching orders found!\n")
                    + "Try a different search term or check your spelling.";
        }

        StringBuilder list = new StringBuilder();
        list.append("Found ").append(tasks.getTaskCount()).append(" matching order(s):\n");

        for (int i = 0; i < tasks.getTaskCount(); i += 1) {
            Task task = tasks.getTask(i + 1);
            String status = task.isDone() ? CHECK + " READY" : CIRCLE + " PREPARING";
            list.append(String.format("%d. %s │ ", i + 1, status));
            list.append(formatTaskDetails(task)).append("\n");

            if (i < tasks.getTaskCount() - 1) {
                list.append("\n");
            }
        }

        return list.toString().trim();
    }

    /**
     * Formats a task added confirmation (GUI mode).
     */
    public String formatTaskAdded(Task task, int totalTasks) {
        return CHECK + formatAsBold("Order placed!:\n")
                + formatTaskDetails(task) + "\n"
                + "Total items on your list: " + totalTasks;
    }

    /**
     * Formats a task marked confirmation (GUI mode).
     */
    public String formatTaskMarked(Task task) {
        return formatAsBold("Order complete! Great work!\n")
                + CHECK + " " + formatTaskDetails(task) + "\n"
                + "Time for a coffee break? " + COFFEE;
    }

    /**
     * Formats a task unmarked confirmation (GUI mode).
     */
    public String formatTaskUnmarked(Task task) {
        return "Back to preparing this order:\n"
                + CIRCLE + " " + formatTaskDetails(task);
    }

    /**
     * Formats a task deleted confirmation (GUI mode).
     */
    public String formatTaskDeleted(Task task, int totalTasks) {
        return formatAsBold("Order cancelled:\n")
                + CROSS + " " + formatTaskDetails(task) + "\n"
                + "Remaining orders: "
                + totalTasks;
    }

    /**
     * Formats a task updated confirmation (GUI mode).
     */
    public String formatTaskUpdated(Task task) {
        return formatAsBold("Order updated:\n")
                + CIRCLE + " " + formatTaskDetails(task);
    }

    /**
     * Formats a single row for the task list.
     * Produces the order number, status, and task details.
     */
    private String formatRow(int index, Task task) {
        String orderNum = String.format("#%02d", index);
        String status = task.isDone() ? CHECK + " READY    " : CIRCLE + " PREPARING";

        return orderNum + " │ " + status + "\n"
                + "    " + formatTaskDetails(task) + "\n";
    }

    /**
     * Formats a task's description and time/date details into a single string.
     */
    private String formatTaskDetails(Task task) {
        if (task instanceof Deadline d) {
            return formatAsBold(d.getDescription())
                    + "\n    " + COFFEE + " Due: " + d.getDeadline();
        } else if (task instanceof Event e) {
            return formatAsBold(e.getDescription())
                    + "\n    " + COFFEE + " " + e.getFrom() + " → " + e.getTo();
        } else {
            return formatAsBold(task.getDescription());
        }
    }

    /**
     * Formats a string as bold.
     */
    private String formatAsBold(String string) {
        return "**" + string + "**";
    }
}
