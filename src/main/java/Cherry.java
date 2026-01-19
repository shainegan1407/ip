public class Cherry {
    public static void printMessage(String message) {
        String line = "____________________________________________________________\n";
        System.out.println(line);
        System.out.println(message);
        System.out.println(line);
    }
    public static void main(String[] args) {
        String logo = "\n          ___I_   \n"
                + "         /\\-_--\\  \n"
                + "        /  \\_-__\\ \n"
                + " (•◡•)  |[]| [] | \n";

        printMessage("Welcome to the task cafe!" + logo + "I'm cherry, how can I help you?");
        printMessage("See you next time, goodbye!");
    }
}
