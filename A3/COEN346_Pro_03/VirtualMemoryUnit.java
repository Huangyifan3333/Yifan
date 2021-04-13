/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package COEN346_Pro_03;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author yifan
 */
public class VirtualMemoryUnit extends Thread{
    //attributes
    private Queue<Page> pageQueue = null;

    public VirtualMemoryUnit() {
    }

    // setter
    public void setPageQueue(Queue<Page> pageQueue) {
        this.pageQueue = pageQueue;
    }

    

    @Override
    public void run() {
        //to do
    }
    
    
    
    
    
    
}
