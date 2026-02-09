import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import cherry.exception.CherryException;
import cherry.task.Task;
import cherry.task.TaskList;

public class TaskListTest {

    @Test
    public void constructor_emptyList_success() {
        TaskList taskList = new TaskList();
        assertEquals(0, taskList.getTaskCount());
    }

    @Test
    public void constructor_existingList_success() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Task("task 1"));
        tasks.add(new Task("task 2"));

        TaskList taskList = new TaskList(tasks);
        assertEquals(2, taskList.getTaskCount());
    }

    @Test
    public void addTask_singleTask_success() {
        TaskList taskList = new TaskList();
        Task task = new Task("read book");
        taskList.addTask(task);

        assertEquals(1, taskList.getTaskCount());
    }

    @Test
    public void addTask_multipleTasks_success() {
        TaskList taskList = new TaskList();
        taskList.addTask(new Task("task 1"));
        taskList.addTask(new Task("task 2"));
        taskList.addTask(new Task("task 3"));

        assertEquals(3, taskList.getTaskCount());
    }

    @Test
    public void getTask_invalidIndexTooLarge_exceptionThrown() {
        TaskList taskList = new TaskList();
        taskList.addTask(new Task("task 1"));

        try {
            taskList.getTask(5);
            fail();
        } catch (CherryException e) {
            assertEquals("This task does not exist, did you mean something else?", e.getMessage());
        }
    }

    @Test
    public void getTask_invalidIndexNegative_exceptionThrown() {
        TaskList taskList = new TaskList();
        taskList.addTask(new Task("task 1"));

        try {
            taskList.getTask(-1);
            fail();
        } catch (CherryException e) {
            assertEquals("This task does not exist, did you mean something else?", e.getMessage());
        }
    }

    @Test
    public void getTaskCount_emptyList_returnsZero() {
        TaskList taskList = new TaskList();
        assertEquals(0, taskList.getTaskCount());
    }

    @Test
    public void getTaskCount_afterOperations_correctCount() {
        TaskList taskList = new TaskList();
        assertEquals(0, taskList.getTaskCount());

        taskList.addTask(new Task("task 1"));
        assertEquals(1, taskList.getTaskCount());

        taskList.addTask(new Task("task 2"));
        assertEquals(2, taskList.getTaskCount());

        taskList.deleteTask(0);
        assertEquals(1, taskList.getTaskCount());
    }
}
