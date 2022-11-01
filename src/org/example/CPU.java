package org.example;

import java.util.Optional;

public class CPU {
    private final int WINDOW_SIZE;
    private int workTime = 0;
    private int idleTime = 0;

    private final RAM ram;
    private final TaskProvider taskProvider;
    private Process process = null;

    public CPU(RAM ram, TaskProvider taskProvider, int windowSize) {
        this.ram = ram;
        this.taskProvider = taskProvider;
        this.WINDOW_SIZE = windowSize;
    }

    public CPUReport run(int estimatedTime) {
        initSystem();

        process = ram.getNextProcess();
        idle("SWITCHING");

        while (workTime + idleTime < estimatedTime) {
            Optional<Task> taskOptional = process.getTempTask();

            if (taskOptional.isPresent()) {
                Task task = taskOptional.get();
                if (task.getTaskType() == TaskType.CALCULATION)
                    work(task);
                else
                    idle("BLOCKING & SWITCHING");
            } else {
                unloadProcess(process);
            }

            taskProvider.tryToGenerateProcess();

            process = ram.getNextProcess();
            idle("");
        }

        return new CPUReport(workTime, idleTime);
    }

    private void initSystem() {
        // INIT SYSTEM
        System.out.println("Load OS");
        taskProvider.generateOSProcesses();

        System.out.println("Load SERVICES");
        taskProvider.generateServicesProcesses();
    }

    private void unloadProcess(Process process) {
        idle("UNLOAD");
        ram.removeProcess(process);
    }

    private void work(Task task) {
        printState("CALCULATION");
        task.tick(WINDOW_SIZE);
        ram.updateIO(WINDOW_SIZE);
        workTime += WINDOW_SIZE;
    }

    private void idle(String state) {
        if (!state.isEmpty()) {
            printState(state);
        }
        ram.updateIO(1);
        idleTime++;
    }

    private void printState(String state) {
        System.out.println("*******************************************************************************************");

        System.out.println("pid=" + (process == null ? "NULL" : process.getId()) +
                        ", total=" + (workTime + idleTime) +
                        ", work=" + workTime +
                        ", idle=" + idleTime +
                        ", state=" + state);
        System.out.println(ram.toString());
    }
}
