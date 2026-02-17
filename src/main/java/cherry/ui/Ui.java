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
    private static final String LINE = "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━";
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
        return COFFEE + " **Welcome to Cherry's Task Café!**" + COFFEE + "\n"
                + "Your cozy corner for managing life's orders.\n"
                + "\n"
                + "Type 'help' to see the full menu " + MENU;
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
        return MENU + "**CHERRY'S CAFÉ MENU**" + MENU + "\n\n"
                + "☕** PLACING ORDERS (Adding Tasks):**\n"
                + "  1. todo DESCRIPTION\n"
                + "     └─ Add a simple to-do task\n"
                + "     └─ Example: todo buy coffee beans\n\n"
                + "  2. deadline DESCRIPTION /by DATE\n"
                + "     └─ Add a task with a deadline\n"
                + "     └─ Example: deadline submit report /by 2025-12-31\n"
                + "     └─ Note: Date format must be yyyy-MM-dd\n\n"
                + "  3. event DESCRIPTION /from START /to END\n"
                + "     └─ Add a scheduled event\n"
                + "     └─ Example: event team meeting /from 2pm /to 4pm\n\n"
                + "☕** MANAGING ORDERS (Task Management):**\n"
                + "  4. list\n"
                + "     └─ View your complete order list\n\n"
                + "  5. find KEYWORD\n"
                + "     └─ Search for specific orders\n"
                + "     └─ Example: find meeting\n\n"
                + "  6. mark INDEX\n"
                + "     └─ Mark an order as complete\n"
                + "     └─ Example: mark 2\n\n"
                + "  7. unmark INDEX\n"
                + "     └─ Mark an order as incomplete\n"
                + "     └─ Example: unmark 2\n\n"
                + "  8. update INDEX [/desc DESC] [/by DATE] [/from TIME] [/to TIME]\n"
                + "     └─ Modify an existing order\n"
                + "     └─ Example: update 1 /desc revised task name\n\n"
                + "  9. duplicate INDEX\n"
                + "     └─ Create a copy of an existing order\n"
                + "     └─ Example: duplicate 3\n\n"
                + "  10. delete INDEX\n"
                + "     └─ Remove an order from your list\n"
                + "     └─ Example: delete 1\n\n"
                + "☕ **OTHER:**\n"
                + "  • help - Show this menu\n"
                + "  • bye - Close the café\n\n"
                + "💡 **Tips:**\n"
                + "  • All commands are case-insensitive\n"
                + "  • Task numbering starts from 1\n"
                + "  • Invalid dates will be caught\n";
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
        return COFFEE + " Thanks for visiting Cherry's Café! " + COFFEE + "\n"
                + "Your orders are saved and ready for next time.\n";
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
     * Formats the task list (GUI mode) as a cafe order list.
     */
    public String formatList(TaskList tasks) throws CherryException {
        if (tasks.getTaskCount() == 0) {
            return MENU + " Your order list is empty!\n"
                    + "Ready to place your first order?\n"
                    + "Try: 'todo buy coffee beans' " + COFFEE;
        }

        StringBuilder menu = new StringBuilder();
        menu.append(MENU).append("**YOUR CAFÉ ORDER LIST**").append(MENU).append("\n");

        int readyCount = 0;
        int preparingCount = 0;

        for (int i = 0; i < tasks.getTaskCount(); i++) {
            Task task = tasks.getTask(i + 1);
            String orderNum = String.format("#%02d", i + 1);
            String status = task.isDone() ? CHECK + " READY    " : CIRCLE + " PREPARING";

            menu.append(orderNum).append(" │ ").append(status).append("\n");
            menu.append("    ");

            // Task description
            if (task instanceof Deadline d) {
                menu.append("**");
                menu.append(d.getDescription());
                menu.append("**");
                menu.append("\n    ").append(COFFEE).append(" Due: ").append(d.getDeadline());
            } else if (task instanceof Event e) {
                menu.append(e.getDescription());
                menu.append("\n    ").append(COFFEE).append(" ")
                        .append(e.getFrom()).append(" → ").append(e.getTo());
            } else {
                menu.append(task.getDescription());
            }

            menu.append("\n");

            if (i < tasks.getTaskCount() - 1) {
                menu.append("┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈\n");
            }

            if (task.isDone()) {
                readyCount++;
            } else {
                preparingCount++;
            }
        }

        menu.append("\n");
        menu.append(String.format("**Total: %d orders │ %s %d ready │ %s %d preparing**",
                tasks.getTaskCount(), CHECK, readyCount, CIRCLE, preparingCount));

        return menu.toString();
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
            return CROSS + " No matching orders found!\n"
                    + "Try a different search term or check your spelling.";
        }

        StringBuilder list = new StringBuilder();
        list.append(" Found ").append(tasks.getTaskCount())
                .append(" matching order(s):\n");

        for (int i = 0; i < tasks.getTaskCount(); i++) {
            Task task = tasks.getTask(i + 1);
            String status = task.isDone() ? CHECK + " READY" : CIRCLE + " PREPARING";

            list.append(String.format("%d. %s │ ", i + 1, status));

            if (task instanceof Deadline d) {
                list.append(d.getDescription());
                list.append("\n   Due: ").append(d.getDeadline());
            } else if (task instanceof Event e) {
                list.append(e.getDescription());
                list.append("\n   ").append(e.getFrom()).append(" → ").append(e.getTo());
            } else {
                list.append(task.getDescription());
            }

            list.append("\n");
            if (i < tasks.getTaskCount() - 1) {
                list.append("\n");
            }
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
        String[] coffeeProgress = {
            COFFEE,
            COFFEE + COFFEE,
            COFFEE + COFFEE + COFFEE,
            COFFEE + COFFEE + COFFEE + " You're on a roll!"
        };
        int coffeeIndex = Math.min(totalTasks / 5, coffeeProgress.length - 1);
        String coffee = coffeeProgress[coffeeIndex];

        StringBuilder msg = new StringBuilder();
        msg.append(CHECK).append(" Order placed! ").append(coffee).append("\n");

        if (task instanceof Deadline d) {
            msg.append(d.getDescription());
            msg.append("\n  Due: ").append(d.getDeadline());
        } else if (task instanceof Event e) {
            msg.append(e.getDescription());
            msg.append("\n  ").append(e.getFrom()).append(" → ").append(e.getTo());
        } else {
            msg.append(task.getDescription());
        }

        msg.append("\n");
        msg.append(String.format("Total items on your list: %d", totalTasks));

        return msg.toString();
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
        StringBuilder msg = new StringBuilder();
        msg.append("Order complete! Great work!\n");
        msg.append(CHECK).append(" ");

        if (task instanceof Deadline d) {
            msg.append(d.getDescription());
            msg.append("\n  Was due: ").append(d.getDeadline());
        } else if (task instanceof Event e) {
            msg.append(e.getDescription());
            msg.append("\n  ").append(e.getFrom()).append(" → ").append(e.getTo());
        } else {
            msg.append(task.getDescription());
        }

        msg.append("Time for a coffee break? ").append(COFFEE);

        return msg.toString();
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
        StringBuilder msg = new StringBuilder();
        msg.append("Back to preparing this order:\n");
        msg.append(CIRCLE).append(" ");

        if (task instanceof Deadline d) {
            msg.append(d.getDescription());
            msg.append("\n  Due: ").append(d.getDeadline());
        } else if (task instanceof Event e) {
            msg.append(e.getDescription());
            msg.append("\n  ").append(e.getFrom()).append(" → ").append(e.getTo());
        } else {
            msg.append(task.getDescription());
        }

        return msg.toString();
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
        StringBuilder msg = new StringBuilder();
        msg.append("Order cancelled:\n");
        msg.append(CROSS).append(" ");

        if (task instanceof Deadline d) {
            msg.append(d.getDescription());
            msg.append("\n  Was due: ").append(d.getDeadline());
        } else if (task instanceof Event e) {
            msg.append(e.getDescription());
            msg.append("\n  ").append(e.getFrom()).append(" → ").append(e.getTo());
        } else {
            msg.append(task.getDescription());
        }

        msg.append(String.format("Remaining orders: %d", totalTasks));

        return msg.toString();
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
        StringBuilder msg = new StringBuilder();
        msg.append("Order updated!\n");
        msg.append(CIRCLE).append(" ");

        if (task instanceof Deadline d) {
            msg.append(d.getDescription());
            msg.append("\n  Due: ").append(d.getDeadline());
        } else if (task instanceof Event e) {
            msg.append(e.getDescription());
            msg.append("\n  ").append(e.getFrom()).append(" → ").append(e.getTo());
        } else {
            msg.append(task.getDescription());
        }

        return msg.toString();
    }
}
