/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package COEN346_Pro_02;

import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yifan
 */
public class Scheduler extends Thread{
   //private int remainingTime = 0; 
   private ArrayList<Process> processArray = null;
   // private Process process = null;
   private Queue<Process> processQueue = null;
   
   // dynamic quantumTime shared by the Running Process
   static int dynamicRunningTime=0;
   // dynamic readyTime to start to run each Process
   static int dynamicReadyTime =0;
   static int totalService =0;
   //clock run by Sechuler
   private Clock clock = null;
   
   public Scheduler(int initialReadyTime, int initialRunningTime){
       Scheduler.dynamicReadyTime=initialReadyTime;
       Scheduler.dynamicRunningTime=initialRunningTime;
       this.processArray = new ArrayList<>();
       this.processQueue = new LinkedList<>();
       clock = new Clock();
   }
   
   public void setTotalServiceTime(int time){
       Scheduler.totalService=time;
   }
   
   
   public void addProcess(Process process){
       if(process!=null)
           this.processArray.add(process);
   }
   
   //get process from processArray by the nearest process entryTime
   public Process getProcessByentryTime(int clockTime){
       Process temp= null;
       for(int i=0; i<processArray.size();++i){
           if(clockTime >= processArray.get(i).getEntryTime()){
                temp=processArray.get(i);
                processArray.remove(i);
                break;
            }
       }
       
       return temp;
   }
  
   // deprecated...
   public void enQueue(ArrayList<User> userarray){
       for(int i=0; i < userarray.size(); ++i){
           for (int j=0; j < userarray.get(i).getProcessCount();++j){
               this.processQueue.add(userarray.get(i).processes.get(j));
           }
       }
   }
   
   // override enQueue 
   public void enQueue(Process process) throws InterruptedException{
       if(process!=null){
            //if(!process.isAlive()){
                if(process.getRemainingTime()> 0){
                    if(process.isAlive()){
                        //suspend the process thread before enQueue
                        //this is dangerous to cause deallock!!!
                        //process.suspend();not work
                    }
                    this.processQueue.add(process);
                }else{
                    process.join();
                }
                
                ///printMsg("Th3 enQueue process at time "+Clock.time);
                process.setRemainingTime(process.getServiceTime()-Scheduler.dynamicReadyTime);
                process.printEnd();
            //}
       }
   }
   
   public void enQueueByReadyTime(int clockTime, Process process) throws InterruptedException{
       //test
       if(clockTime >= Scheduler.dynamicReadyTime){
           //printMsg("Th3 enQueue process at time "+Clock.time);
           if(process!=null){
               this.processQueue.add(process);
                if(process.getRemainingTime()<= 0){
                    printMsg("pid "+process.getProcessId() +"remaining Time: "+ process.getRemainingTime()+"\n");
                    process.printEnd();
                    process.join();
                }
                else if(process.isAlive()){
                    //suspend the process thread before enQueue
                    //this is dangerous to cause deallock!!!
                    process.printEnd();
                    //process.suspend();//not work
                }
            }    
       }
   }
   
   //deprecated...
   //enQueue process directly from processArray
   public void enQueueFromArray(int clockTime){
       this.processQueue.add(this.getProcessByentryTime(clockTime));
   }
   
   //deQueue by checking the real clockTime and readyTime for each process
   public Process deQueueByReadyTime(int readyTime, int clockTime){
       if(clockTime >= readyTime)
           return this.processQueue.poll();
       else
           return null;
   }
   
   // scheduleprocess to start running......
   public Process scheduleProcessRun(int readyTime,int clockTime) throws InterruptedException{
       Process p = null;
       //take process from processArray be ready to run
       if(!this.processArray.isEmpty()){
           p = this.getProcessByentryTime(clockTime);
   
           if(p!=null){

               if(!p.isAlive()){
                  p.start();
               }
               p.printStart();
             
           }
           return p;
       }else{
            //deQueue the process to be ready to run   
           p = this.deQueueByReadyTime(readyTime,clockTime);
           if(p!=null){
               //print right before process being executed in CPU
               if(p.getRemainingTime() > 0&& p.isInterrupted()){
                  //p.resume();
               }
               p.printStart();

           }
           return p;
       }     
   }
   
   // call a thread to run the scheduler... 
   // Scheduling algorithm is in this block 
   @Override
   public void run(){
       //start clock
       clock.start();
       
       while(Clock.time <= Scheduler.totalService){
           try {
               Thread.sleep(1000);
               //..to start processes
               Process p = null;
               p = this.scheduleProcessRun(Scheduler.dynamicReadyTime, Clock.time);//???            
               if(p!=null){                  
                   updateReadyTime(Scheduler.dynamicReadyTime,Scheduler.dynamicRunningTime);
                   //printMsg("UpdateReadyTime: "+Scheduler.dynamicReadyTime);
                   //printMsg("Real ClockTime: "+ Clock.time);
                   //printMsg("Th3 remaining Time: "+ p.getRemainingTime()+"\n");
                   Thread.sleep(Scheduler.dynamicRunningTime*1000);
                   this.enQueueByReadyTime(Clock.time, p); 
               }
               
           } catch (InterruptedException ex) {           
               Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
       
       try {
           clock.join();
       } catch (InterruptedException ex) {
           Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
  
   //update dynamic ready Time
   public void updateReadyTime(int currentReadyTime, int currentRunningTime){
       Scheduler.dynamicReadyTime = currentReadyTime + currentRunningTime;
   }
   
   public void updateReadyTime(int time){
       Scheduler.dynamicReadyTime = time;
   }
   
   //update dynamic RunningTime for each process before running
   public void updateRunningTime( ){
       // to do...
       
   }
   
   public void printMsg(String s){
       System.out.println(s);
   }
   
   
    
}
