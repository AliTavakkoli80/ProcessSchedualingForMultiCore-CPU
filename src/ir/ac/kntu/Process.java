package ir.ac.kntu;


public class Process implements Runnable,Comparable<Process> {
    private final int pid;
    private final int arrivalTime;
    private final int CPUBurst;
    private final int coreNeeded;
    private long startTime;
    private boolean isRunning;
    private boolean isReady;
    private boolean isDone;


    public Process(int arrivalTime, int CPUBurst, int coreNeeded, int pid) {
        this.startTime = -1;
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.CPUBurst = CPUBurst;
        this.coreNeeded = coreNeeded;
        this.isRunning = false;
        this.isReady = false;
        this.isDone = false;
    }

    public int getPid() {
        return pid;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getCPUBurst() {
        return CPUBurst;
    }

    public int getCoreNeeded() {
        return coreNeeded;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "Process{" +
                "pid=" + pid +
                ", arrivalTime=" + arrivalTime +
                ", CPUBurst=" + CPUBurst +
                ", coreNeeded=" + coreNeeded +
                ", startTime=" + startTime +
                ", isRunning=" + isRunning +
                ", isReady=" + isReady +
                ", isDone=" + isDone +
                "}\n";
    }

    @Override
    public int compareTo(Process o) {
        return Integer.compare(getArrivalTime(), o.getArrivalTime());
    }

    @Override
    public void run() {
        try {
            Thread.sleep(getCPUBurst()*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int j = 0; j < Timing.cpu.getNumCores(); j++) {
            if (Timing.cpu.getN().get(j).isBusy() && Timing.cpu.getN().get(j).getPid() == this.getPid()) {
                Timing.cpu.getN().get(j).setBusy(false);
                Timing.cpu.getN().get(j).setPid(-1);
            }
        }
        this.setDone(true);
        this.setReady(false);
        this.setRunning(false);
    }
}
