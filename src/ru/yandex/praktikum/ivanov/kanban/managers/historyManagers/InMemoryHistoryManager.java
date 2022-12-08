package ru.yandex.praktikum.ivanov.kanban.managers.historyManagers;

import ru.yandex.praktikum.ivanov.kanban.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private CustomLinkedList<Task> customList = new CustomLinkedList<>();       // двусвязный список для хранения истории_просмотров

    @Override
    public void add(Task task) {                            // <ТЗ-5> удалить задачу из списка (removeNode), а затем добавить в конец списка
        customList.removeNode((task.getId()));
        customList.linkLast(task);
    }

    @Override
    public void remove(int id) {                                           // <ТЗ-5> удаление задач из истории_просмотра
        customList.removeNode(id);
    }

    @Override
    public List<Task> getHistory() {        // <ТЗ-5> getHistory перекладывает задачи из связного списка в ArrayList для формирования ответа
        return customList.getTasks();
    }


    public class CustomLinkedList<T> {
        private HashMap<Integer, Node<T>> historyMap = new HashMap<>();          // <ТЗ-5> В ключах хранится id задач, а в значениях — узлы связного списка
        private Node<T> head;
        private Node<T> tail;

        public void linkLast(T task) {                              // <ТЗ-5> Добавить задачу в конец двусвязного списка
            final Node<T> oldTail = tail;
            final Node<T> newTail = new Node<T>(oldTail, task, null);
            tail = newTail;
            if (oldTail == null) {
                head = newTail;
            } else {
                oldTail.setNext(newTail);
            }

            if (task instanceof Task) {
                Integer id = ((Task) task).getId();
                historyMap.put(id, tail);
            } else {
                System.out.println("Некорректный тип задачи");
            }
        }

        public ArrayList<T> getTasks() {                       // <ТЗ-5> собрать все задачи из двусвязного списка в обычный ArrayList
            ArrayList<T> taskArray = new ArrayList<>();
            Node<T> element = head;
            while (element != null) {
                taskArray.add(element.getData());
                element = element.getNext();
            }
            return taskArray;
        }

        public void removeNode(int id) {                                   // <ТЗ-5> удаление узла из двусвязного списка
            if (historyMap.get(id) == null) {
                return;
            }

            Node<T> node = historyMap.get(id);
            final Node<T> next = node.getNext();
            final Node<T> prev = node.getPrev();

            if (prev == null) {
                head = next;
            } else {
                prev.setNext(next);
                node.setPrev(null);
            }

            if (next == null) {
                tail = prev;
            } else {
                next.setPrev(prev);
                node.setNext(null);
            }
        }
    }
}
