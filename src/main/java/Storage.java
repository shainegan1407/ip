import java.io.*;
import java.util.ArrayList;

public class Storage {
    private final String path;

    public Storage(String path) {
        this.path = path;
    }

    public ArrayList<Task> load() throws IOException {
        File file = new File(path);
        ArrayList<Task> tasks = new ArrayList<>();

        if (!file.exists()) {
            return tasks; // return new list
        }

        // load existing list
        Parser parser = new Parser();
        BufferedReader fileReader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = fileReader.readLine()) != null) {
            Task task = parser.getTaskFromString(line);
            if (task != null) {
                tasks.add(task);
            }
        }
        fileReader.close();
        return tasks;
    }

    public void save(ArrayList<Task> tasks) throws IOException{
        File file = new File(path);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        FileWriter fileWriter = new FileWriter(path);
        for (Task task : tasks) {
            fileWriter.write(task.toString() + System.lineSeparator());
        }
        fileWriter.close();
    }
}

