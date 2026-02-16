package cherry.task;

import java.util.Map;

/**
 * Represents a generic task which has a description and done status.
 * Serves as a base class for specific task types like {@link Deadline} and {@link Event}.
 */
public abstract class Task {
    protected String taskDescription;
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
     * Gets the task description.
     */
    public String getDescription() {
        return taskDescription;
    }

    /**
     * Returns whether the task is done.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Gets the status icon for menu display.
     */
    public String getStatusIcon() {
        return isDone ? "✓" : "○";
    }

    /**
     * Gets the formatted status string.
     */
    public String getFormattedStatus() {
        return isDone ? "READY" : "PREPARING";
    }

    /**
     * Returns the following to-do string representation:
     * (T) | description | done status
     */
    @Override
    public String toString() {
        return "(T) | " + (isDone ? "[X] | " : "[ ] | ")
                + this.taskDescription;
    }

    /**
     * Returns the following to-do string representation for saving in the data file.
     */
    public String toSaveFormat() {
        return this.toString();
    }

    /**
     * Updates the task description.
     */
    public void updateDescription(String newTaskDescription) {
        if (newTaskDescription != null && !newTaskDescription.isBlank()) {
            this.taskDescription = newTaskDescription;
        }
    }

    /**
     * Updates the fields of this task according to the provided map.
     * <p>
     * Each subclass defines which keys are supported:
     * <ul>
     *   <li>{@link Todo}: "/desc"</li>
     *   <li>{@link Deadline}: "/desc", "/by"</li>
     *   <li>{@link Event}: "/desc", "/from", "/to"</li>
     * </ul>
     * </p>
     */
    public abstract void update(Map<String, String> fields);
}
