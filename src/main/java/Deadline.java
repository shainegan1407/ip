public class Deadline extends Task{
    protected StringBuilder deadline;

    public Deadline(String[] tokens) throws CherryException {
        super();
        deadline = new StringBuilder();

        // parse for /by
        int byIndex = -1;
        for (int j = 0; j < tokens.length; j += 1) {
            if ("/by".equals(tokens[j])) {
                byIndex = j;
            }
        }
        if ((byIndex == -1) || (byIndex + 1 >= tokens.length)) {
            throw new CherryException("Please include the deadline using /by");
        }

        // initialise description
        for (int j = 1; j < byIndex; j += 1) {
            description.append(" ").append(tokens[j]);
        }

        // initialise deadline
        for (int j = byIndex + 1; j < tokens.length; j += 1) {
            deadline.append(" ").append(tokens[j]);
        }
    }

    @Override
    public String toString() {
        return  "(D) " + (isDone ? "[✔]" : "[ ]")
                + this.description.toString()
                + " (by:" + this.deadline.toString()
                + ")";
    }
}
