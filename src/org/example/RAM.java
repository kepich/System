package org.example;

public class RAM {
    private final int size;
    private final TaskScheduler taskScheduler;

    public RAM(int size, TaskScheduler taskScheduler) {
        this.size = size;
        this.taskScheduler = taskScheduler;
    }

    public void removeProcess(Process process) {
        taskScheduler.remove(process);
    }

    public Process getNextProcess() {
        return taskScheduler.getReadyProcess().orElse(taskScheduler.getIdle());
    }

    public void addProcess(Process process) throws Exception {
        if (isEnoughMemory(process.getSize())) {
            taskScheduler.add(process);
        } else {
            throw new Exception("Not enough memory");
        }
    }

    public boolean isServiceLoaded(int id) {
        return taskScheduler.isServiceLoaded(id);
    }

    private boolean isEnoughMemory(int required) {
        return size - taskScheduler.getUsedMemorySize() - required >= 0;
    }

    public void updateIO(int time) {
        taskScheduler.updateIO(time);
    }

    @Override
    public String toString() {
        return taskScheduler.toString();
    }
}
