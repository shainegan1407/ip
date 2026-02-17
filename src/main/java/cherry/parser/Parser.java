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
    private static final String[] UPDATE_FIELDS = {"/desc", "/by", "/from", "/to"};

    /**
     * Extracts task data from a saved line and returns the respective Task object.
     */
    public Task getTaskFromString(String input) throws CherryException {
        assert input != null : "Input should not be null";

        if (input.trim().isEmpty()) {
            throw new CherryException("Empty line in data file - skipping.");
        }

        String[] tokens = input.split("\\|", -1);

        if (tokens.length < 3) {
            throw new CherryException("Invalid task format: " + input);
        }

        String taskType = tokens[0].trim();
        String doneStatus = tokens[1].trim();
        String description = tokens[2].trim();

        validateDoneStatus(doneStatus);
        validateDescription(description);

        boolean isDone = doneStatus.equals("[X]");

        try {
            return buildTaskFromTokens(taskType, isDone, description, tokens);
        } catch (CherryException e) {
            throw new CherryException("Error parsing task from file: " + e.getMessage());
        }
    }

    /**
     * Parses user input and returns the corresponding Command object.
     */
    public Command parse(String input) throws CherryException {
        assert input != null : "Input should not be null";

        String normalized = normalizeInput(input);

        if (normalized.isEmpty()) {
            throw new CherryException("I didn't catch that! Please type a command.\n"
                    + "Type 'help' to see all available commands.");
        }

        String[] tokens = normalized.split(" ", 50);
        String commandWord = tokens[0].toLowerCase();

        try {
            return buildCommand(commandWord, tokens);
        } catch (CherryException e) {
            throw e;
        } catch (Exception e) {
            throw new CherryException("Something unexpected happened: " + e.getMessage() + "\n"
                    + "Please check your command format and try again!");
        }
    }

    /**
     * Delegates to the appropriate command builder based on the command word.
     */
    private Command buildCommand(String commandWord, String[] tokens) throws CherryException {
        switch (commandWord) {
        case "help":
            return new HelpCommand();
        case "bye":
            return new ByeCommand();
        case "list":
            return new ListCommand();
        case "find":
            return parseFindCommand(tokens);
        case "todo":
            return parseTodoCommand(tokens);
        case "deadline":
            return parseDeadlineCommand(tokens);
        case "event":
            return parseEventCommand(tokens);
        case "mark":
            return new MarkCommand(getTaskNumber(tokens));
        case "unmark":
            return new UnmarkCommand(getTaskNumber(tokens));
        case "delete":
            return new DeleteCommand(getTaskNumber(tokens));
        case "duplicate":
            return new DuplicateCommand(getTaskNumber(tokens));
        case "update":
            return parseUpdateCommand(tokens);
        default:
            String suggestion = getSuggestion(commandWord);
            throw new CherryException("I don't recognise '" + commandWord + "' as a command. "
                    + suggestion + "\nType 'help' to see all available commands!");
        }
    }

    /**
     * Parses find commands.
     */
    private Command parseFindCommand(String[] tokens) throws CherryException {
        requireArguments(tokens, "find", "find KEYWORD\nExample: find meeting");
        return new FindCommand(getTargetTokens(tokens, 0, tokens.length));
    }

    /**
     * Parses to-do commands.
     */
    private Command parseTodoCommand(String[] tokens) throws CherryException {
        requireArguments(tokens, "todo", "todo DESCRIPTION\nExample: todo read book");
        return new AddCommand(new Todo(getTargetTokens(tokens, 0, tokens.length)));
    }

    /**
     * Parses deadline commands.
     */
    private Command parseDeadlineCommand(String[] tokens) throws CherryException {
        requireArguments(tokens, "deadline",
                "deadline DESCRIPTION /by DATE\nExample: deadline submit report /by 2025-12-31");

        int byIndex = getIndex(tokens, "/by");

        if (byIndex == 1) {
            throw new CherryException("What's the deadline for?\n"
                    + "Add a description before /by.");
        }
        if (byIndex == tokens.length - 1) {
            throw new CherryException("When is the deadline?\n"
                    + "Add a date after /by (e.g., /by 2025-12-31).");
        }

        String description = getTargetTokens(tokens, 0, byIndex);
        LocalDate date = getDate(getTargetTokens(tokens, byIndex, tokens.length));

        if (date.isBefore(LocalDate.now())) {
            throw new CherryException("That deadline has already passed!\n"
                    + "Are you sure you want to add it?");
        }

        return new AddCommand(new Deadline(description, date));
    }

    /**
     * Parses event commands.
     */
    private Command parseEventCommand(String[] tokens) throws CherryException {
        requireArguments(tokens, "event",
                "event DESCRIPTION /from START /to END\nExample: event meeting /from 2pm /to 4pm");

        int fromIndex = getIndex(tokens, "/from");
        int toIndex = getIndex(tokens, "/to");

        if (fromIndex >= toIndex) {
            throw new CherryException("/from must come before /to in your command.");
        }
        if (fromIndex == 1) {
            throw new CherryException("What's the event about?\n"
                    + "Add a description before /from.");
        }
        if (fromIndex == toIndex - 1) {
            throw new CherryException("When does the event start?\n"
                    + "Add a start time after /from.");
        }
        if (toIndex == tokens.length - 1) {
            throw new CherryException("When does the event end?\n"
                    + "Add an end time after /to.");
        }

        String description = getTargetTokens(tokens, 0, fromIndex);
        String from = getTargetTokens(tokens, fromIndex, toIndex);
        String to = getTargetTokens(tokens, toIndex, tokens.length);

        if (from.equalsIgnoreCase(to)) {
            throw new CherryException("The start and end times are the same!\n"
                    + "An event needs a duration.");
        }

        return new AddCommand(new Event(description, from, to));
    }

    /**
     * Parses update commands.
     */
    private Command parseUpdateCommand(String[] tokens) throws CherryException {
        int taskIndex = getTaskNumber(tokens);
        Map<String, String> fields = extractUpdateFields(tokens);

        if (fields.isEmpty()) {
            throw new CherryException("What would you like to update?\n"
                    + "Use /desc, /by, /from, or /to to specify changes.");
        }

        return new UpdateCommand(taskIndex, fields);
    }

    /**
     * Extracts all recognized update fields from the token array into a map.
     */
    private Map<String, String> extractUpdateFields(String[] tokens) throws CherryException {
        Map<String, String> fields = new HashMap<>();

        for (String field : UPDATE_FIELDS) {
            if (hasToken(tokens, field)) {
                int fieldIdx = getIndex(tokens, field);
                int nextIdx = findNextFieldIndex(tokens, field, fieldIdx);
                String value = getTargetTokens(tokens, fieldIdx, nextIdx);

                if (field.equals("/by")) {
                    getDate(value);
                }

                fields.put(fieldKeyName(field), value);
            }
        }

        return fields;
    }

    /**
     * Finds the index of the next field flag that appears after currentIdx,
     * returning tokens.length if none is found.
     */
    private int findNextFieldIndex(String[] tokens, String currentField, int currentIdx) {
        int nextIdx = tokens.length;

        for (String otherField : UPDATE_FIELDS) {
            if (otherField.equals(currentField)) {
                continue;
            }
            if (hasToken(tokens, otherField)) {
                try {
                    int idx = getIndex(tokens, otherField);
                    if (idx > currentIdx && idx < nextIdx) {
                        nextIdx = idx;
                    }
                } catch (CherryException ignored) {
                    // Field not present or duplicate — skip
                }
            }
        }

        return nextIdx;
    }

    /**
     * Converts a flag like "/by" into a plain key name like "by".
     */
    private String fieldKeyName(String flag) {
        return flag.substring(1);
    }

    /**
     * Constructs a Task from parsed save-file tokens.
     */
    private Task buildTaskFromTokens(String taskType, boolean isDone,
                                     String description, String[] tokens) throws CherryException {
        switch (taskType) {
        case "(T)":
            return new Todo(description, isDone);
        case "(D)":
            validateTokenCount(tokens, 4, "Deadline is missing its date field.");
            validateNotEmpty(tokens[3].trim(), "Deadline date is empty.");
            return new Deadline(description, isDone, getDate(tokens[3].trim()));
        case "(E)":
            validateTokenCount(tokens, 5, "Event is missing its time fields.");
            validateNotEmpty(tokens[3].trim(), "Event start time is empty.");
            validateNotEmpty(tokens[4].trim(), "Event end time is empty.");
            return new Event(description, isDone, tokens[3].trim(), tokens[4].trim());
        default:
            throw new CherryException("Unknown task type: " + taskType);
        }
    }

    /**
     * Throws if tokens has fewer than 2 elements (i.e., no arguments given).
     */
    private void requireArguments(String[] tokens, String command, String usage)
            throws CherryException {
        if (tokens.length < 2) {
            throw new CherryException("Your " + command + " needs more details!\n"
                    + "Usage: " + usage);
        }
    }

    /**
     * Throws if doneStatus is not one of the two valid values.
     */
    private void validateDoneStatus(String doneStatus) throws CherryException {
        if (!doneStatus.equals("[X]") && !doneStatus.equals("[ ]")) {
            throw new CherryException("Invalid done status in file: '" + doneStatus + "'");
        }
    }

    /**
     * Throws if description is blank.
     */
    private void validateDescription(String description) throws CherryException {
        if (description.isEmpty()) {
            throw new CherryException("Task description is empty.");
        }
    }

    /**
     * Throws if the token array is shorter than the required length.
     */
    private void validateTokenCount(String[] tokens, int required, String message)
            throws CherryException {
        if (tokens.length < required) {
            throw new CherryException(message);
        }
    }

    /**
     * Throws if the given value is empty.
     */
    private void validateNotEmpty(String value, String message) throws CherryException {
        if (value.isEmpty()) {
            throw new CherryException(message);
        }
    }

    /**
     * Trims and collapses multiple spaces into one.
     */
    private String normalizeInput(String input) {
        return input.trim().replaceAll("\\s+", " ");
    }

    /**
     * Returns the index of the single occurrence of text in tokens.
     */
    private int getIndex(String[] tokens, String text) throws CherryException {
        assert tokens != null : "Tokens should not be null";
        assert text != null : "Search text should not be null";

        int count = 0;
        int lastIndex = -1;

        for (int i = 0; i < tokens.length; i += 1) {
            if (text.equals(tokens[i])) {
                count += 1;
                lastIndex = i;
            }
        }

        if (count == 0) {
            throw new CherryException("'" + text + "' is missing from your command.\n"
                    + "Did you forget to add it?");
        }
        if (count > 1) {
            throw new CherryException("'" + text + "' appears " + count + " times.\n"
                    + "Please use each flag only once.");
        }

        return lastIndex;
    }

    /**
     * Returns true if text appears at least once in tokens.
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
     * Parses and validates the task number from tokens[1].
     */
    private int getTaskNumber(String[] tokens) throws CherryException {
        if (tokens.length < 2) {
            throw new CherryException("Please provide a task number.\n"
                    + "Example: mark 2");
        }

        String numberStr = tokens[1].trim();

        if (!numberStr.matches("\\d+")) {
            throw new CherryException("'" + numberStr + "' is not a valid task number.\n"
                    + "Please use a positive number like 1, 2, 3...");
        }

        int taskIndex = Integer.parseInt(numberStr);

        if (taskIndex < 1) {
            throw new CherryException("Task numbers start from 1.\n"
                    + "Please use a positive number.");
        }
        if (taskIndex > MAX_TASKS) {
            throw new CherryException("Task number " + taskIndex + " is too high!\n"
                    + "Please use a number up to " + MAX_TASKS + ".");
        }

        return taskIndex;
    }

    /**
     * Joins tokens from startTokensIndex+1 to endTokensIndex into a trimmed string.
     */
    private String getTargetTokens(String[] tokens, int startTokensIndex,
                                   int endTokensIndex) throws CherryException {
        assert tokens != null : "Tokens should not be null";
        assert startTokensIndex >= 0 : "Start index must be non-negative";
        assert endTokensIndex >= 0 : "End index must be non-negative";

        if (endTokensIndex > tokens.length || startTokensIndex >= endTokensIndex) {
            throw new CherryException("Something went wrong reading your command.\n"
                    + "Please check the format and try again.");
        }

        String result = String.join(" ",
                Arrays.copyOfRange(tokens, startTokensIndex + 1, endTokensIndex)).trim();

        if (result.isEmpty()) {
            throw new CherryException("The description is empty!\n"
                    + "Please add some details.");
        }
        if (result.length() > MAX_DESCRIPTION_LENGTH) {
            throw new CherryException("That description is too long!\n"
                    + "Please keep it under " + MAX_DESCRIPTION_LENGTH + " characters.");
        }

        return result;
    }

    /**
     * Parses and validates a date string in yyyy-MM-dd format.
     */
    public LocalDate getDate(String input) throws CherryException {
        assert input != null : "Date input should not be null";

        String trimmed = input.trim();

        if (trimmed.isEmpty()) {
            throw new CherryException("The date is missing!\n"
                    + "Please provide a date in yyyy-MM-dd format (e.g., 2025-12-31).");
        }

        if (!trimmed.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new CherryException("'" + trimmed + "' is not in the right format.\n"
                    + "Please use yyyy-MM-dd (e.g., 2025-12-31).");
        }

        try {
            LocalDate date = LocalDate.parse(trimmed);
            validateDateRange(date, trimmed);
            return date;
        } catch (DateTimeParseException e) {
            throw new CherryException("'" + trimmed + "' is not a valid date.\n"
                    + "Please check the month and day values.");
        }
    }

    /**
     * Throws if the date is too far in the past or future.
     */
    private void validateDateRange(LocalDate date, String trimmed) throws CherryException {
        if (date.isBefore(LocalDate.now().minusYears(10))) {
            throw new CherryException("'" + trimmed + "' is more than 10 years in the past.\n"
                    + "Are you sure that's correct?");
        }
        if (date.isAfter(LocalDate.now().plusYears(50))) {
            throw new CherryException("'" + trimmed + "' is more than 50 years in the future.\n"
                    + "Let's keep it within 50 years, shall we?");
        }
    }

    /**
     * Returns a suggestion string if the input closely matches a known command.
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
     * Calculates edit distance between two strings for typo detection.
     */
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i += 1) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j += 1) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= s1.length(); i += 1) {
            for (int j = 1; j <= s2.length(); j += 1) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost);
            }
        }

        return dp[s1.length()][s2.length()];
    }
}
