package cherry.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cherry.command.AddCommand;
import cherry.command.ByeCommand;
import cherry.command.Command;
import cherry.command.DeleteCommand;
import cherry.command.DuplicateCommand;
import cherry.command.FindCommand;
import cherry.command.HelpCommand;
import cherry.command.ListCommand;
import cherry.command.MarkCommand;
import cherry.command.UnmarkCommand;
import cherry.command.UpdateCommand;
import cherry.exception.CherryException;
import cherry.task.Deadline;
import cherry.task.Event;
import cherry.task.Task;
import cherry.task.Todo;

/**
 * Parses user input into its respective {@link Command} objects.
 */
public class Parser {
    private static final int MAX_TASKS = 100;
    private static final int MAX_DESCRIPTION_LENGTH = 500;

    /**
     * Normalizes input by trimming and collapsing multiple spaces.
     */
    private String normalizeInput(String input) {
        return input.trim().replaceAll("\\s+", " ");
    }

    /**
     * Returns index of the target string in the tokens array.
     */
    private int getIndex(String[] tokens, String text) throws CherryException {
        assert tokens != null : "Tokens should not be null";
        assert text != null : "Search text should not be null";

        int count = 0;
        int lastIndex = -1;

        for (int i = 0; i < tokens.length; i++) {
            if (text.equals(tokens[i])) {
                count += 1;
                lastIndex = i;
            }
        }

        if (count == 0) {
            throw new CherryException("Hmm, I don't see '" + text + "' in your order. "
                    + "Did you forget to add it?");
        }
        if (count > 1) {
            throw new CherryException("You mentioned '" + text + "' " + count + " times. "
                    + "Please use it only once in your order!");
        }

        return lastIndex;
    }
    /**
     * Returns true if specified token is found within the tokens array.
     */
    private boolean hasToken(String[] tokens, String text) {
        assert tokens != null;
        for (String token : tokens) {
            if (text.equals(token)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Returns the task number given in the tokens array.
     */
    private int getTaskNumber(String[] tokens) throws CherryException {
        assert tokens != null : "Tokens should not be null";

        if (tokens.length < 2) {
            throw new CherryException("Please tell me which item on your list");
        }

        String numberStr = tokens[1].trim();

        // Check for non-numeric characters
        if (!numberStr.matches("\\d+")) {
            throw new CherryException("☕ Hmm, '" + numberStr + "' doesn't look like a number. "
                    + "Please use a number like 1, 2, 3...");
        }

        try {
            int taskIndex = Integer.parseInt(numberStr);
            if (taskIndex < 1) {
                throw new CherryException("Task numbers start from 1! "
                        + "Please use a positive number.");
            }
            if (taskIndex > MAX_TASKS) {
                throw new CherryException("Whoa! Task number " + taskIndex + " is too high! "
                        + "Please use a number up to " + MAX_TASKS + ".");
            }

            return taskIndex;
        } catch (NumberFormatException e) {
            throw new CherryException("Oops! '" + numberStr + "' is not a valid number. "
                    + "Try something like 1, 2, or 3!");
        }
    }

    /**
     * Extracts the target string found between two specified indexes of the tokens array.
     */
    private String getTargetTokens(String[] tokens, int startTokensIndex, int endTokensIndex) throws CherryException {
        assert tokens != null : "Tokens should not be null";
        assert startTokensIndex >= 0 : "Start index should be non-negative";
        assert endTokensIndex >= 0 : "End index should be non-negative";

        if (endTokensIndex > tokens.length || startTokensIndex >= endTokensIndex) {
            throw new CherryException("Oops! Something went wrong with your order. "
                    + "Please check your command format.");
        }

        String result = String.join(" ", Arrays.copyOfRange(tokens, startTokensIndex + 1, endTokensIndex)).trim();

        if (result.isEmpty()) {
            throw new CherryException("Your description seems to be empty! "
                    + "Please add some details.");
        }

        if (result.length() > MAX_DESCRIPTION_LENGTH) {
            throw new CherryException("Wow, that's quite detailed! "
                    + "Please keep descriptions under " + MAX_DESCRIPTION_LENGTH + " characters.");
        }

        return result;
    }

    /**
     * Extracts task data from a given line to return the respective cherry.task.Task object.
     * Used in loading tasks from storage, to convert each line into a cherry.task.Task object.
     */
    public Task getTaskFromString(String input) throws CherryException {
        assert input != null : "Input should not be null";

        if (input.trim().isEmpty()) {
            throw new CherryException("Empty line in data file - skipping");
        }

        String[] tokens = input.split("\\|", -1); // -1 to preserve empty trailing fields

        if (tokens.length < 3) {
            throw new CherryException("Invalid task format: " + input);
        }

        String taskType = tokens[0].trim();
        String doneStatus = tokens[1].trim();
        String description = tokens[2].trim();

        // Validate done status
        if (!doneStatus.equals("[X]") && !doneStatus.equals("[ ]")) {
            throw new CherryException("Invalid done status: " + doneStatus);
        }

        boolean isDone = doneStatus.equals("[X]");

        if (description.isEmpty()) {
            throw new CherryException("Task description is empty");
        }

        Task task;
        try {
            switch (taskType) {
            case "(T)":
                task = new Todo(description, isDone);
                break;
            case "(D)":
                if (tokens.length < 4) {
                    throw new CherryException("Deadline is missing date field");
                }
                if (tokens[3].trim().isEmpty()) {
                    throw new CherryException("Deadline date is empty");
                }
                task = new Deadline(description, isDone, getDate(tokens[3].trim()));
                break;
            case "(E)":
                if (tokens.length < 5) {
                    throw new CherryException("Event is missing time fields");
                }
                if (tokens[3].trim().isEmpty() || tokens[4].trim().isEmpty()) {
                    throw new CherryException("Event times are empty");
                }
                String from = tokens[3].trim();
                String to = tokens[4].trim();
                task = new Event(description, isDone, from, to);
                break;
            default:
                throw new CherryException("Unknown task type: " + taskType);
            }
        } catch (CherryException e) {
            throw new CherryException("Error parsing task from file: " + e.getMessage());
        }
        return task;
    }

    /**
     * Validates and parses date from input string in ISO_LOCAL_DATE pattern (yyyy-MM-dd)
     */
    public LocalDate getDate(String input) throws CherryException {
        assert input != null : "Date input should not be null";

        String trimmed = input.trim();

        if (trimmed.isEmpty()) {
            throw new CherryException("The date is missing! Please add a date in yyyy-MM-dd format "
                    + "(like 2025-12-31).");
        }

        // Check for common format mistakes
        if (!trimmed.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new CherryException("The date format doesn't look right. "
                    + "Please use yyyy-MM-dd format (like 2025-12-31)");
        }
        try {
            LocalDate date = LocalDate.parse(trimmed);

            // Check if date is too far in the past
            if (date.isBefore(LocalDate.now().minusYears(10))) {
                throw new CherryException("This date is very far back!"
                        + "Are you sure?");
            }

            // Check if date is too far in the future
            if (date.isAfter(LocalDate.now().plusYears(50))) {
                throw new CherryException("That's really far in the future! "
                        + "Let's keep it within 50 years, shall we?");
            }
            return date;
        } catch (DateTimeParseException e) {
            // Specific error for invalid dates like Feb 30
            String[] parts = trimmed.split("-");
            if (parts.length == 3) {
                throw new CherryException(trimmed + " is not a valid date. "
                        + "Please check the month and day.");
            }
            throw new CherryException("Hmm, I couldn't understand that date: " + trimmed + "\n"
                    + "Please use yyyy-MM-dd format (like 2025-12-31).");
        }
    }

    /**
     * Suggests similar commands for typos.
     */
    private String getSuggestion(String input) {
        String[] commands = {"todo", "deadline", "event", "list", "find",
            "mark", "unmark", "delete", "update", "duplicate", "help", "bye"};

        for (String cmd : commands) {
            if (levenshteinDistance(input, cmd) <= 2) {
                return "Did you mean '" + cmd + "'?";
            }
        }
        return "";
    }

    /**
     * Calculates Levenshtein distance for typo detection.
     */
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost);
            }
        }

