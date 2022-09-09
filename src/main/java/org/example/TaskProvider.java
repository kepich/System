package org.example;

import java.util.Random;

public class TaskProvider {

    private final RAM ram;
    private final Random random = new Random();


    public TaskProvider(RAM ram) {
        this.ram = ram;
    }

    public void readNewProcess() {
        Process process = generateProcess();
        addProcessToRam(process);
    }

    private void addProcessToRam(Process process) {
        try {
            ram.addProcess(process);
        } catch (Exception exception) {
            System.out.println("Cannot load new task! " + exception.getMessage());
        }
    }

    private Process generateProcess() {
        Process process = new Process(random.nextInt(300));

        for(int i = 0; i < random.nextInt(5); i++) {
            process.addTask(generateTask());
        }
        return process;
    }

    private Task generateTask() {
        return new Task(random.nextInt(100), TaskType.values()[random.nextInt(TaskType.values().length)]);
    }
}
