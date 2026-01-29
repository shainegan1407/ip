package cherry.task;

public class Event extends Task {
    protected String start;
    protected String end;

    /**
     * Creates an event task with the given description, start and end, unmarked.
     */
    public Event(String description, String start, String end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Creates a deadline task with the given description, done status, start and end.
     */
    public Event(String description, boolean isDone, String start, String end) {
        super(description, isDone);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the following event string representation:
     * (E) | description | done status | start | end
     */
    @Override
    public String toString() {
        return "(E) | " + (isDone ? "[✔] | " : "[ ] | ")
                + this.description
                + " (from: " + this.start
                + " to: " + this.end
                + ")";
    }

    /**
     * Returns the following to-do string representation for saving in the data file.
     */
    public String toSaveFormat() {
        return "(E) | " + (isDone ? "[✔] | " : "[ ] | ")
                + this.description
                + " | " + this.start
                + " | " + this.end;
    }
}
