package org.example;

public class Main {
    public static void main(String[] args) {

        RAM ram = new RAM(500);
        TaskProvider taskProvider = new TaskProvider(ram);
        CPU cpu = new CPU(ram, taskProvider);

        CPUReport report = cpu.run(10000);

        System.out.println(report.toString());
    }
}