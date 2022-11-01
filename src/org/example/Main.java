package org.example;

public class Main {
    public static void main(String[] args) {

        TaskScheduler taskScheduler = new TaskScheduler();
        RAM ram = new RAM(2000, taskScheduler);
        TaskProvider taskProvider = new TaskProvider(ram);
        CPU cpu = new CPU(ram, taskProvider, 5);

        CPUReport report = cpu.run(500);

        System.out.println(report.toString());
    }
}