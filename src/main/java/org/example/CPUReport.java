package org.example;

public class CPUReport {
    private final int totalTime;
    private final int workTime;
    private final int idleTime;

    public CPUReport(int workTime, int idleTime) {
        this.workTime = workTime;
        this.idleTime = idleTime;
        this.totalTime = workTime + idleTime;
    }

    @Override
    public String toString() {
        return "total: " + (workTime + idleTime) + ", work: " + workTime + ", idle: " + idleTime + "(ticks)";
    }
}
