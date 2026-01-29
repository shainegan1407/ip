package cherry.task;

import cherry.*;

import java.util.ArrayList;

public class TaskList {
    ArrayList<Task> tasks;

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
            throw new CherryException("This task does not exist, did you mean something else?");
        }
        return tasks.get(taskNumber - 1);
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
    public void addTask(Task task){
        tasks.add(task);
    }

    /**
     * Deletes a task at the given task number (1-based for user).
     */
    public void deleteTask(int taskNumber) {
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
    public int getTaskCount(){
        return tasks.size();
    }
}
