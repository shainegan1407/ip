package cherry.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a deadline.
 * Extends {@link Task} by including a {@link java.time.LocalDate} deadline.
 */
public class Deadline extends Task {
    protected LocalDate deadline;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

    /**
     * Creates a deadline task with the given description and deadline, unmarked.
     * Deadline given in the form: yyyy-MM-dd (E.g. 2025-12-31).
     */
    public Deadline(String description, LocalDate deadline) {
        super(description);
        this.deadline = deadline;
    }

    /**
     * Creates a deadline task with the given description, done status and deadline.
     * Deadline given in the form: yyyy-MM-dd (E.g. 2025-12-31).
     */
    public Deadline(String description, boolean isDone, LocalDate deadline) {
        super(description, isDone);
        this.deadline = deadline;
    }

    /**
     * Returns the following deadline string representation:
     * (D) | description | done status | deadline
     */
    @Override
    public String toString() {
        return "(D) " + (isDone ? "[✔] |" : "[ ] | ")
                + this.description
                + " (by: " + this.deadline.format(formatter)
                + ")";
    }

    @Override
    public String toSaveFormat() {
        return "(D) | " + (isDone ? "[✔] | " : "[ ] | ")
                + this.description
                + " | " + this.deadline.toString();
    }
}
