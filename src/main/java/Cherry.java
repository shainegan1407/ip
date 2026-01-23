import java.util.ArrayList;
import java.util.Scanner;

public class Cherry {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Task> tasks = new ArrayList<>();

    public static String list() {
        if (tasks.isEmpty()) {
            return "No tasks yet!";
        }
        StringBuilder list = new StringBuilder();
        for (int i = 0; i < tasks.size(); i += 1) {
            list.append((i + 1)).append(". ").append(tasks.get(i).toString()).append('\n');
        }
        return list.toString();
    }

    private static void addTask(Task task) throws CherryException {
        tasks.add(task);
        printMessage("New Task: " + task.toString());
    }

    private static void modifyTask(String[] tokens, String action) throws CherryException {
        if (tokens.length <= 1) {
            throw new CherryException("You will need to specify the task number");
        }

        int taskNumber = Integer.parseInt(tokens[1]);
        if (taskNumber <= 0 || taskNumber > tasks.size()) {
            throw new CherryException("This task does not exist, did you mean something else?");
        }

        Task task = tasks.get(taskNumber - 1);

        switch (action) {
            case "mark" -> {
                task.markTask();
                printMessage("Good job! I've marked this task as done:\n"
                        + task);
            }
            case "unmark" -> {
                task.unmarkTask();
                printMessage("Alright, I've unmarked this task:\n"
                        + task);
            }
            case "delete" -> {
                tasks.remove(taskNumber - 1);
                printMessage("Alright, I've deleted this task:\n"
                        + task.toString()
                        + "Now you have " + tasks.size() + " tasks in the list\n");
            }
        }
    }

    public static void prompt() {
        System.out.println("☆ Type below ☆");
        String input = scanner.nextLine();
        String[] tokens = input.split(" ", 50);
        String command = tokens[0];

        try {
            switch (command) {
                case "todo" -> addTask(new Task(tokens));
                case "event" -> addTask(new Event(tokens));
                case "deadline" -> addTask(new Deadline(tokens));
                case "mark", "unmark", "delete" -> modifyTask(tokens, command);
                case "list" -> printMessage(list());
                case "bye" -> {
                    printMessage("See you next time, goodbye!");
                    return;
                }
                default -> throw new CherryException("I'm sorry, the task cafe can't help you with this yet");
            }
            prompt();
        } catch (CherryException e) {
            printMessage(e.getMessage());
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
