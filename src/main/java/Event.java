public class Event extends Task {
    protected StringBuilder start;
    protected StringBuilder end;

    public Event(String[] tokens) {
        super();
        start = new StringBuilder();
        end  = new StringBuilder();

        // parse for /from
        int fromIndex = -1;
        for (int j = 0; j < tokens.length; j += 1) {
            if ("/from".equals(tokens[j])) {
                fromIndex = j;
            }
        }

        // parse for /to
        int toIndex = -1;
        for (int j = 0; j < tokens.length; j += 1) {
            if ("/to".equals(tokens[j])) {
                toIndex = j;
            }
        }

        if ((fromIndex == -1) || (toIndex == -1) || (fromIndex >= toIndex)) {
            throw new IllegalArgumentException("Incorrect command");
        }

        // initialise description
        for (int j = 1; j < fromIndex; j += 1) {
            description.append(" ").append(tokens[j]);
        }

        // initialise start
        for (int j = fromIndex + 1; j < toIndex; j += 1) {
            start.append(" ").append(tokens[j]);
        }

        // initialise end
        for (int j = toIndex + 1; j < tokens.length; j += 1) {
            end.append(" ").append(tokens[j]);
        }
    }

    @Override
    public String toString() {
        return  "(E) " + (isDone ? "[✔]" : "[ ]")
                + this.description.toString()
                + " (from:" + start.toString()
                + " to:" + end.toString()
                + ")";
    }
}
