/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package COEN346_Pro_03;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yifan
 */
public enum MyClock implements Runnable{
    INSTANCE(1000);

    private int time;
    private boolean endClock;
    
    MyClock(int t){
        this.time = t;
        this.endClock = false;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isEndClock() {
        return endClock;
    }

    public void setEndClock(boolean endClock) {
        this.endClock = endClock;
    }
    
    public void printMsg(String msg){
        System.out.print(msg);
    }
    
    @Override
    public void run() {
        while(!this.endClock){
            try {
                Thread.sleep(100);
                this.time = this.time + 100;
            } catch (InterruptedException ex) {
                Logger.getLogger(MyClock.class.getName()).log(Level.SEVERE, null, ex);
                System.out.print("Clock Time: " + this.time + "\n");
            }
        }
    }
    
    
}
