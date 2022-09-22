package org.example;

import java.util.Optional;

public class CPU {
    private int workTime = 0;
    private int idleTime = 0;

    private final RAM ram;
    private final TaskProvider taskProvider;
    private Process process = null;

    public CPU(RAM ram, TaskProvider taskProvider) {
        this.ram = ram;
        this.taskProvider = taskProvider;
    }

    public CPUReport run(int estimatedTime) {
        while (workTime + idleTime < estimatedTime) {
            if (process == null) {
                Optional<Process> buf = ram.getNextProcess();

                if (buf.isPresent()) {
                    // Переключение на готовый процесс
                    idle("SWITCHING");
                    process = buf.get();
                } else {
                    tryToLoadNewProcess();
                }
            } else {
                Optional<Task> taskOptional = process.getTempTask();

                if (taskOptional.isPresent()) {
                    Task task = taskOptional.get();
                    if (task.getTaskType() == TaskType.CALCULATION) {
                        work(task);
                    } else {
                        idle("BLOCKING");
                        tryToLoadNewProcess();
                        process = null;
                    }
                } else {
                    unloadProcess();
                    process = null;
                }
            }
        }
        return new CPUReport(workTime, idleTime);
    }

    private void tryToLoadNewProcess() {
        idle("LOAD");
        taskProvider.readNewProcess();
    }

    private void unloadProcess() {
        idle("UNLOAD");
        ram.removeProcess(process);
    }

    private void work(Task task) {
        printState("CALCULATION");
        int taskTime = task.tick();
        ram.updateIO(taskTime);
        workTime += taskTime;
    }

    private void idle(String state) {
        printState(state);
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
