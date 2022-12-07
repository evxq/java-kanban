package ru.yandex.praktikum.ivanov.kanban.managers.historyManagers;

import ru.yandex.praktikum.ivanov.kanban.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private CustomLinkedList<Task> customList = new CustomLinkedList<>();       // двусвязный список для хранения истории_просмотров

    @Override
    public void add(Task task) {
        // если задача содержится в HashMap, то удалить ее из списка (removeNode), а затем добавить в конец списка (и обновить значение узла)
        // иначе: добавить задачу в конец списка и в HashMap
        if (customList.historyMap.containsKey(task.getId())) {
            customList.removeNode((task.getId()));
        }
        customList.linkLast(task);
        customList.historyMap.put(task.getId(), customList.tail);
    }

    @Override
    public void remove(int id) {                               // <ТЗ-5> удаление задач из истории_просмотра
        customList.removeNode(id);
    }

    @Override
    public List<Task> getHistory() {        // <ТЗ-5> getHistory перекладывает задачи из связного списка в ArrayList для формирования ответа
        return customList.getTasks();
    }


    public class CustomLinkedList<T> {
        private HashMap<Integer, Node<Task>> historyMap = new HashMap<>();          // <ТЗ-5> В ключах хранится id задач, а в значениях — узлы связного списка
        private Node<Task> head;
        private Node<Task> tail;
        private int size = 0;

        public void linkLast(Task task) {                         // <ТЗ-5> Добавить задачу в конец двусвязного списка
            final Node<Task> oldTail = tail;
            final Node<Task> newTail = new Node<Task>(oldTail, task, null);
            tail = newTail;
            if (oldTail == null) {
                head = newTail;
            } else {
                oldTail.next = newTail;
            }
            size++;
        }

        public ArrayList<Task> getTasks() {                       // <ТЗ-5> собрать все задачи из двусвязного списка в обычный ArrayList
            ArrayList<Task> taskArray = new ArrayList<>();
            Node<Task> x = head;
            while (x != null) {
                taskArray.add(x.data);
                x = x.next;
            }
            return taskArray;
        }

        public void removeNode(int id) {                 // <ТЗ-5> удаление узла из двусвязного списка
            Node<Task> node = historyMap.get(id);
            final Node<Task> next = node.next;
            final Node<Task> prev = node.prev;

            if (prev == null) {
                head = next;
            } else {
                prev.next = next;
                node.prev = null;
            }

            if (next == null) {
                tail = prev;
            } else {
                next.prev = prev;
                node.next = null;
            }

            node.data = null;
            size--;
        }
    }
}
