/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package COEN346_Pro_03;

import java.io.IOException;
import java.util.Queue;
import java.util.Random;
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
    private Semaphore S_process = null;
    // binary semaphore to behave as mutex
    private Semaphore mutex = null;

    //command from command file
    private Command command = null;
    private CommandPID_Pair commandPid_pair = null;

    // ---- CONSTRUCTOR ----
    public Process(){};
    
    public Process(String inputLine) {
        String[] splitInput = inputLine.split(" ");
        this.arrivingTime = Integer.parseInt(splitInput[0]) * 1000;
        this.serviceTime = Integer.parseInt(splitInput[1]) * 1000;
        COEN346_Pro_03.Process.pid++;
        this.processID = Process.pid;
        this.changeState = false;
        this.processState = 0;//initial stste
    }
    // override constructor
    public Process(String inputLine, Semaphore S, Semaphore mutex) {
        String[] splitInput = inputLine.split(" ");
        this.arrivingTime = Integer.parseInt(splitInput[0]) * 1000;
        this.serviceTime = Integer.parseInt(splitInput[1]) * 1000;
        COEN346_Pro_03.Process.pid++;
        this.processID = Process.pid;
        this.changeState = false;
        this.processState = 0;//initial stste
        
        this.S_process = S;// counting semaphore
        this.mutex = mutex;// binary semaphore
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
        int time = MyClock.INSTANCE.getTime();

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
        int timelapse = 0;
        int startTime = 0;
        int remainingTime = this.serviceTime;
        // this.changeState = true;
        try {
            // counting semaphore
            this.S_process.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
        }
        //crtical section 1:
        
        do {
            int clockTime = MyClock.INSTANCE.getTime(); // read clock

            switch (this.processState) {
                case 1 -> {// process started
                    if (this.changeState) {
                        startTime = MyClock.INSTANCE.getTime();//read clock
                        clockTime = startTime;
                        String msg = "Clock: " + clockTime + ", Process " + this.processID + " started" + "\n";
                        MyClock.INSTANCE.printMsg(msg);
                        try {
                            //write to output.txt
                            MyfileIO.INSTANCE.getFileWRToOutput().write(msg);
                        } catch (IOException ex) {
                            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        this.changeState = false;
                    }
                    
                    try {
                        // binary semaphore behaves as mutex
                        this.mutex.acquire();
                        //critical section 2:  protects execute command:
                        
                        // aquire the command from command Queue in Main
                        int length = this.readCommand(COEN346_Pro_03.Command.commandStringQueue);
                        
                        if (length > 0) {
                            // send command to MMU
                            this.sendCommand(this.command);
                            clockTime = MyClock.INSTANCE.getTime();
                            String msg = "Clock: " + clockTime + " , Process " + this.processID + ", " 
                                    + this.command.getCommand() + " " + this.command.getId() + " " 
                                    + this.command.getValue() + "\n";
                            // MyClock.INSTANCE.printMsg(msg);
                        }
                        else{
                            this.printMsg("sending command failed! \n");
                        }
                        
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    // mutex realese
                    this.mutex.release();
                    
                    timelapse = clockTime - startTime;
                    remainingTime = remainingTime - timelapse;
                    if (timelapse >= this.serviceTime) {
                        this.processState = 2;
                        clockTime = MyClock.INSTANCE.getTime();
                        String msg = "Clock: " + clockTime + ", Process " + this.processID + " finished" + "\n";
                        MyClock.INSTANCE.printMsg(msg);
                        try {
                            //write to output.txt
                            MyfileIO.INSTANCE.getFileWRToOutput().write(msg);
                        } catch (IOException ex) {
                            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    // thread wait for random time
                    this.thead_Wait(this.setRandomTime(remainingTime));

                    break;
                }
                case 2 -> {// process finished
                    break;
                }
                default -> {// process is idle
                    if (this.changeState) {
                        clockTime = MyClock.INSTANCE.getTime();
                        String msg = "Clock " + clockTime + " , Process " + this.processID + "is idle" + "\n";
                        MyClock.INSTANCE.printMsg(msg);
                        this.changeState = false;
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
        this.printMsg("Process is released, permit = " + this.S_process.availablePermits() + "\n");

    }

    // aquire the command
    public int readCommand(Queue<String> commandQueue) throws InterruptedException {
        // Thread.sleep(10);
        // dequeue command string
        String inputLine = commandQueue.remove();
        // enqueue command string 
        commandQueue.add(inputLine);
        
        String[] splitInput = inputLine.split(" ");
        if (splitInput.length > 0) {
            switch (splitInput.length) {
                case 2:// lookup and release command
                    this.command = new Command(splitInput[0], Integer.parseInt(splitInput[1]), -1);
                    break;
                case 3:// store command
                    this.command = new Command(splitInput[0], Integer.parseInt(splitInput[1]), Integer.parseInt(splitInput[2]));
                    break;
                default:
                    printMsg("read command error! \n");
                    break;
            }
            return splitInput.length;
        } else {// return -1 if no command was read
            return -1;
        }
    }

    // wait thread for specific time, similar to Thread.sleep(XXX)
    public void thead_Wait(int waitTime) {
        int clockTime = MyClock.INSTANCE.getTime();
        int finishTime = clockTime + waitTime;
        while (clockTime < finishTime) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
            }
            clockTime = MyClock.INSTANCE.getTime();
        }
    }

    // set minimum random time
    int setRandomTime(int remainingTime){
        Random rand = new Random();
        return Math.min(rand.nextInt(1000), remainingTime);
    }
    
    //send command to MMU
    void sendCommand(Command command) {
        try {
            MMU.INSTANCE.getMutex_1().acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
        }
        // mutex is protecting access data quque in MMU
        
        // add to commandpid_pair Queue in MMU
        MMU.INSTANCE.addToCommandPid_PairQueue(new CommandPID_Pair(this.processID, command));

        //depreacted 
        // add command to commandQueue in MMU
        MMU.INSTANCE.addToCommandQueue(command);
        // add the current process ID to PID queue in MMU
        MMU.INSTANCE.addToProcessIdQueue(this.processID);
        
        //release permit
        MMU.INSTANCE.getMutex_1().release();
    }
    public void printMsg(String string) {
        System.out.print(string);
    }

}
