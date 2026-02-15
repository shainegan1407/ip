package cherry.task;

import java.util.Map;

/**
 * Represents a task that occurs at a specific time or date.
 * Extends {@link Task} by including the event's start and end information.
 */
public class Event extends Task {
    protected String eventStart;
    protected String eventEnd;

    /**
     * Creates an event task with the given description, start and end, unmarked.
     */
    public Event(String taskDescription, String eventStart, String eventEnd) {
        super(taskDescription);
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
    }

    /**
     * Creates a deadline task with the given description, done status, start and end.
     */
    public Event(String taskDescription, boolean isDone, String eventStart, String eventEnd) {
        super(taskDescription, isDone);
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
    }
    @Override
    public void update(Map<String, String> fields) {
        updateDescription(fields.get("desc"));
        if (fields.containsKey("from")) {
            this.eventStart = fields.get("from");
        }
        if (fields.containsKey("to")) {
            this.eventEnd = fields.get("to");
        }
    }

    /**
     * Returns the following event string representation:
     * (E) | taskDescription | done status | start | end
     */
    @Override
    public String toString() {
        return "(E) " + (isDone ? "[X] | " : "[ ] | ")
                + this.taskDescription
                + " (from: " + this.eventStart
                + " to: " + this.eventEnd
                + ")";
    }

    /**
     * Returns the following to-do string representation for saving in the data file.
     */
    public String toSaveFormat() {
        return "(E) | " + (isDone ? "[X] | " : "[ ] | ")
                + this.taskDescription
                + " | " + this.eventStart
                + " | " + this.eventEnd;
    }
}
