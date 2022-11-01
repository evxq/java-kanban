package main.tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> epicTaskList = new ArrayList<>();

    public Epic(String name, String description, ArrayList<Subtask> epicTaskList) {
        super(name, description);
        this.epicTaskList = epicTaskList;
    }

    public ArrayList<Subtask> getEpicTaskList() {
        return epicTaskList;
    }

    public void checkEpicStatus() {
        int statusNew = 0;
        int statusDone = 0;
        int statusProg = 0;

        for (Subtask subtask : epicTaskList) {
            if (subtask.getStatus().equals("NEW")) {
                statusNew++;
            } else if (subtask.getStatus().equals("DONE")) {
                statusDone++;
            } else if (subtask.getStatus().equals("IN_PROGRESS")) {
                statusProg++;
            }
        }

        if (statusDone == 0 && statusProg == 0) {
            this.setStatus("NEW");
        } else if (statusNew == 0 && statusProg == 0) {
            this.setStatus("DONE");
        } else {
            this.setStatus("IN_PROGRESS");
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "epicTaskList=" + epicTaskList +
                "name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", id=" + this.getId() +
                ", status='" + this.getStatus() + '\'' +
                '}';
    }
}
