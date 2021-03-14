/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package COEN346_Pro_02;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yifan
 */
public class Clock extends Thread{
    static int time;
       
    public Clock(){
        Clock.time = 0;
    }
    
    public long getTime(){
        return Clock.time;
    }
    
    @Override
    public void run(){
        while(true){
            try {
                //sleep for 1 second
                Thread.sleep(1000);
                Clock.time++;
                //System.out.println("Th1 Time: "+Clock.time);//test clock
            } catch (InterruptedException ex) {
                Logger.getLogger(Clock.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(Clock.time > 30){
                return;
            }
        }
    } 
}
