package ru.yandex.praktikum.ivanov.kanban.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Subtask> epicTaskList;
    private LocalDateTime endTime;
    private ArrayList<Integer> epicIDTaskList = new ArrayList<>();
    public String epicType;

    public Epic(String name, String description, ArrayList<Subtask> epicTaskList) {
        super(name, description);
        this.epicTaskList = epicTaskList;
    }

    public ArrayList<Subtask> getEpicTaskList() {
        return epicTaskList;
    }

    public ArrayList<Integer> getEpicIDTaskList() {
        return epicIDTaskList;
    }

    public void SetSubtaskToEpic(Subtask subtask) {
        epicTaskList.add(subtask);
    }

    public void checkEpicStatus() {
        int statusNew = 0;
        int statusDone = 0;
        int statusProg = 0;

        for (Subtask subtask : epicTaskList) {
            if (subtask.getStatus().equals(Status.NEW)) {
                statusNew++;
            } else if (subtask.getStatus().equals(Status.DONE)) {
                statusDone++;
            } else if (subtask.getStatus().equals(Status.IN_PROGRESS)) {
                statusProg++;
            }
        }

        if (statusDone == 0 && statusProg == 0) {
            this.setStatus(Status.NEW);
        } else if (statusNew == 0 && statusProg == 0) {
            this.setStatus(Status.DONE);
        } else {
            this.setStatus(Status.IN_PROGRESS);
        }
    }

    public void setEndTime(LocalDateTime endTime) {                 // <ТЗ-7>
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {                             // <ТЗ-7>
        return endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "epicTaskList=" + epicTaskList +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", id=" + this.getId() +
                ", status='" + this.getStatus() + '\'' +
                ", duration='" + this.getDuration() + '\'' +
                ", startTime='" + this.getStartTime() + '\'' +
                '}';
    }
}
