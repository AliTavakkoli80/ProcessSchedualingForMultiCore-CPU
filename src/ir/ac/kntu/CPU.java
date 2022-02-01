package ir.ac.kntu;

import java.util.ArrayList;

public class CPU {

    public ArrayList<Core> N;
    private final int numCores;


    public CPU(int n) {
        this.numCores = n;
        this.N = new ArrayList<>(numCores);
        for (int i = 0; i < n; i++) {
            N.add(new Core(i));
        }
    }

    public int getNumCores() {
        return numCores;
    }

    public int getFreeCoresNum() {
        int freeCores = 0;
        for (int i = 0; i < N.size(); i++) {
            if (!N.get(i).isBusy) {
                freeCores++;
            }
        }

        return freeCores;
    }

    public int getBusyCoresNum() {
        return numCores - getFreeCoresNum();
    }

    public ArrayList<Core> getN() {
        return N;
    }

    public class Core {

        private final int coreNum;
        private boolean isBusy;
        private int pid;

        public Core(int coreNum) {
            this.coreNum = coreNum;
            this.isBusy = false;
            this.pid = -1;
        }

        public boolean isBusy() {
            return isBusy;
        }

        public void setBusy(boolean busy) {
            isBusy = busy;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public int getCoreNum() {
            return coreNum;
        }

        @Override
        public String toString() {
            return "Core{" +
                    "coreNum=" + coreNum +
                    ", isBusy=" + isBusy +
                    ", pid=" + pid +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CPU{" +
                "Cores=" + N.toString() +
                ", numCores=" + numCores +
                ", numFreeCores=" + getFreeCoresNum() +
                ", numBusyCores=" + getBusyCoresNum() +
                '}';
    }
}
