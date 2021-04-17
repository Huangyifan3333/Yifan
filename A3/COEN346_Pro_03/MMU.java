/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package COEN346_Pro_03;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yifan
 */
public enum MMU implements Runnable {
    INSTANCE();
    //attributes
    private boolean endMMU;
    private LinkedList<Page> mainMemory = null;
    private Queue<CommandPID_Pair> commandPid_PairQueue = null;

    //temp disk buffer to store value before writing disk
    private ArrayList<Page> tempDisk = null;
    // binary semaphore behave as mutex
    private Semaphore mutex_1 = null;
    private Semaphore mutex_2 = null;
    private Semaphore mutex_3 = null;

    //deprecated
    private Queue<Command> commandQueue = null;
    private Queue<Integer> processIdQueue = null;

    MMU() {
        this.endMMU = false;//default state
        this.mainMemory = new LinkedList<>();
        this.commandPid_PairQueue = new LinkedList<>();
        this.tempDisk = new ArrayList<>();
        //mutex initialized
        mutex_1 = new Semaphore(1);
        mutex_2 = new Semaphore(1);
        mutex_3 = new Semaphore(1);
        
        //deprecated
        this.commandQueue = new LinkedList<>();
        this.processIdQueue = new LinkedList<>();
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

    public Queue<CommandPID_Pair> getCommandPid_pair() {
        return commandPid_PairQueue;
    }

    public void addToCommandPid_PairQueue(CommandPID_Pair commandPid_pair) {
        this.commandPid_PairQueue.add(commandPid_pair);
    }

    public Semaphore getMutex_1() {
        return mutex_1;
    }

    // deprecated
    public Queue<Command> getCommandQueue() {
        return commandQueue;
    }

    public Queue<Integer> getProcessIdQueue() {
        return processIdQueue;
    }

    public void addToProcessIdQueue(int pid) {
        this.processIdQueue.add(pid);
    }

    public void addToCommandQueue(Command command) {
        this.commandQueue.add(command);
    }

    @Override
    public void run() {
        // initial value
        CommandPID_Pair CPP = new CommandPID_Pair(-1, new Command("", -1, -1));

        while (!this.endMMU) {
            try {
                // this.printMsg("Enter MMU \n");
                int clockTime = MyClock.INSTANCE.getTime();
                // MyClock.INSTANCE.printMsg("Clock " + clockTime + "\n");

                //mutex protection access to data queue
                this.mutex_1.acquire();
                // critical section: 

                if (!this.commandPid_PairQueue.isEmpty()) {// access data queue
                    clockTime = MyClock.INSTANCE.getTime();
                    // this.printMsg("Enter MMU \n");

                    CPP = this.commandPid_PairQueue.remove();// access data queue
                    String CString = CPP.getCommand().getCommand();
                    int id = CPP.getCommand().getId();
                    int value = CPP.getCommand().getValue();
                    int pid = CPP.getPid();

                    switch (CString) {
                        case "Store" -> {
                            try {
                                this.storePage(id, value, pid);
                            } catch (IOException ex) {
                                Logger.getLogger(MMU.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        }
                        case "Release" -> {
                            try {
                                this.releasePage(id, pid);
                            } catch (IOException ex) {
                                Logger.getLogger(MMU.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        }
                        case "Lookup" -> {
                            try {
                                // call lookup API and print inside API
                                this.lookupPage(id, pid);
                            } catch (IOException ex) {
                                Logger.getLogger(MMU.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        }
                        default -> {
                            // any new API could be added here
                            break;
                        }
                    }
                } else {
                    // sleep while no comand is coming in
                    Thread.sleep(10);
                    // MyClock.INSTANCE.printMsg("Clock " + clockTime + " waiting... \n");
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(MMU.class.getName()).log(Level.SEVERE, null, ex);
            }

            // realese permit
            this.mutex_1.release();

        }
        
        try {
            MyfileIO.INSTANCE.getFileWRToDisk().close();
            MyfileIO.INSTANCE.getFileWRToOutput().close();
        } catch (IOException ex) {
            Logger.getLogger(MMU.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //API method -- Store, return 0 if store in main memory, return 1 if store in disk.txt
    public int storePage(int id, int value, int pid)
            throws IOException {
        //print Store API
        int clockTime = MyClock.INSTANCE.getTime();
        String msg = "Clock: " + clockTime + ", Proccess " + pid + ", Store: Id " + id + ", value " + value + "\n";
        MyClock.INSTANCE.printMsg(msg);
        //write to output.txt
        MyfileIO.INSTANCE.getFileWRToOutput().write(msg);

        Page page = new Page(id, value);
        //main memory has empty spot
        if (this.mainMemory.size() < Main.pageNum) {
            // add page to main memory header
            this.mainMemory.addFirst(page);

            this.printMsg("store in main memory \n");
            return 0;
        } else {// main memory is full
            // store page in temporary disk
            this.tempDisk.add(new Page(id, value));

            // write to disk.txt
            this.writeToDisk(id, value);
            this.printMsg("store in disk \n");
            return 1;
        }
    }

    //API method -- Release, return true if deleted, otherwise return false
    public boolean releasePage(int id, int pid)
            throws IOException {
        int clockTime;
        boolean valid = false;
        if (!this.mainMemory.isEmpty()) {
            //search main memory
            for (Page page : this.mainMemory) {
                if (page.getId() == id) {
                    valid = true;
                    //print Release API
                    clockTime = MyClock.INSTANCE.getTime();
                    String msg = "Clock: " + clockTime + ", Proccess " + pid + ", Release: Id " + id + "\n";
                    MyClock.INSTANCE.printMsg(msg);
                    //write to output.txt
                    MyfileIO.INSTANCE.getFileWRToOutput().write(msg);
                    this.printMsg("value " + page.getValue() + " was removed \n");
                    this.mainMemory.remove(page);
                    return valid;
                }
            }
        }

        clockTime = MyClock.INSTANCE.getTime();
        String msg = "Clock: " + clockTime + ", Proccess " + pid + ", Release: Id " + id + " falied \n";
        MyClock.INSTANCE.printMsg(msg);
        this.printMsg(" no matched value in main memory \n");
        //write to output.txt
        MyfileIO.INSTANCE.getFileWRToOutput().write(msg);
        return valid;
    }

    //API method -- Lookup, return value if found, 
    //return -911 if no page found in either memory or disk
    public int lookupPage(int id, int pid)
            throws IOException {
        int value;
        int clockTime;

        // search in main memory return value if found in memory
        if (!this.mainMemory.isEmpty()) {
            for (Page page : this.mainMemory) {
                if (page.getId() == id) {
                    value = page.getValue();
                    // print Lookup when page found in main memory
                    clockTime = MyClock.INSTANCE.getTime();
                    String msg = "Clock: " + clockTime + ", Proccess " + pid + ", Lookup: Id " + id + ", value " + value + "\n";
                    MyClock.INSTANCE.printMsg(msg);
                    //write to output.txt
                    MyfileIO.INSTANCE.getFileWRToOutput().write(msg);

                    //remove obj and add to the header
                    this.mainMemory.remove(page);
                    this.mainMemory.addFirst(page);
                    return value;
                }
            }
        }

        // search disk and update disk.txt file, return value if found in disk.
        value = this.searchDisk(id);
        boolean isSwap = false;
        if (value != -911) {
            isSwap = this.SwapPage(id, value, pid);
        } else {
            // print to test error 
            this.printMsg("error value is: " + value + "\n");
        }
        // print Lookup after SWAP
        if (isSwap) {
            clockTime = MyClock.INSTANCE.getTime();
            String msg = "Clock: " + clockTime + ", Proccess " + pid + ", Lookup: Id " + id + ", value " + value + "\n";
            MyClock.INSTANCE.printMsg(msg);
            //write to output.txt
            MyfileIO.INSTANCE.getFileWRToOutput().write(msg);
        } else {
            clockTime = MyClock.INSTANCE.getTime();
            String msg = "Clock: " + clockTime + ", Proccess " + pid + ", Lookup: Id " + id + " not found \n";
            MyClock.INSTANCE.printMsg(msg);
            //write to output.txt
            MyfileIO.INSTANCE.getFileWRToOutput().write(msg);
        }
        return value;
    }

    // search value by ID, return -911 if not found id
    public int searchDisk(int id)
            throws IOException {
        int var = -911;

        try {
            this.mutex_2.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(MMU.class.getName()).log(Level.SEVERE, null, ex);
        }
        // the following is deprecated....
        // read data from disk.txt
        String workingDirectory = Paths.get("").toAbsolutePath().toString();
        ArrayList<String> inputArray
                = new ArrayList(Files.readAllLines((Paths.get(workingDirectory, "disk.txt")), StandardCharsets.UTF_8));

        this.mutex_2.release();

        String[] value;
        if (!inputArray.isEmpty()) {
            for (String s : inputArray) {
                // split String line...
                value = s.split(" ");
                if (Integer.parseInt(value[0]) == id) {
                    var = Integer.parseInt(value[1]);
                } else {// replace disk.txt, skip the swapped page value
                    this.writeToDisk(Integer.parseInt(value[0]), Integer.parseInt(value[1]));
                }
            }
        }
        // the above is deprecated

        // new temp disk
        ArrayList<Page> newTempDisk = new ArrayList<>();

        try {
            this.mutex_3.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(MMU.class.getName()).log(Level.SEVERE, null, ex);
        }
        //mutex protects access to temp disk
        // search temp disk
        for (int i=0; i<this.tempDisk.size(); i++) {
            if (this.tempDisk.get(i).getId() == id) {
                var = this.tempDisk.get(i).getValue();
                this.tempDisk.remove(this.tempDisk.get(i));
            } else {
                // add the new temp disk , skip the value deleted
                newTempDisk.add(this.tempDisk.get(i));
            }
        }
        //copy to temp disk
        this.tempDisk = newTempDisk;
        
        //release permit
        this.mutex_3.release();
       
        return var;
    }

    // swap the specified page from disk to memory
    public boolean SwapPage(int id, int value, int pid)
            throws IOException {
        Page page = new Page();
        try {
            this.mutex_3.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(MMU.class.getName()).log(Level.SEVERE, null, ex);
        }
        //mutex protects access to main memory and temp disk
        // check main memory size
        if (this.mainMemory.size() == Main.pageNum) {
            // remove the least accessed page from the main memory 
            page = this.mainMemory.removeLast();

            // store this removed page in temporary disk
            this.tempDisk.add(page);
            // store this removed page in disk.txt
            this.writeToDisk(page.getId(), page.getValue());
        }

        //store the sepcific page from disk in main memory 
        boolean isStore = false;
        if (this.mainMemory.size() < Main.pageNum) {
            // add page to main memory header
            this.mainMemory.addFirst(new Page(id, value));
            isStore = true;
        }
        //release permit
        this.mutex_3.release();
        
        int clock = MyClock.INSTANCE.getTime();
        if (isStore) {
            // print SWAP
            String msg = "Clock: " + clock + ", Memeory Manger, SWAP: " + "Id " + id + " with Value " + value + "\n";
            MyClock.INSTANCE.printMsg(msg);
            //write to output.txt
            MyfileIO.INSTANCE.getFileWRToOutput().write(msg);
            return true;
        } else {
            this.printMsg("Clock: " + clock + ", Swap failed \n");
            return false;
        }

    }

    // write page to disk.txt
    public void writeToDisk(int id, int value)
            throws IOException {
        try {
            this.mutex_2.acquire();

            // write to disk.txt
            MyfileIO.INSTANCE.getFileWRToDisk().write(id + " " + value + "\n");

        } catch (InterruptedException ex) {
            Logger.getLogger(MMU.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.mutex_2.release();
    }

    //write print msg to output.txt
    public void writeToOutput(String e)
            throws IOException {

        MyfileIO.INSTANCE.getFileWRToOutput().write(e);
    }

    public void printMsg(String e) {
        System.out.print(e);
    }

}
