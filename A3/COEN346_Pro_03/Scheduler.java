/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package COEN346_Pro_03;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yifan
 */
public class Scheduler extends Thread {

    //attributes
    private Queue<Process> readyQueue = null;
    private ArrayList<Process> processArray = null;
    
    private boolean processEnd;

    public Scheduler() {
        this.readyQueue = new LinkedList<Process>();
        this.processArray = new ArrayList<Process>();
        this.processEnd= false;
    }

    //setter and getter
    public void setReadyQueue(Queue<Process> readyQueue) {
        this.readyQueue = readyQueue;
    }
    //Overrride setter
    public void setReadayQueue() {
        if (this.readyQueue == null) {
            this.printMsg("readuQueue is null \n");
        }
        ArrayList<Process> processlist = sortArray(Main.processList);
        if (processlist != null) {
            for (Process p : processlist) {
                this.readyQueue.add(p);
            }
        }
    }

    public Queue<Process> getReadyQueue() {
        return readyQueue;
    }

    public ArrayList<Process> getProcessArray() {
        return processArray;
    }

    public void setProcessArray(ArrayList<Process> processArray) {
        this.processArray = processArray;
    }

    public void addToProcessArray(Process process) {
        this.processArray.add(process);
    }

    @Override
    public void run() {
        // initialze ready queue
        this.setReadayQueue();
        if (this.readyQueue.isEmpty()) {
            this.printMsg("readyQueue is not initialized! \n");
            return;
        }

        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
            Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
        }

        int clockTime = MyClock.INSTANCE.getTime(); // read clock
        
        while (!this.processEnd) {// flag to control the loop
       
            if (!this.readyQueue.isEmpty()) {
                // get arriving time
                int arrivingTime = this.readyQueue.peek().getArrivingTime();
                if (clockTime >= arrivingTime) {
                    // dequeue process and start thread by time
                    Process p = this.readyQueue.poll();
                    
                    // set process state to start...
                    p.setProcessState(1);
                    p.start();// start thread
                    
                     // add runing process to process array
                    this.processArray.add(p);
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
            }
            clockTime = MyClock.INSTANCE.getTime(); // read clock
            
            this.updateProcessFlag();
        }

        //join the process thread from process array
        for (Process p : this.processArray) {
            try {
                p.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
    public void printMsg(String string) {
        System.out.print(string);
    }

    public ArrayList<Process> sortArray(ArrayList<Process> list) {
        // ascending order
        Queue<Process> readyQueue = new LinkedList<>();
        if (list == null || list.isEmpty()) {
            return null;
        }

        int size = list.size();
        Process tempProcess = new Process();
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (list.get(j).getArrivingTime() > list.get(j + 1).getArrivingTime()) {
                    // swap two processes
                    tempProcess = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, tempProcess);
                }
            }
        }
        return list;
    }
    
    public void updateProcessFlag() {
        int pNum = Main.processNum;
        if (pNum == 0) {
            return;
        }
        if (this.processArray.isEmpty()) {
            return;
        } else if (this.processArray.size() == pNum) {
            int tempFlag = 0;
            for (Process p : this.processArray) {
                if (p.getProcessState() == 2) {
                    tempFlag++;
                }
            }
            if (tempFlag == pNum) {
                this.processEnd = true;
            }
        }
    }
}
