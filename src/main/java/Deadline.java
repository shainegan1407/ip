public class Deadline extends Task{
    protected String deadline;

    /**
     * Creates a deadline task with the given description and deadline, unmarked.
     */
    public Deadline(String description, String deadline) {
        super(description);
        this.deadline = deadline;
    }

    /**
     * Creates a deadline task with the given description, done status and deadline.
     */
    public Deadline(String description, boolean isDone, String deadline) {
        super(description, isDone);
        this.deadline = deadline;
    }

    /**
     * Returns the following deadline string representation:
     * (D) | description | done status | deadline
     */
    @Override
    public String toString() {
        return  "(D) | " + (isDone ? "[✔] |" : "[ ] | ")
                + this.description
                + " | by:" + this.deadline;
    }
}
