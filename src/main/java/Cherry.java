import java.util.Scanner;

public class Cherry {
    static Scanner scanner = new Scanner(System.in);
    static Task[] tasks = new Task[100];
    static int taskCount = 0;

    public static String list() {
        if (taskCount == 0) {
            return "No tasks yet!";
        }
        StringBuilder list = new StringBuilder();
        for (int i = 0; i < taskCount; i += 1) {
            list.append((i + 1)).append(". [")
                    .append(tasks[i].getStatusIcon()).append("] ")
                    .append(tasks[i].description).append("\n");
        }
        return list.toString();
    }

    public static void prompt() {
        System.out.println("☆ Type below ☆");
        String input = scanner.nextLine();
        String[] tokens = input.split(" ", 2);
        String command = tokens[0];

        switch (command) {
            case "bye" -> printMessage("See you next time, goodbye!");
            case "list" -> {
                printMessage(list());
                prompt();
            }
            case "mark" -> {
                if (tokens.length > 1) {
                    int taskNumber = Integer.parseInt(tokens[1]);
                    if (0 < taskNumber && taskNumber < 101) {
                        tasks[taskNumber - 1].markTask();
                        printMessage("Good job! I've marked this task as done:\n["
                                + tasks[taskNumber - 1].getStatusIcon() + "] "
                                + tasks[taskNumber - 1].description);
                        prompt();
                    } else {
                        printMessage("This task doesn't exist! Did you mean something else?");
                        prompt();
                    }
                } else {
                    printMessage("Please let me know which task you are referring to :)");
                    prompt();
                }
            }
            case "unmark" -> {
                if (tokens.length > 1) {
                    int taskNumber = Integer.parseInt(tokens[1]);
                    if (0 < taskNumber && taskNumber < 101) {
                        tasks[taskNumber - 1].unmarkTask();
                        printMessage("Alright, I've unmarked this task:\n["
                                + tasks[taskNumber - 1].getStatusIcon() + "] "
                                + tasks[taskNumber - 1].description);
                        prompt();
                    } else {
                        printMessage("This task doesn't exist! Did you mean something else?");
                        prompt();
                    }
                } else {
                    printMessage("Please let me know which task you are referring to :)");
                    prompt();
                }
            }
            default -> {
                tasks[taskCount] = new Task(input);
                taskCount += 1;
                printMessage("New Task: " + input);
                prompt();
            }
        }
    }

    public static void printMessage(String message) {
        String line = "____________________________________________________________\n";
        System.out.println(line);
        System.out.println(message);
        System.out.println(line);
    }

    public static void main(String[] args) {
        String logo = """
                  ___I_
                 /\\-_--\\
                /  \\_-__\\
         (•◡•)  |[]| [] |
        """;

        printMessage("Welcome to the task cafe!\n" + logo + "I'm cherry, how can I help you?");
        prompt();
    }
}
