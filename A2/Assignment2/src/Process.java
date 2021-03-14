package COEN346_Pro_02;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Process extends Thread{
    // ---- ATTRIBUTES ----
    private int entryTime = -1;
    private int readyTime = entryTime;// not sure to be useful?
    private int serviceTime = -1;
    private int remainingTime = -1;
    private int processId = -1;
    private ProcessStatus status = ProcessStatus.Unknown;
    // this is the flag to calculate the shared execution time
    private boolean active = false;
    
    // ---- CONSTRUCTOR ----
    public Process(String inputLine){
        String[] splitInput = inputLine.split(" ");
        this.readyTime = Integer.parseInt(splitInput[0]);
        this.serviceTime = Integer.parseInt(splitInput[1]);
        this.remainingTime = serviceTime;
        this.readyTime = this.entryTime;
    }

    /**
     *
     * @param eTime
     * @param sTime
     * @param pid
     */
    public Process (int eTime, int sTime, int pid){
        this.entryTime = eTime;
        this.serviceTime = sTime;
        this.processId = pid;
        this.remainingTime = this.serviceTime;
        this.readyTime = this.entryTime;
        
    }
    public Process(){
    // to test
    };
    
    //----Run Process----
    //run method...To be modified based on Time..
    @Override
    public void run(){
        
        while(this.remainingTime > 0){
            try {
                this.active = this.remainingTime > 0;
                this.setRemainingTime(this.remainingTime - Scheduler.dynamicRunningTime);
                this.setReadyTime(this.readyTime+Scheduler.dynamicRunningTime);
                //printMsg("pid" + this.processId+ " remaining time: "+ this.getRemainingTime()+"\n");
                Thread.sleep(1000);
            
            } catch (InterruptedException ex) {
                Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } 
    }

    // ---- ACCESSORS ----
    public int getEntryTime(){
        return entryTime;
    }
    public int getReadyTime() {
        return readyTime;
    }
    public int getServiceTime() {
        return serviceTime;
    }
    
    public int getRemainingTime() {
        return remainingTime;
    }
    
    public int getProcessId() {
        return processId;
    }
    public ProcessStatus getStatus() {
        return status;
    }

    // ---- SETTERS ----
    public void setReadyTime(int newReadyTime) {
        readyTime = newReadyTime;
    }
    public void setServiceTime(int newServiceTime) {
        serviceTime = newServiceTime;
    }
    
    public void setRemainingTime(int newRemainingTime) {
        remainingTime = newRemainingTime;
    }
    
    public void setProcessId(int newProcessId) {
        processId = newProcessId;
    }
    public void setStatus(ProcessStatus newStatus) {
        status = newStatus;
    }
    
    // ---- Print Function ----
    void printMsg(ProcessStatus msg){
        System.out.print(msg);
    }
    void printMsg(String s){
        System.out.print(s);
    }
    
    void printStart(){
        if(remainingTime == serviceTime){
            printMsg("Time "+ Clock.time+" ");
            printMsg("Process " + this.processId + " ");
            printMsg(ProcessStatus.Start);
            printMsg("\n");
        }else if(remainingTime < serviceTime && remainingTime > 0){
            printMsg("Time "+ Clock.time+" ");
            printMsg("Process " + this.processId + " ");
            printMsg(ProcessStatus.Resume);
            printMsg("\n");
        }
    }
    void printEnd(){
        if(remainingTime > 0){
            printMsg("Time "+ Clock.time+" ");
            printMsg("Process " + this.processId + " ");
            printMsg(ProcessStatus.Pause);
            printMsg("\n");
        }else if(remainingTime <= 0){
            printMsg("Time "+ Clock.time+" ");
            printMsg("Process " + this.processId + " ");
            printMsg(ProcessStatus.Finish);
            printMsg("\n");
        }
    }
    
   
    
}
