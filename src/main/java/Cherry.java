import java.util.Scanner;

public class Cherry {
    static Scanner scanner = new Scanner(System.in);

    public static void prompt() {
        System.out.println("☆");
        String command = scanner.nextLine();
        if (command.equals("bye")) {
            printMessage("See you next time, goodbye!");
        } else {
            printMessage(command);
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
