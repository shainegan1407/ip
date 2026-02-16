package cherry.task;

import java.util.Map;

/**
 * Represents a generic task which has a description and done status.
 */
public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }
    /**
     * Creates a to-do task with the given description.
     */
    public Todo(String description, boolean isDone) {
        super(description, isDone);
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

    @Override
    public void update(Map<String, String> fields) {
        updateDescription(fields.get("desc"));
    }
}

