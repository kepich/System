package org.example;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

public class RAM {
    private final int size;
    private final Queue<Process> processes = new ArrayDeque<>();

    public RAM(int size) {
        this.size = size;
    }

    public void removeProcess(Process process) {
        processes.remove(process);
    }

    public Optional<Process> getNextProcess() {
        if (processes.isEmpty()) {
            return Optional.empty();
        } else {
            processes.add(processes.remove());
            return Optional.of(processes.peek());
        }
    }

    public void addProcess(Process process) throws Exception {
        if (isEnoughMemory(process.getSize())) {
            processes.add(process);
        } else {
            throw new Exception("Not enough memory");
        }
    }

    private boolean isEnoughMemory(int required) {
        return size - processes.stream().map(Process::getSize).reduce(0, Integer::sum) - required >= 0;
    }

    public void updateIO(int time) {
        processes.forEach(process -> process.getTempTask().ifPresent(task -> {
            if (task.getTaskType() == TaskType.IO)
                task.tick(time);
        }));
    }

    @Override
    public String toString() {
        String res = "";
        for (Process process: processes) {
            res += process.toString() + "\n";
        }
        return res;
    }
}
