package org.example;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

public class RAM {
    private final int size;
    private final Queue<Process> processes = new ArrayDeque<>();
    private final TaskScheduler taskScheduler = new TaskScheduler();

    public RAM(int size) {
        this.size = size;
    }

    public void removeProcess(Process process) {
        taskScheduler.remove(process);
    }

    public Process getNextProcess() {
        return taskScheduler.getReadyProcess().orElse(taskScheduler.getIdle());
    }

    public void addProcess(Process process) throws Exception {
        if (isEnoughMemory(process.getSize())) {
            taskScheduler.add(process);
        } else {
            throw new Exception("Not enough memory");
        }
    }

    private boolean isEnoughMemory(int required) {
        return size - taskScheduler.memoryLoaded() - required >= 0;
    }

    public void updateIO(int time) {
        taskScheduler.updateIO(time);
    }

    @Override
    public String toString() {
        return taskScheduler.toString();
    }
}
