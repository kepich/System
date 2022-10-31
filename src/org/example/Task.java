package org.example;

import static java.lang.Math.min;

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

    public int tick() {
        int res = this.estimatedTime;
        estimatedTime = 0;
        return res;
    }

    public void tick(int time) {
        estimatedTime -= min(estimatedTime, time);
    }

    public boolean isCompleted() {
        return estimatedTime == 0;
    }
}
