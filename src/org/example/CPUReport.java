package org.example;

public class CPUReport {
    private final int workTime;
    private final int idleTime;

    public CPUReport(int workTime, int idleTime) {
        this.workTime = workTime;
        this.idleTime = idleTime;
    }

    @Override
    public String toString() {
        return "### CPU usage report: " +
                "total=" + (workTime + idleTime) + "[ticks]" +
                ", work=" + workTime + "[ticks]" + "(" + ((float)workTime / (workTime + idleTime) * 100) + "%)" +
                ", idle=" + idleTime + "[ticks]" + "(" + ((float)idleTime / (workTime + idleTime) * 100) + "%)";
    }
}
