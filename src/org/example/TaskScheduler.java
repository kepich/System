package org.example;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Optional;
import java.util.Queue;

import static org.example.ProcessLayer.IDLE;
import static org.example.ProcessLayer.OS;
import static org.example.ProcessLayer.SERVICE;
import static org.example.ProcessLayer.USER;

public class TaskScheduler {
    private final Process idleProcess = new Process(1, -99, ProcessLayer.IDLE);
    private final Queue<Process> userLayer = new ArrayDeque<>();
    private final Queue<Process> serviceLayer = new ArrayDeque<>();
    private final Queue<Process> coreLayer = new ArrayDeque<>();


    public void remove(Process process) {
        switch (process.getProcessLayer()) {
            case SERVICE -> serviceLayer.remove(process);
            case USER -> userLayer.remove(process);
            case IDLE -> {}
            default -> System.out.println("Invalid operation! Cannot UNLOAD process: " + process);
        }
    }

    public void add(Process process) {
        switch (process.getProcessLayer()) {
            case SERVICE -> serviceLayer.add(process);
            case USER -> userLayer.add(process);
            case OS -> coreLayer.add(process);
            default -> System.out.println("Invalid operation! Cannot LOAD process: " + process);
        }
    }

    public int getUsedMemorySize() {
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
                printProcessList(serviceLayer, SERVICE.name()) +
                printProcessList(userLayer, USER.name()) +
                IDLE.name() + "\n" + printProcess(idleProcess);
    }

    public String printProcessList(Queue<Process> processLayer, String layerName) {
        String res = layerName + "\n";

        for (Process process : processLayer.stream().sorted(Comparator.comparingInt(Process::getId)).toList()) {
            res += printProcess(process);
        }
        return res;
    }

    private String printProcess(Process process) {
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

            if (process.getTempTask().isEmpty() || process.getTempTask().get().getTaskType() != TaskType.IO) {
                return Optional.of(process);
            }
        }
        return Optional.empty();
    }

    public Process getIdle() {
        return idleProcess;
    }

    public boolean isServiceLoaded(int serviceId) {
        return serviceLayer.stream().anyMatch(proc -> proc.getId() == serviceId);
    }
}