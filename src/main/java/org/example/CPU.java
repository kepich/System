package org.example;

import java.util.Optional;

public class CPU {
    private int workTime = 0;
    private int idleTime = 0;

    private final RAM ram;
    private final TaskProvider taskProvider;

    public CPU(RAM ram, TaskProvider taskProvider) {
        this.ram = ram;
        this.taskProvider = taskProvider;
    }

    public CPUReport run(int estimatedTime) {
        Process process = null;
        while (workTime + idleTime < estimatedTime) {
            if (process == null) {
                Optional<Process> buf = ram.getNextProcess();

                if (buf.isPresent()) {
                    process = buf.get();
                } else {
                    taskProvider.readNewProcess();
                }

                idle();
            } else {
                Optional<Task> taskOptional = process.getTempTask();

                if (taskOptional.isPresent()) {
                    Task task = taskOptional.get();
                    if (task.getTaskType() == TaskType.CALCULATION) {
                        work(task);
                    } else {
                        switchProcess(process);
                    }
                } else {
                    unloadPrrocess(process);
                }

            }
        }
        return new CPUReport(workTime, idleTime);
    }

    private void unloadPrrocess(Process process) {
        ram.removeProcess(process);
        idle();
    }

    private void switchProcess(Process process) {
        try {
            ram.addProcess(process);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        idle();
    }

    private void work(Task task) {
        task.tick();
        ram.updateIO();
        workTime++;
    }

    private void idle() {
        ram.updateIO();
        idleTime++;
    }
}
