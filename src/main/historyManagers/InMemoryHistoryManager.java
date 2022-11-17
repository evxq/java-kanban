package main.historyManagers;

import main.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> historyList = new ArrayList<>();
    private static final int HISTORY_CAPACITY = 10;

    @Override
    public void add(Task task) {
        historyList.add(task);                                 // Добавить в список истории
        if (historyList.size() > HISTORY_CAPACITY) {           // Проверка размера списка истории
            historyList.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
