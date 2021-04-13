/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package COEN346_Pro_03;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yifan
 */
public class Process extends Thread {

    private int arrivingTime = 0;
    private int serviceTime = 0;
    static int pid = 0;
    private int processID = 0;
    private boolean changeState;
    private int processState;
    // 0 : idle
    // 1: started
    // 2: finished

    //counting semaphore to run multiple processes concurrently 
    Semaphore S_process = null;
    // binary semaphore to behave as mutex
    Semaphore mutex = null;

    // ---- CONSTRUCTOR ----
    public Process(String inputLine) {
        String[] splitInput = inputLine.split(" ");
        this.arrivingTime = Integer.parseInt(splitInput[0]);
        this.serviceTime = Integer.parseInt(splitInput[1]);
        COEN346_Pro_03.Process.pid++;
        this.processID = Process.pid;
        this.changeState = false;
        this.processState = 0;//initial stste
    }

    //getter and setter
    public int getArrivingTime() {
        return arrivingTime;
    }

    public void setArrivingTime(int arrivingTime) {
        this.arrivingTime = arrivingTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getProcessID() {
        return processID;
    }

    public void setProcessID(int processID) {
        this.processID = processID;
    }

    public boolean isChangeState() {
        return changeState;
    }

    public void setChangeState(boolean changeState) {
        this.changeState = changeState;
    }

    public int getProcessState() {
        return processState;
    }

    public int setProcessState(int processState) {
        try {
            this.mutex.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
        }
        int time = MyClock.INSTANCE.getTime();
        this.mutex.release();
        if (this.processState != 2) {
            this.processState = processState;
            this.setChangeState(true);
        }
        return time; 
    }

    // get and set Semaphore
    public Semaphore getS_process() {
        return S_process;
    }

    public void setS_process(Semaphore S_process) {
        this.S_process = S_process;
    }

    public Semaphore getMutex() {
        return mutex;
    }

    public void setMutex(Semaphore mutex) {
        this.mutex = mutex;
    }

    @Override
    public void run() {
        try {
            //to do: add counting semaphore equaled to 2 !!!
            this.S_process.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
        }
        //crtical section 1:
        int timelapse = 0;
        int startTime = 0;
        this.changeState = true;
        do {
            try {
                // to do: add mutex for reading clock!!!
                this.mutex.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
            }
            //critical section 2:
            int clockTime = MyClock.INSTANCE.getTime(); // read clock
            this.mutex.release();

            switch (this.processState) {
                case 1 -> {// process started
                    if (this.changeState) {
                        try {
                            this.mutex.acquire();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        //critical section 3:
                        startTime = MyClock.INSTANCE.getTime();//read clock
                        this.mutex.release();
                        clockTime = startTime;
                        String msg = "Clock " + clockTime / 1000 + " , Process " + this.processID + "started" + "\n";
                        MyClock.INSTANCE.printMsg(msg);
                        this.changeState = false;
                    }
                    
                    timelapse = clockTime - startTime;
                    if (timelapse >= this.serviceTime) {
                        this.processState = 2;
                        String msg = "Clock " + clockTime / 1000 + " , Process " + this.processID + "finished" + "\n";
                        MyClock.INSTANCE.printMsg(msg);
                    }
                    // to do: send API call....

                    break;
                }
                case 2 -> {// process finished
                    break;
                }
                default -> {// process is idle
                    if (this.changeState) {
                        String msg = "Clock " + clockTime / 1000 + " , Process " + this.processID + "is idle" + "\n";
                        MyClock.INSTANCE.printMsg(msg);
                    }
                    break;
                }
            }// end Switch
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (this.processState != 2);

        //Semaphore release permit
        this.S_process.release();
        this.printMsg("Process is released, permit = " + this.S_process.availablePermits());

    }

    public void printMsg(String string) {
        System.out.println(string);
    }

}
