package org.example;

public class Task {
    private int estimatedTime;
    private TaskType taskType;

    public Task(int estimatedTime, TaskType taskType) {
        this.estimatedTime = estimatedTime;
        this.taskType = taskType;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void tick() {
        this.estimatedTime--;
    }

    public boolean isCompleted() {
        return estimatedTime == 0;
    }
}
