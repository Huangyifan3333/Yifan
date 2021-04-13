/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package COEN346_Pro_03;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author yifan
 */
public class Scheduler extends Thread{
    //attributes
    private Queue<Process> readyQueue = null;
    private ArrayList<Process> processArray = null;

    public Scheduler() {
        this.readyQueue = new LinkedList<Process>();
        processArray = new ArrayList<Process>();
    }

    //setter and getter
    public void setReadyQueue(Queue<Process> readyQueue) {
        this.readyQueue = readyQueue;
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
    
    
    
    

    @Override
    public void run() {
        //to do ...
        while(!MyClock.INSTANCE.isEndClock()){// end clock first, then join the Scheduler!!!
            // to do...
            // read clock ...
            // dequeue process and start thread by time
            // add runing process to process array
            // set process state to start...
            //...
            
            
            //join the process thread from process array
            
        }
    }
    
    
   
    
}
