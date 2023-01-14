package ru.yandex.praktikum.ivanov.kanban.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private Integer id;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStartTime(LocalDateTime startTime) {         // <ТЗ-7>
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {                       // <ТЗ-7>
        return startTime;
    }

    public Duration getDuration() {                             // <ТЗ-7>
        return duration;
    }

    public void setDuration(Duration duration) {                // <ТЗ-7>
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {                         // <ТЗ-7>
        if (startTime != null) {
            return startTime.plus(duration);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", duration='" + duration + '\'' +
                ", startTime='" + startTime + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(name, otherTask.name) &&
                Objects.equals(description, otherTask.description) &&
                Objects.equals(id, otherTask.id) &&
                Objects.equals(status, otherTask.status) &&
                Objects.equals(duration, otherTask.duration) &&
                Objects.equals(startTime, otherTask.startTime);
    }
}
