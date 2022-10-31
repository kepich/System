package org.example;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

import static org.example.ProcessLayer.IDLE;
import static org.example.ProcessLayer.OS;

public class TaskScheduler {
    private final Process idleProcess = new Process(1, -1, ProcessLayer.IDLE);
    private final Queue<Process> userLayer = new ArrayDeque<>();
    private final Queue<Process> serviceLayer = new ArrayDeque<>();
    private final Queue<Process> coreLayer = new ArrayDeque<>();


    public void remove(Process process) {
        switch (process.getProcessLayer()) {
            case SERVICE -> serviceLayer.remove(process);
            case USER -> userLayer.remove(process);
            default -> System.out.println("Invalid operation! Cannot UNLOAD process: " + process);
        }
    }

    public void add(Process process) {
        switch (process.getProcessLayer()) {
            case SERVICE -> serviceLayer.remove(process);
            case USER -> userLayer.remove(process);
            case OS -> coreLayer.remove(process);
            default -> System.out.println("Invalid operation! Cannot LOAD process: " + process);
        }
    }

    public int memoryLoaded() {
        return userLayer.stream().map(Process::getSize).reduce(0, Integer::sum) +
                serviceLayer.stream().map(Process::getSize).reduce(0, Integer::sum) +
                coreLayer.stream().map(Process::getSize).reduce(0, Integer::sum) +
                idleProcess.getSize();
    }

    public void updateIO(int time) {
        updateIO(userLayer, time);
        updateIO(serviceLayer, time);
        updateIO(coreLayer, time);
        updateIO(idleProcess, time);
    }

    private void updateIO(Queue<Process> processes, int time) {
        processes.forEach(process -> updateIO(process, time));
    }

    private void updateIO(Process process, int time) {
        process.getTempTask().ifPresent(task -> {
            if (task.getTaskType() == TaskType.IO)
                task.tick(time);
        });
    }

    @Override
    public String toString() {
        return printProcessList(coreLayer, OS.name()) +
                printProcessList(serviceLayer, OS.name()) +
                printProcessList(userLayer, OS.name()) +
                IDLE.name() + "\n" + printProcess(idleProcess);
    }

    public String printProcessList(Queue<Process> userLayer, String layerName) {
        String res = layerName + "\n";
        for (Process process : userLayer) {
            res += printProcess(process);
        }
        return res;
    }

    public String printProcess(Process process) {
        return "\t" + process.toString() + "\n";
    }

    public Optional<Process> getReadyProcess() {
        return getNonBlockedProcess(coreLayer)
                .or(() -> getNonBlockedProcess(serviceLayer))
                .or(() -> getNonBlockedProcess(userLayer));
    }

    private Optional<Process> getNonBlockedProcess(Queue<Process> processes) {
        int processesSize = processes.size();

        for (int i = 0; i < processesSize; i++) {
            Process process = processes.remove();
            processes.add(process);

            if (process.getTempTask().isPresent() && process.getTempTask().get().getTaskType() != TaskType.IO) {
                return Optional.of(process);
            }
        }
        return  Optional.empty();
    }

    public Process getIdle() {
        return idleProcess;
    }
}