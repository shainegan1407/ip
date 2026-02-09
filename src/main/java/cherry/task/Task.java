package cherry.task;

/**
 * Represents a generic task which has a description and done status.
 * Serves as a base class for specific task types like {@link Deadline} and {@link Event}.
 */
public class Task {
    protected final String taskDescription;
    protected boolean isDone;

    /**
     * Creates a to-do task with the given description, unmarked.
     */
    public Task(String taskDescription) {
        assert taskDescription != null : "Description should not be null";
        assert !taskDescription.trim().isEmpty() : "Description should not be empty";
        this.taskDescription = taskDescription;
        this.isDone = false;
    }

    /**
     * Creates a to-do task with the given description and done status.
     */
    public Task(String taskDescription, boolean isDone) {
        this.taskDescription = taskDescription;
        this.isDone = isDone;
    }

    /**
     * Marks the task.
     */
    public void markTask() {
        assert !isDone : "Task should not already be marked when marking";
        isDone = true;
    }

    /**
     * Unmarks the task.
     */
    public void unmarkTask() {
        this.isDone = false;
    }

    /**
     * Checks if the task description contains the specified keyword.
     * Returns true if description contains keyword, and false otherwise.
     */
    public boolean hasKeyword(String keyword) {
        return this.taskDescription.contains(keyword.trim());
    }

    /**
     * Returns the following to-do string representation:
     * (T) | description | done status
     */
    @Override
    public String toString() {
        return "(T) | " + (isDone ? "[✔] | " : "[ ] | ")
                + this.taskDescription;
    }

    /**
     * Returns the following to-do string representation for saving in the data file.
     */
    public String toSaveFormat() {
        return this.toString();
    }
}
