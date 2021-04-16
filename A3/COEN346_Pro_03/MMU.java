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
public enum MMU implements Runnable{
    INSTANCE();
    //attributes
    private boolean endMMU;
    private LinkedList<Page> mainMemory = null;
    private Queue<Command> commandQueue = null;

    MMU() {
        this.endMMU = false;
        this.mainMemory = new LinkedList<>();
        this.commandQueue = new LinkedList<>();
    }

    // setter
    public void setMainMemory(LinkedList<Page> pageList) {
        this.mainMemory = pageList;
    }

    public void setEndMMU(boolean endMMU) {
        this.endMMU = endMMU;
    }

    public boolean isEndMMU() {
        return endMMU;
    }

    public LinkedList<Page> getMainMemory() {
        return mainMemory;
    }

    public Queue<Command> getCommandQueue() {
        return commandQueue;
    }
    
    
    
    // deprecated
    public void addToCommandQueue(Command command){
        this.commandQueue.add(command);
    }

    
    @Override
    public void run() {
        //to do...
        while(!this.endMMU){
            // to do...
        }
        
    }
    
    
    
    
    
    
}
