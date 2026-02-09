package cherry.task;

import java.util.ArrayList;

import cherry.exception.CherryException;

/**
 * Represents a collection of tasks.
 * Provides methods to add, delete, mark, and unmark tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates task list from an existing list of tasks.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates task list from an existing list of tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Returns a task at the given task number (1-based for user).
     */
    public Task getTask(int taskNumber) throws CherryException {
        if (taskNumber <= 0 || taskNumber > tasks.size()) {
            throw new CherryException("This task does not exist");
        }

        Task task = tasks.get(taskNumber - 1);
        assert task != null : "Retrieved task should not be null";
        return task;
    }

    /**
     * Returns a task at the given task number (1-based for user).
     */
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the bottom of the list.
     */
    public void addTask(Task task) {
        assert task != null : "Task to add should not be null";
        tasks.add(task);
        assert tasks.contains(task) : "Task should be in list after adding";
    }

    /**
     * Deletes a task at the given task number (1-based for user).
     */
    public void deleteTask(int taskNumber) {
        assert taskNumber >= 0 && taskNumber < tasks.size() : "Task index should be valid";
        tasks.remove(taskNumber);
    }

    /**
     * Marks a task as done at the given task number (1-based for user).
     */
    public void markTask(int taskNumber) throws CherryException {
        getTask(taskNumber).markTask();
    }

    /**
     * Unmarks a task as done at the given task number (1-based for user).
     */

    public void unmarkTask(int taskNumber) throws CherryException {
        getTask(taskNumber).unmarkTask();
    }

    /**
     * Returns the size of the task list.
     */
    public int getTaskCount() {
        return tasks.size();
    }
}
