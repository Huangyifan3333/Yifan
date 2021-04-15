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

/**
 *
 * @author yifan
 */
public class Main {
    // attributes
    public static int processCore = 0;
    public static int processNum = 0;
    // initial process Queue
    public static ArrayList<Process> processList = new ArrayList<Process>();
    // public static Queue<String> commandList = new LinkedList<String>();
    public static int pageNum = 0;
    // linkedList page in main memory
    public static LinkedList<Page> pageList = new LinkedList<Page>();

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
        
        Main.readFile_process("process.txt");
        Main.readFile_memconfig("memconfig.txt");
        Main.readFile_commands("commands.txt");

        Scheduler sch = new Scheduler();
        sch.start();
        Thread clockThread = new Thread(MyClock.INSTANCE);
        clockThread.start();

        MyClock.INSTANCE.setEndClock(true);
        try {
            sch.join();
            clockThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    
    // read data from process.txt to Main.processList
    public static void readFile_process(String fileName) {
        List<String> inputLines = Collections.emptyList();

        // Get Working Directory. Code is one folder lower.
        String workingDirectory = Paths.get("").toAbsolutePath().toString();

        try {
            inputLines = Files.readAllLines(Paths.get(workingDirectory, fileName), StandardCharsets.UTF_8);
        }

        catch (IOException exception) {
        }

        int inputSize = inputLines.size();
        int lineCounter = 0;

        // Read Process Core number
        COEN346_Pro_03.Main.processCore = Integer.parseInt(inputLines.get(lineCounter++));
        System.out.print("Process Core: " + Main.processCore + "\n");
        
        // Read total Process number
        COEN346_Pro_03.Main.processNum = Integer.parseInt(inputLines.get(lineCounter++));
        System.out.print("Process Number: " + Main.processNum + "\n");
        
        Semaphore countingSemaphore = null;
        if (Main.processCore > 0) {
            countingSemaphore = new Semaphore(Main.processCore);
        } else {
            System.out.print("read process file error \n");
        }
        Semaphore mutex = new Semaphore(1);
        
        if (inputSize > 1) {
            while (lineCounter < inputSize) {

                for (int i = 0; i < Main.processNum; i++) {
                    Main.processList.add(new COEN346_Pro_03.Process(inputLines.get(lineCounter++), countingSemaphore, mutex));
                }
            }
        }
    }
    
    //read data from memconfig.txt to Main.pageNum
    public static void readFile_memconfig(String fileName) {
        List<String> inputLines = Collections.emptyList();

        // Get Working Directory. Code is one folder lower.
        String workingDirectory = Paths.get("").toAbsolutePath().toString();

        try {
            inputLines = Files.readAllLines(Paths.get(workingDirectory, fileName), StandardCharsets.UTF_8);
        }

        catch (IOException exception) {
        }
        
        if(!inputLines.isEmpty()){
            COEN346_Pro_03.Main.pageNum = Integer.parseInt(inputLines.get(0));
            System.out.print("Page number: " + Main.pageNum + "\n");            
        }
        
        if(Main.pageNum > 0){
            for (int i=0; i<Main.pageNum; i++){
                // initialize the empty pages to the list
                Main.pageList.add(new Page());
            }
        }
    }
    
    //read data from commands.txt to Command.commandQueueString
    public static void readFile_commands(String fileName) {
        List<String> inputLines = Collections.emptyList();

        // Get Working Directory. Code is one folder lower.
        String workingDirectory = Paths.get("").toAbsolutePath().toString();

        try {
            inputLines = Files.readAllLines(Paths.get(workingDirectory, fileName), StandardCharsets.UTF_8);
        } catch (IOException exception) {
        }

        int inputSize = inputLines.size();
        int lineCounter = 0;
        if (inputSize > 0) {
            for (int i = 0; i < inputSize; i++) {
                Command.commandStringQueue.add(inputLines.get(i));
                System.out.print("command: " + inputLines.get(i) + "\n");
            }
        }

    }

    
    
}
