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
public class Command {
    //default value
    private String command = "";
    private int id = -1;
    private int value = -1;
    static Queue<String> commandStringQueue = new LinkedList<>();

    public Command(String command, int id, int value) {
        this.command = command;
        this.id = id;
        this.value = value;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static Queue<String> getCommandQueue() {
        return commandStringQueue;
    }

    public static void setCommandQueue(Queue<String> commandQueue) {
        Command.commandStringQueue = commandQueue;
    }
    
    
    
    
}
