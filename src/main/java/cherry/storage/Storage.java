package cherry.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import cherry.exception.CherryException;
import cherry.parser.Parser;
import cherry.task.Task;

/**
 * Handles reading to and writing from the storage file on hard disk.
 */
public class Storage {
    private final String path;

    public Storage(String path) {
        this.path = path;
    }

    /**
     * Loads tasks from existing data file, skips corrupted lines.
     * If data file does not exist, returns an empty list.
     */
    public ArrayList<Task> load() throws IOException {
        File file = new File(path);
        ArrayList<Task> tasks = new ArrayList<>();

        if (!file.exists()) {
            return tasks; // return new list
        }

        // load existing list
        Parser parser = new Parser();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = fileReader.readLine()) != null) {
                try {
                    Task task = parser.getTaskFromString(line);
                    tasks.add(task);
                } catch (CherryException e) {
                    System.out.println("Skipped corrupted line: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found at " + path);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Saves tasks to data file.
     * If file does not exist, creates a new directory.
     */
    public void save(ArrayList<Task> tasks) throws IOException {
        File file = new File(path);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("Failed to create directory: " + parentDir.getPath());
            }
        }

        try (FileWriter fileWriter = new FileWriter(path)) {
            for (Task task : tasks) {
                fileWriter.write(task.toSaveFormat() + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new IOException("Failed to save tasks: " + e.getMessage());
        }
    }
}

