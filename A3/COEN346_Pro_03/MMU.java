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
public class MMU extends Thread{
    //attributes
    private LinkedList<Page> pageList = null;
    static Queue<Command> commandQueue = null;

    public MMU() {
        this.pageList = new LinkedList<>();
        COEN346_Pro_03.MMU.commandQueue = new LinkedList<>();
    }

    // setter
    public void setPageList(LinkedList<Page> pageList) {
        this.pageList = pageList;
    }

    // deprecated
    public void addToCommandQueue(Command command){
        MMU.commandQueue.add(command);
    }

    @Override
    public void run() {
        //to do
        
    }
    
    
    
    
    
    
}
