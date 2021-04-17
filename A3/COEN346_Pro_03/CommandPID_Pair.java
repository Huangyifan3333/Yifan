/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package COEN346_Pro_03;

/**
 *
 * @author yifan
 */
public class CommandPID_Pair {
    private int pid=-1;
    private Command command = null;

    public CommandPID_Pair() {
    }
    
    public CommandPID_Pair(int p, Command c) {
        this.pid = p;
        this.command = c;
    }
    

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
    
    
    
}
