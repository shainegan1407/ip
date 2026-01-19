import java.util.Scanner;

public class Cherry {
    static Scanner scanner = new Scanner(System.in);
    static String[] tasks = new String[100];
    static int taskCount = 0;

    public static String list() {
        if (taskCount == 0) {
            return "No tasks yet!";
        }
        StringBuilder list = new StringBuilder();
        for (int i = 0; i < taskCount; i += 1) {
            list.append((i + 1)).append(". ").append(tasks[i]).append("\n");
        }
        return list.toString();
    }

    public static void prompt() {
        System.out.println("☆");
        String input = scanner.nextLine();
        if (input.equals("bye")) {
            printMessage("See you next time, goodbye!");
        } else if (input.equals("list")) {
            printMessage(list());
            prompt();
        } else {
            tasks[taskCount] = input;
            taskCount += 1;
            printMessage("New Task: " + input);
            prompt();
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
