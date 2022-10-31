package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Process {
    private final List<Task> tasks = new ArrayList<>();
    private final int size;
    private final int id;
    private final ProcessLayer processLayer;

    public Process(int size, int id, ProcessLayer processLayer) {
        this.size = size;
        this.id = id;
        this.processLayer = processLayer;
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
            return "{id=" + id + ": \"" + "COMPLETED" + "\", " + size + "Mb, " + processLayer.name() + "}";
        } else {
            return "{id=" + id + ": \"" + tasks.get(0).getTaskType() + "\", (estimatedTime: " + tasks.get(0).getEstimatedTime() + "), " + size +"Mb, " + processLayer.name() + "}";
        }
    }

    public ProcessLayer getProcessLayer() {
        return processLayer;
    }
}
