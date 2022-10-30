public class Subtask extends Task {
    private Epic epic;

    public Subtask(String name, String description, String status, Epic epic) {
        super(name, description, status);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return "\n" +
               "Подзадача ID" + this.getId() +
               ". Эпик ID" + epic.getId() +
               ". " + this.getName() +
               ": " + this.getDescription() +
               ". Статус - " + this.getStatus();
    }
}
