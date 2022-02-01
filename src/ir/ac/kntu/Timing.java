package ir.ac.kntu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.*;

public class Timing {

    public static int numCores = 5;
    public static int numProcesses = 4;
    public static int infoTime = 5;
    public static long elapsedTime = 0;
    public static CPU cpu = new CPU(numCores);


    public static void main(String[] args) {

        ArrayList<Process> processes = randomProcessGenerator();

//        ArrayList<Process> processes = new ArrayList<>(numProcesses);
//        Process process = new Process(0, 1, 1, 122);
//        Process process2 = new Process(0, 1, 1, 123);
//        Process process3 = new Process(0, 1, 2, 124);
//        Process process4 = new Process(0, 1, 1, 125);
//        processes.add(process);
//        processes.add(process2);
//        processes.add(process3);
//        processes.add(process4);

        Collections.sort(processes);

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                getSysInfo(cpu, processes);
            }
        }, 0, infoTime + 1, TimeUnit.SECONDS);


        boolean flag = true;
        while (flag) {
            int doneCounter = 0;

            for (Process processTmp : processes) {
                if (processTmp.isDone()) {
                    doneCounter++;
                }
            }
            if (doneCounter == processes.size()) {
                flag = false;
            }
            checkIfProcessIsReady(elapsedTime, processes);
            assignCoreToProcess(elapsedTime, cpu, processes);
            runningProcess(elapsedTime, cpu, processes);
//            System.out.println(cpu.toString());

//            System.out.println(processes.toString());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            elapsedTime++;
//            System.out.println(elapsedTime + "***************************************************");
        }
        exec.shutdown();
        getSysInfo(cpu, processes);


    }

    public static void checkIfProcessIsReady(long elapsedTime, ArrayList<Process> processes) {
        for (Process process : processes) {
            if (elapsedTime == process.getArrivalTime() && !process.isDone()) {
//                System.out.println("Process " + process.getPid() + " is ready!");
                process.setReady(true);
            }
        }
    }

    public static void assignCoreToProcess(long elapsedTime, CPU cpu, ArrayList<Process> processes) {
        for (Process process : processes) {
            if (process.isReady() && !process.isDone()) {
                if (cpu.getFreeCoresNum() >= process.getCoreNeeded()) {
//                    System.out.println("Process " + process.getPid() + " found free core!");
                    int coreCounter = 0;
                    for (int j = 0; j < cpu.getNumCores(); j++) {
                        if (!cpu.getN().get(j).isBusy() && coreCounter < process.getCoreNeeded()) {
                            cpu.getN().get(j).setBusy(true);
                            cpu.getN().get(j).setPid(process.getPid());
                            coreCounter++;
                        }
                    }
                    if (coreCounter == process.getCoreNeeded()) {
                        process.setRunning(true);
                        process.setReady(false);
                        process.setStartTime(elapsedTime);
                    }
                }
            }
        }
    }

    public static void runningProcess(long elapsedTime, CPU cpu, ArrayList<Process> processes) {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(processes.size());
        for (Process process : processes) {
            if (process.isRunning()) {
                threadPoolExecutor.submit(process);
//                System.out.println("Process " + process.getPid() + "is running!");
//                if (process.getStartTime() + process.getCPUBurst() == elapsedTime) {
//                    for (int j = 0; j < cpu.getNumCores(); j++) {
//                        if (cpu.getN().get(j).isBusy() && cpu.getN().get(j).getPid() == process.getPid()) {
//                            cpu.getN().get(j).setBusy(false);
//                            cpu.getN().get(j).setPid(-1);
//                        }
//                    }
//                    process.setDone(true);
//                    process.setReady(false);
//                    process.setRunning(false);
//                }

            }
        }
        threadPoolExecutor.shutdown();

    }

    public static ArrayList<Process> randomProcessGenerator() {
        ArrayList<Process> processes = new ArrayList<Process>(numProcesses);
        for (int i = 0; i < numProcesses; i++) {
            int randArrivalTime = ThreadLocalRandom.current().nextInt(0, 10);
            int randCPUBurst = ThreadLocalRandom.current().nextInt(1, 10);
            int randCoreNeeded = ThreadLocalRandom.current().nextInt(1, numCores);
            int randPid = ThreadLocalRandom.current().nextInt(0, 100);
            Process temp = new Process(randArrivalTime, randCPUBurst, randCoreNeeded, randPid);
            processes.add(temp);
        }
        return processes;
    }


    public static void getSysInfo(CPU cpu, ArrayList<Process> processes) {
        System.out.println("**********************+< elapsedTime : " + elapsedTime + " >+**********************");
        System.out.println(cpu.toString());
        System.out.println(processes.toString());
    }

}
