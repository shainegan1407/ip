public class Parser {

    /**
     * Returns index of the target string in the tokens array.
     */
    private int getIndex(String[] tokens, String text) throws CherryException {
        for (int i = 0; i < tokens.length; i += 1) {
            if (text.equals(tokens[i])) {
                return i;
            }
        }
        throw new CherryException(text + "not found in command");
    }

    /**
     * Returns the task number given in the tokens array.
     */
    private int getTaskNumber(String[] tokens) throws CherryException {
        if (tokens.length < 2) {
            throw new CherryException("Missing task number");
        }

        try {
            int index = Integer.parseInt(tokens[1]);
            if (index < 0 || index > 100) {
                throw new CherryException("Invalid task number");
            }
            return index;
        } catch (CherryException e) {
            throw new CherryException("Invalid task number");
        }
    }

    /**
     * Extracts the target string found between two specified indexes of the tokens array.
     */
    private String getTargetTokens(String[] tokens, int i, int j) throws CherryException {
        if (i < 0 || j < 0 || i > tokens.length || j > tokens.length) {
            throw new CherryException("Index out of bounds");
        }
        if (i == j) {
            return tokens[i];
        }
        StringBuilder targetTokens = new StringBuilder();
        if (i < j){
            for (int k = i + 1; k < j; k += 1) {
                targetTokens.append(" ").append(tokens[k]);
            }
        } else {
            for (int k = j + 1; k < i; k += 1) {
                targetTokens.append(" ").append(tokens[k]);
            }
        }
        return targetTokens.toString();
    }

    /**
     * Extracts task data from a given line to return the respective Task object.
     * Used in loading tasks from storage, to convert each line into a Task object.
     */
    public Task getTaskFromString(String input) {
        String[] tokens = input.split("\\|", 50);
        String taskType = tokens[0];
        boolean isDone = tokens[1].equals("[✔]");
        String description = tokens[2];

        switch (taskType) {
            case "T" -> {
                return new Task(description, isDone);
            }
            case "D" -> {
                return new Deadline(description, isDone, tokens[3]);
            }
            case "E" -> {
                return new Event(description, isDone, tokens[3], tokens[4]);
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * Extracts command data from the given input to return the respective Command object.
     */
    public Command parse(String input) throws CherryException {
        String[] tokens = input.split(" ", 50);
        if (tokens.length < 1) {
            throw new CherryException("No user input :(");
        }
        switch (tokens[0]) {
            case "bye" -> {
                return new ByeCommand();
            }
            case "list" -> {
                return new ListCommand();
            }
            case "todo" -> {
                if (tokens.length < 1) {
                    throw new CherryException("No user input :(");
                }
                return new AddCommand(new Task(tokens[1]));
            }
            case "event" -> {
                int fromIndex = getIndex(tokens, "/from");
                int toIndex = getIndex(tokens, "/to");
                if (fromIndex < tokens.length && fromIndex > 0
                        && toIndex < tokens.length && toIndex > 0
                        && fromIndex < toIndex) {
                    String from = getTargetTokens(tokens, fromIndex, toIndex);
                    String to = getTargetTokens(tokens, toIndex, tokens.length);
                    return new AddCommand(new Event(tokens[1], from, to));
                }
            }
            case "deadline" -> {
                int byIndex = getIndex(tokens, "/by");
                if (byIndex < tokens.length && byIndex > 0) {
                    String deadline = getTargetTokens(tokens, byIndex, tokens.length);
                    return new AddCommand(new Deadline(tokens[1], deadline));
                }
            }
            case "mark" -> {
                return new MarkCommand(getTaskNumber(tokens));
            }
            case "unmark" -> {
                return new UnmarkCommand(getTaskNumber(tokens));
            }
            case "delete" -> {
                return new DeleteCommand(getTaskNumber(tokens));
            }
        }
        throw new CherryException("Sorry, the task cafe can't help with that yet!");
    }
}

