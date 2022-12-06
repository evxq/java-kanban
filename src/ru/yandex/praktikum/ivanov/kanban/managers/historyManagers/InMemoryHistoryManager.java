package ru.yandex.praktikum.ivanov.kanban.managers.historyManagers;

import ru.yandex.praktikum.ivanov.kanban.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

//    private static final int HISTORY_CAPACITY = 10;
    private HashMap<Integer, Node<Task>> historyMap = new HashMap<>();          // В ключах хранится id задач, а в значениях — узлы связного списка
    private CustomLinkedList<Task> customList = new CustomLinkedList<>();       // двусвязный список для хранения истории_просмотров

    @Override
    public void add(Task task) {
        /*if (historyList.size() > HISTORY_CAPACITY) {           // Проверка размера истории_просмотров
            historyList.remove(0);
        }*/
        // если задача содержится в HashMap, то удалить ее из списка (removeNode), а затем добавить в конец списка (и обновить значение узла)
        // иначе: добавить задачу в конец списка и в HashMap
        if (historyMap.containsKey(task.getId())) {
            customList.removeNode(historyMap.get(task.getId()));
        }
        customList.linkLast(task);
        historyMap.put(task.getId(), customList.last);
    }

    @Override
    public void remove(int id) {                               // <ТЗ-5> удаление задач из истории_просмотра
        customList.removeNode(historyMap.get(id));
    }

    @Override
    public List<Task> getHistory() {        // <ТЗ-5> getHistory перекладывает задачи из связного списка в ArrayList для формирования ответа
        return customList.getTasks();
    }


    public class CustomLinkedList<T> {
        public Node<T> head;
        public Node<T> last;
        private int size = 0;

        public void linkLast(T task) {                         // <ТЗ-5> Добавить задачу в конец двусвязного списка
            final Node<T> oldTail = last;
            final Node<T> newTail = new Node<>(oldTail, task, null);
            last = newTail;
            if (oldTail == null) {
                head = newTail;
            } else {
                oldTail.next = newTail;
            }
            size++;
        }

        public ArrayList<T> getTasks() {                       // <ТЗ-5> собрать все задачи из двусвязного списка в обычный ArrayList
            ArrayList<T> taskArray = new ArrayList<>();
            for (Node<T> x = head; x != null; x = x.next) {
                taskArray.add(x.data);
            }
            return taskArray;
        }

        public void removeNode(Node<T> node) {                 // <ТЗ-5> удаление узла из двусвязного списка
            final T element = node.data;
            final Node<T> next = node.next;
            final Node<T> prev = node.prev;

            if (prev == null) {
                head = next;
            } else {
                prev.next = next;
                node.prev = null;
            }

            if (next == null) {
                last = prev;
            } else {
                next.prev = prev;
                node.next = null;
            }

            node.data = null;
            size--;
        }
    }
}
