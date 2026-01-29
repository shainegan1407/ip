package cherry.task;

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
