package cherry.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import cherry.command.AddCommand;
import cherry.command.ByeCommand;
import cherry.command.Command;
import cherry.command.DeleteCommand;
import cherry.command.FindCommand;
import cherry.command.ListCommand;
import cherry.command.MarkCommand;
import cherry.command.UnmarkCommand;
import cherry.exception.CherryException;
import cherry.task.Deadline;
import cherry.task.Event;
import cherry.task.Task;

/**
 * Parses user input into its respective {@link Command} objects.
 */
public class Parser {
    private static final int MAX_TASKS = 100;
    /**
     * Returns index of the target string in the tokens array.
     */
    private int getIndex(String[] tokens, String text) throws CherryException {
        for (int i = 0; i < tokens.length; i += 1) {
            if (text.equals(tokens[i])) {
                return i;
            }
        }
        throw new CherryException(text + " not found in command");
    }

    /**
     * Returns the task number given in the tokens array.
     */
    private int getTaskNumber(String[] tokens) throws CherryException {
        if (tokens.length < 2) {
            throw new CherryException("Missing task number");
        }

        try {
            int taskIndex = Integer.parseInt(tokens[1]);
            if (taskIndex < 1 || taskIndex > MAX_TASKS) {
                throw new CherryException("Invalid task number");
            }
            return taskIndex;
        } catch (NumberFormatException e) {
            throw new CherryException("Invalid task number");
        }
    }

    /**
     * Extracts the target string found between two specified indexes of the tokens array.
     */
    private String getTargetTokens(String[] tokens, int startTokensIndex, int endTokensIndex) throws CherryException {
        if (startTokensIndex < 0 || endTokensIndex > tokens.length || startTokensIndex >= endTokensIndex) {
            throw new CherryException("Invalid token range");
        }
        return String.join(" ", Arrays.copyOfRange(tokens, startTokensIndex + 1, endTokensIndex));
    }

    /**
     * Extracts task data from a given line to return the respective cherry.task.Task object.
     * Used in loading tasks from storage, to convert each line into a cherry.task.Task object.
     */
    public Task getTaskFromString(String input) throws CherryException {
        String[] tokens = input.split("\\|", 50);
        if (tokens.length < 3) {
            throw new CherryException("Invalid task format in data file: " + input);
        }
        String taskType = tokens[0].trim();
        boolean isDone = tokens[1].trim().equals("[✔]");
        String taskDescription = tokens[2].trim();

        return switch (taskType) {
        case "(T)":
            yield new Task(taskDescription, isDone);
        case "(D)":
            if (tokens.length < 4) {
                throw new CherryException("Invalid deadline format");
            }
            yield new Deadline(taskDescription, isDone, getDate(tokens[3].trim()));
        case "(E)":
            if (tokens.length < 5) {
                throw new CherryException("Invalid event format");
            }
            yield new Event(taskDescription, isDone, tokens[3].trim(), tokens[4].trim());
        default:
            throw new CherryException("Unknown task type: " + taskType);
        };
    }

    /**
     * Extracts date from input string in ISO_LOCAL_DATE pattern (yyyy-MM-dd)
     */
    public LocalDate getDate(String input) throws CherryException {
        try {
            return LocalDate.parse(input.trim());
        } catch (DateTimeParseException e) {
            throw new CherryException("Invalid date format. Please use yyyy-MM-dd (E.g. 2025-12-31)");
        }
    }

    /**
     * Extracts command data from the given input to return the respective cherry.command.Command object.
     */
    public Command parse(String input) throws CherryException {
        String[] tokens = input.split(" ", 50);
        if (tokens.length < 1 || tokens[0].isEmpty()) {
            throw new CherryException("No user input :(");
        }

        switch (tokens[0].toLowerCase()) {
        case "bye":
            return new ByeCommand();
        case "list":
            return new ListCommand();
        case "find":
            if (tokens.length < 2) {
                throw new CherryException("No keyword description");
            }
            String keyword = getTargetTokens(tokens, 0, tokens.length);
            return new FindCommand(keyword);
        case "todo":
            if (tokens.length < 2) {
                throw new CherryException("No task description");
            }
            String taskDescription = getTargetTokens(tokens, 0, tokens.length);
            return new AddCommand(new Task(taskDescription));
        case "event":
            int fromIndex = getIndex(tokens, "/from");
            int toIndex = getIndex(tokens, "/to");
            if (fromIndex <= 1 || toIndex <= 1) {
                throw new CherryException("Please give an event description");
            }
            if (fromIndex >= toIndex) {
                throw new CherryException("/from must come before /to");
            }
            String eventDescription = getTargetTokens(tokens, 0, fromIndex);
            String fromDescription = getTargetTokens(tokens, fromIndex, toIndex);
            String toDescription = getTargetTokens(tokens, toIndex, tokens.length);
            return new AddCommand(new Event(eventDescription, fromDescription, toDescription));
        case "deadline":
            int byIndex = getIndex(tokens, "/by");
            if (byIndex <= 1) {
                throw new CherryException("Please give a deadline description");
            }
            String deadlineDescription = getTargetTokens(tokens, 0, byIndex);
            String deadlineString = getTargetTokens(tokens, byIndex, tokens.length);
            LocalDate deadlineLocalDate = getDate(deadlineString);
            return new AddCommand(new Deadline(deadlineDescription, deadlineLocalDate));
        case "mark":
            return new MarkCommand(getTaskNumber(tokens));
        case "unmark":
            return new UnmarkCommand(getTaskNumber(tokens));
        case "delete":
            return new DeleteCommand(getTaskNumber(tokens));
        default:
            throw new CherryException("Sorry, the task cafe can't help with that yet!");
        }
    }
}

