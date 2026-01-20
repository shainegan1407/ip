public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "✔" : " "); // mark done task with ✔
    }

    public void markTask() {
        this.isDone = true;
    }

    public void unmarkTask() {
        this.isDone = false;
    }
}
