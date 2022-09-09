package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Process {
    private List<Task> tasks = new ArrayList<>();
    private final int size;

    public Process(List<Task> tasks, int size) {
        this.tasks = tasks;
        this.size = size;
    }

    public Process(int size) {
        this.size = size;
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
}
