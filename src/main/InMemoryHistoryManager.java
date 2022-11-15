package main;

import main.HistoryManager;
import main.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    static List<Task> historyList = new ArrayList<>();

    @Override
    public void add(Task task) {
        historyList.add(task);                                 // Добавить в список истории
        if (historyList.size() > 10) {                         // Проверка размера списка истории
            historyList.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
