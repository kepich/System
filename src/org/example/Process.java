package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Process {
    private List<Task> tasks = new ArrayList<>();
    private final int size;
    private final int id;

    public Process(List<Task> tasks, int size, int id) {
        this.tasks = tasks;
        this.size = size;
        this.id = id;
    }

    public Process(int size, int id) {
        this.size = size;
        this.id = id;
    }

    public Optional<Task> getTempTask() {
        if (tasks.isEmpty()) {
            return Optional.empty();
        } else {
            Task task = this.tasks.get(0);
            if (task.isCompleted()) {
                this.tasks.remove(task);
                return getTempTask();
            } else {
                return Optional.of(task);
            }
        }
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public int getSize() {
        return size;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        if (this.tasks.isEmpty()) {
            return "{id=" + id + ": \"" + "COMPLETED" + "\", " + size + "Mb}";
        } else {
            return "{id=" + id + ": \"" + this.tasks.get(0).getTaskType() + "\", (estimatedTime: " + this.tasks.get(0).getEstimatedTime() + "), " + size +"Mb}";
        }
    }
}