        return dp[s1.length()][s2.length()];
    }

    /**
     * Extracts command data from the given input to return the respective cherry.command.Command object.
     */
    public Command parse(String input) throws CherryException {
        assert input != null : "Input should not be null";

        String normalized = normalizeInput(input);

        if (normalized.isEmpty()) {
            throw new CherryException("Sorry, I didn't hear anything! "
                    + "Please type a command.");
        }

        String[] tokens = normalized.split(" ", 50);

        assert tokens.length > 0;
        String commandWord = tokens[0].toLowerCase();

        Command command;
        try {
            switch (commandWord) {
            case "help":
                command = new HelpCommand();
                break;

            case "bye":
                command = new ByeCommand();
                break;

            case "list":
                command = new ListCommand();
                break;

            case "find":
                if (tokens.length < 2) {
                    throw new CherryException("What should I look for? "
                            + "Please add a keyword after 'find'.");
                }
                String keyword = getTargetTokens(tokens, 0, tokens.length);
                command = new FindCommand(keyword);
                break;

            case "todo":
                if (tokens.length < 2) {
                    throw new CherryException("Your todo needs a description! "
                            + "Try: todo read book");
                }
                String todoDesc = getTargetTokens(tokens, 0, tokens.length);
                command = new AddCommand(new Todo(todoDesc));
                break;

            case "deadline":
                if (tokens.length < 2) {
                    throw new CherryException("Your deadline needs details! "
                            + "Try: deadline return book /by 2025-12-31");
                }

                int byIndex = getIndex(tokens, "/by");

                if (byIndex == 1) {
                    throw new CherryException("What's the deadline for? "
                            + "Add a description before /by.");
                }

                if (byIndex == tokens.length - 1) {
                    throw new CherryException("When is the deadline? "
                            + "Add a date after /by.");
                }

                String deadlineDesc = getTargetTokens(tokens, 0, byIndex);
                String deadlineStr = getTargetTokens(tokens, byIndex, tokens.length);
                LocalDate deadlineDate = getDate(deadlineStr);

                // Check if deadline is in the past
                if (deadlineDate.isBefore(LocalDate.now())) {
                    throw new CherryException("That deadline has already passed! "
                            + "Are you sure you want to add it?");
                }

                command = new AddCommand(new Deadline(deadlineDesc, deadlineDate));
                break;

            case "event":
                if (tokens.length < 2) {
                    throw new CherryException("Your event needs details! "
                            + "Try: event meeting /from 2pm /to 4pm");
                }

                int fromIndex = getIndex(tokens, "/from");
                int toIndex = getIndex(tokens, "/to");

                if (fromIndex >= toIndex) {
                    throw new CherryException("Oops! /from should come before /to in your command.");
                }

                if (fromIndex == 1) {
                    throw new CherryException("What's the event about? "
                            + "Add a description before /from.");
                }

                if (fromIndex == toIndex - 1) {
                    throw new CherryException("When does the event start? "
                            + "Add a start time after /from.");
                }

                if (toIndex == tokens.length - 1) {
                    throw new CherryException("When does the event end? "
                            + "Add an end time after /to.");
                }

                String eventDesc = getTargetTokens(tokens, 0, fromIndex);
                String from = getTargetTokens(tokens, fromIndex, toIndex);
                String to = getTargetTokens(tokens, toIndex, tokens.length);

                // Validate start/end times aren't identical
                if (from.equalsIgnoreCase(to)) {
                    throw new CherryException("The start and end times are the same! "
                            + "An event should have a duration.");
                }

                command = new AddCommand(new Event(eventDesc, from, to));
                break;

            case "mark":
                command = new MarkCommand(getTaskNumber(tokens));
                break;

            case "unmark":
                command = new UnmarkCommand(getTaskNumber(tokens));
                break;

            case "delete":
                command = new DeleteCommand(getTaskNumber(tokens));
                break;

            case "duplicate":
                command = new DuplicateCommand(getTaskNumber(tokens));
                break;

            case "update":
                int updateIndex = getTaskNumber(tokens);
                Map<String, String> fields = new HashMap<>();

                if (hasToken(tokens, "/desc")) {
                    int descIndex = getIndex(tokens, "/desc");
                    int nextIndex = tokens.length;
                    for (String field : new String[]{"/by", "/from", "/to"}) {
                        if (hasToken(tokens, field)) {
                            int idx = getIndex(tokens, field);
                            if (idx > descIndex && idx < nextIndex) {
                                nextIndex = idx;
                            }
                        }
                    }
                    fields.put("desc", getTargetTokens(tokens, descIndex, nextIndex));
                }

                if (hasToken(tokens, "/by")) {
                    int byIdx = getIndex(tokens, "/by");
                    int nextIndex = tokens.length;
                    for (String field : new String[]{"/desc", "/from", "/to"}) {
                        if (hasToken(tokens, field)) {
                            int idx = getIndex(tokens, field);
                            if (idx > byIdx && idx < nextIndex) {
                                nextIndex = idx;
                            }
                        }
                    }
                    String dateStr = getTargetTokens(tokens, byIdx, nextIndex);
                    getDate(dateStr); // Validate date format
                    fields.put("by", dateStr);
                }

                if (hasToken(tokens, "/from")) {
                    int fromIdx = getIndex(tokens, "/from");
                    int nextIndex = tokens.length;
                    for (String field : new String[]{"/desc", "/by", "/to"}) {
                        if (hasToken(tokens, field)) {
                            int idx = getIndex(tokens, field);
                            if (idx > fromIdx && idx < nextIndex) {
                                nextIndex = idx;
                            }
                        }
                    }
                    fields.put("from", getTargetTokens(tokens, fromIdx, nextIndex));
                }

                if (hasToken(tokens, "/to")) {
                    int toIdx = getIndex(tokens, "/to");
                    fields.put("to", getTargetTokens(tokens, toIdx, tokens.length));
                }

                if (fields.isEmpty()) {
                    throw new CherryException("What would you like to update? "
                            + "Use /desc, /by, /from, or /to to specify changes.");
                }

                command = new UpdateCommand(updateIndex, fields);
                break;

            default:
                String suggestion = getSuggestion(commandWord);
                throw new CherryException("Sorry, I don't recognise '" + commandWord + "' as a command. "
                        + suggestion + "\n"
                        + "Type 'help' to see all available commands!");
            }
        } catch (CherryException e) {
            throw e;
        } catch (Exception e) {
            throw new CherryException("Something unexpected happened: " + e.getMessage() + "\n"
                    + "Please check your command format and try again!");
        }

        return command;
    }
}

