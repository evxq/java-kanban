package ru.yandex.praktikum.ivanov.kanban.tasks;

public class Subtask extends Task {
    private Epic epic;
    public String subtaskType;
    private int epicId;

    public Subtask(String name, String description, Status status, Epic epic) {
        super(name, description, status);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    public void setSubtasksEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getSubtasksEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicID='" + epicId + '\'' +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", id=" + this.getId() +
                ", status='" + this.getStatus() + '\'' +
                ", duration='" + this.getDuration() + '\'' +
                ", startTime='" + this.getStartTime() + '\'' +
                '}';
    }
}
