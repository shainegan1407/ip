public class Task {
    protected StringBuilder description;
    protected boolean isDone;

    public Task() {
        this.description = new StringBuilder();
        this.isDone = false;
    }

    public Task(String[] tokens) throws CherryException {
        if (tokens.length <= 1) {
            throw new CherryException("Please let me know the task description");
        }
        this.description = new StringBuilder();
        for (int i = 1; i < tokens.length; i += 1){
            description.append(" ").append(tokens[i]);
        }
        this.isDone = false;
    }

    public void markTask() {
        this.isDone = true;
    }

    public void unmarkTask() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        return  "(T) " + (isDone ? "[✔]" : "[ ]")
                + this.description.toString();
    }
}
