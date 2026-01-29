package cherry.task;

/**
 * Represents a generic task which has a description and done status.
 * Serves as a base class for specific task types like {@link Deadline} and {@link Event}.
 */
public class Task {
    protected final String description;
    protected boolean isDone;

    /**
     * Creates a to-do task with the given description, unmarked.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Creates a to-do task with the given description and done status.
     */
    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    /**
     * Marks the task.
     */
    public void markTask() {
        this.isDone = true;
    }

    /**
     * Unmarks the task.
     */
    public void unmarkTask() {
        this.isDone = false;
    }

    /**
     * Returns the following to-do string representation:
     * (T) | description | done status
     */
    @Override
    public String toString() {
        return "(T) " + (isDone ? "[✔] | " : "[ ] | ")
                + this.description;
    }

    /**
     * Returns the following to-do string representation for saving in the data file.
     */
    public String toSaveFormat() {
        return this.toString();
    }
}
