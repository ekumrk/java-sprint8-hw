package Tests;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryHistoryManagerTest {
   private TaskManager manager;
   Task task;
   Task task2;
   Task task3;
   Task task4;
   Task task5;

    @BeforeEach
    void init() throws IOException {
        manager = Managers.getDefault();
        task = new Task("taskTitle", "taskContent");
        task2 = new Task("task2Title", "task2Content");
        task3 = new Task("task3Title", "task3Content");
        task4 = new Task("task4Title", "task4Content");
        task5 = new Task("task5Title", "task5Content");
    }

    @Test
    void getHistoryIfEmpty() {
        List<Task> result = manager.getHistory();
        assertTrue(result.isEmpty());
    }

    @Test
    void getHistoryIfDoubled() throws IOException {
        manager.createNewTask(task);
        manager.createNewTask(task2);

        manager.getTaskFromId(task.getId());
        manager.getTaskFromId(task2.getId());
        manager.getTaskFromId(task.getId());

        List<Task> result = manager.getHistory();
        assertEquals(2, result.size());
    }

    @Test
    void getHistoryIfRemovedFromStartMiddleAndEnd() throws IOException {
        manager.createNewTask(task);
        manager.createNewTask(task2);
        manager.createNewTask(task3);
        manager.createNewTask(task4);
        manager.createNewTask(task5);

        manager.getTaskFromId(1);
        manager.getTaskFromId(2);
        manager.getTaskFromId(3);
        manager.getTaskFromId(4);
        manager.getTaskFromId(5);

        manager.deleteTaskFromId(1);
        manager.deleteTaskFromId(3);
        manager.deleteTaskFromId(5);

        List<Task> expected = List.of(task2,task4);
        List<Task> result = manager.getHistory();
        assertEquals(expected, result);

    }

}
