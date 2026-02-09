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

    @Override
    public void update(Map<String, String> fields) {
        updateDescription(fields.get("desc"));
    }
}

