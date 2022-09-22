package org.example;

import java.util.Random;

public class TaskProvider {

    private final int MIN_TASKS = 5;
    private final int MAX_TASKS = 15;

    private final int MIN_PROC_SIZE = 50;
    private final int MAX_PROC_SIZE = 500;

    private final int IO_CHANCE = 40;

    private final int MIN_IO_EST = 50;
    private final int MAX_IO_EST = 80;

    private final int MIN_CALC_EST = 60;
    private final int MAX_CALC_EST = 100;

    private final RAM ram;
    private final Random random = new Random();
    private int id = 0;
    private Process lastGenerated;


    public TaskProvider(RAM ram) {
        this.ram = ram;
    }

    public void readNewProcess() {
        if (lastGenerated == null) {
            Process process = generateProcess();
            addProcessToRam(process);
        } else {
            addProcessToRam(lastGenerated);
        }
    }

    private void addProcessToRam(Process process) {
        try {
            ram.addProcess(process);
            lastGenerated = null;
        } catch (Exception exception) {
            System.out.println("Cannot load new task! " + exception.getMessage());
            lastGenerated = process;
        }
    }

    private Process generateProcess() {
        Process process = new Process(random.nextInt(MAX_PROC_SIZE - MIN_PROC_SIZE) + MIN_PROC_SIZE, id++);

        for(int i = 0; i < random.nextInt(MAX_TASKS - MIN_TASKS) + MIN_TASKS; i++) {
            process.addTask(generateTask());
        }
        return process;
    }

    private Task generateTask() {
        if (random.nextInt(100) < IO_CHANCE) {
            return new Task(random.nextInt(MAX_IO_EST - MIN_IO_EST) + MIN_IO_EST, TaskType.IO);
        } else {
            return new Task(random.nextInt(MAX_CALC_EST - MIN_CALC_EST) + MIN_CALC_EST, TaskType.CALCULATION);
        }
    }
}
