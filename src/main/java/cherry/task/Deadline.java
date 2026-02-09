package cherry.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Represents a task with a deadline.
 * Extends {@link Task} by including a {@link java.time.LocalDate} deadline.
 */
public class Deadline extends Task {
    protected LocalDate deadlineLocalDate;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

    /**
     * Creates a deadline task with the given description and deadline, unmarked.
     * Deadline given in the form: yyyy-MM-dd (E.g. 2025-12-31).
     */
    public Deadline(String taskDescription, LocalDate deadlineLocalDate) {
        super(taskDescription);
        this.deadlineLocalDate = deadlineLocalDate;
    }

    /**
     * Creates a deadline task with the given taskDescription, done status and deadline.
     * Deadline given in the form: yyyy-MM-dd (E.g. 2025-12-31).
     */
    public Deadline(String taskDescription, boolean isDone, LocalDate deadlineLocalDate) {
        super(taskDescription, isDone);
        this.deadlineLocalDate = deadlineLocalDate;
    }
    @Override
    public void update(Map<String, String> fields) {
        updateDescription(fields.get("desc"));
        if (fields.containsKey("by")) {
            this.deadlineLocalDate = LocalDate.parse(fields.get("by"));
        }
    }

    /**
     * Returns the following deadline string representation:
     * (D) | taskDescription | done status | deadline
     */
    @Override
    public String toString() {
        return "(D) " + (isDone ? "[✔] |" : "[ ] | ")
                + this.taskDescription
                + " (by: " + this.deadlineLocalDate.format(formatter)
                + ")";
    }

    @Override
    public String toSaveFormat() {
        return "(D) | " + (isDone ? "[✔] | " : "[ ] | ")
                + this.taskDescription
                + " | " + this.deadlineLocalDate.toString();
    }
}
