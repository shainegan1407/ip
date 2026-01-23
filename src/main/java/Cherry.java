import java.util.ArrayList;
import java.util.Scanner;

public class Cherry {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Task> tasks = new ArrayList<>();
    static int taskCount = 0;

    public static String list() {
        if (taskCount == 0) {
            return "No tasks yet!";
        }
        StringBuilder list = new StringBuilder();
        for (int i = 0; i < taskCount; i += 1) {
            list.append((i + 1)).append(". ").append(tasks.get(i).toString()).append('\n');
        }
        return list.toString();
    }

    public static void prompt() {
        System.out.println("☆ Type below ☆");
        String input = scanner.nextLine();
        String[] tokens = input.split(" ", 50);
        String command = tokens[0];

        try {
            switch (command) {
                case "todo" -> {
                    tasks.add(new Task(tokens));
                    taskCount += 1;
                    printMessage("New Task: " + tasks.get(taskCount - 1).toString());
                    prompt();
                }
                case "event" -> {
                    tasks.add(new Event(tokens));
                    taskCount += 1;
                    printMessage("New Task: " + tasks.get(taskCount - 1).toString());
                    prompt();
                }
                case "deadline" -> {
                    tasks.add(new Deadline(tokens));
                    taskCount += 1;
                    printMessage("New Task: " + tasks.get(taskCount - 1).toString());
                    prompt();
                }
                case "mark" -> {
                    if (tokens.length > 1) {
                        int taskNumber = Integer.parseInt(tokens[1]);
                        if (0 < taskNumber && taskNumber < 101) {
                            tasks.get(taskNumber - 1).markTask();
                            printMessage("Good job! I've marked this task as done:\n"
                                    + tasks.get(taskNumber - 1).toString());
                        } else {
                            throw new CherryException("This task does not exist, did you mean something else?");
                        }
                    } else {
                        throw new CherryException("You will need to specify the task number");
                    }
                    prompt();
                }
                case "unmark" -> {
                    if (tokens.length > 1) {
                        int taskNumber = Integer.parseInt(tokens[1]);
                        if (0 < taskNumber && taskNumber < 101) {
                            tasks.get(taskNumber - 1).unmarkTask();
                            printMessage("Alright, I've unmarked this task:\n"
                                    + tasks.get(taskNumber - 1).toString());
                        } else {
                            throw new CherryException("This task does not exist, did you mean something else?");
                        }
                    } else {
                        throw new CherryException("You will need to specify the task number");
                    }
                    prompt();
                }
                case "delete" -> {
                    if (tokens.length > 1) {
                        int taskNumber = Integer.parseInt(tokens[1]);
                        if (0 < taskNumber && taskNumber < 101) {
                            taskCount -= 1;
                            printMessage("Alright, I've deleted this task:\n"
                                    + tasks.get(taskNumber - 1).toString()
                                    + "Now you have " + taskCount + " tasks in the list\n");
                            tasks.remove(taskNumber - 1);
                        } else {
                            throw new CherryException("This task does not exist, did you mean something else?");
                        }
                    } else {
                        throw new CherryException("You will need to specify the task number");
                    }
                    prompt();
                }
                case "list" -> {
                    printMessage(list());
                    prompt();
                }
                case "bye" -> printMessage("See you next time, goodbye!");
                default -> throw new CherryException("I'm sorry, the task cafe can't help you with this yet");

            }
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
