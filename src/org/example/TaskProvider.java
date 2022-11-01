package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaskProvider {

    private final int MIN_TASKS = 3;
    private final int MAX_TASKS = 7;

    private final int MIN_PROC_SIZE = 50;
    private final int MAX_PROC_SIZE = 500;

    private final int IO_CHANCE = 40;

    private final int MIN_IO_EST = 20;
    private final int MAX_IO_EST = 40;

    private final int MIN_CALC_EST = 10;
    private final int MAX_CALC_EST = 50;

    private final RAM ram;
    private final List<Process> services = new ArrayList<>();

    private int nextProcessId = 0;
    private Process lastGenerated;

    private final Random random = new Random();


    public TaskProvider(RAM ram) {
        this.ram = ram;
    }

    public void tryToGenerateProcess() {
        if (checkChance(10)) {
            if (checkChance(50)) {
                Process process = services.get(random.nextInt(services.size()));
                if(!ram.isServiceLoaded(process.getId())) {
                    generateTasksForService(process);
                    addProcessToRam(process);
                }
            } else {
                if (lastGenerated == null) {
                    Process process = generateProcess();
                    addProcessToRam(process);
                } else {
                    addProcessToRam(lastGenerated);
                }
            }
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
        Process process = new Process(random.nextInt(MAX_PROC_SIZE - MIN_PROC_SIZE) + MIN_PROC_SIZE, nextProcessId++, ProcessLayer.USER);

        for(int i = 0; i < random.nextInt(MAX_TASKS - MIN_TASKS) + MIN_TASKS; i++) {
            process.addTask(generateTask(IO_CHANCE, MIN_IO_EST, MAX_IO_EST, MIN_CALC_EST, MAX_CALC_EST));
        }
        return process;
    }

    public void generateOSProcesses() {
        int ioChance = 80;
        int minIOEst = 60;
        int maxIOEst = 70;
        int minCalcEst = 20;
        int maxCalcEst = 40;

        int numOfProcesses = 2;

        Process process;
        for (int n = 0; n < numOfProcesses; n++) {
            process = new Process(random.nextInt(100) + 100, -1 - n, ProcessLayer.OS);
            for(int i = 0; i < 300; i++) {
                process.addTask(generateTask(ioChance, minIOEst, maxIOEst, minCalcEst, maxCalcEst));
            }
            addProcessToRam(process);
        }
    }

    public void generateServicesProcesses() {
        int numOfProcesses = 3;

        Process process;
        for (int n = 0; n < numOfProcesses; n++) {
            process = new Process(random.nextInt(100) + 200, -100 - n, ProcessLayer.SERVICE);
            generateTasksForService(process);
            services.add(process);
            addProcessToRam(process);
        }
    }

    private void generateTasksForService(Process process) {
        int ioChance = 30;
        int minIOEst = 10;
        int maxIOEst = 30;
        int minCalcEst = 20;
        int maxCalcEst = 40;

        for(int i = 0; i < (random.nextInt(MAX_TASKS - MIN_TASKS) + MIN_TASKS) / 2; i++) {
            process.addTask(generateTask(ioChance, minIOEst, maxIOEst, minCalcEst, maxCalcEst));
        }
    }

    private boolean checkChance(int prob) {
        return random.nextInt(100) < prob;
    }

    private Task generateTask(int ioChance, int minIOEst, int maxIOEst, int minCalcEst, int maxCalcEst) {
        if (checkChance(ioChance)) {
            return new Task(random.nextInt(maxIOEst - minIOEst) + minIOEst, TaskType.IO);
        } else {
            return new Task(random.nextInt(maxCalcEst - minCalcEst) + minCalcEst, TaskType.CALCULATION);
        }
    }
}
