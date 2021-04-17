/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package COEN346_Pro_03;

import COEN346_Pro_02.fileWriter;
import static COEN346_Pro_02.fileWriter.workingDirectory;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yifan
 */
public enum MyfileIO {
    INSTANCE;
    private String workingDirectory;
    private FileWriter fileWRToDisk = null;
    private FileWriter fileWRToOutput = null;
    // public static List<String> inputLines = Collections.emptyList();

    MyfileIO() {
        this.workingDirectory = Paths.get("").toAbsolutePath().toString();

        try {
            // Open the output file
            this.fileWRToDisk = new FileWriter(Paths.get(workingDirectory, "disk.txt").toAbsolutePath().toString());
            this.fileWRToOutput = new FileWriter(Paths.get(workingDirectory, "output.txt").toAbsolutePath().toString());
        } catch (IOException ex) {
            Logger.getLogger(MyfileIO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public FileWriter getFileWRToDisk() {
        return fileWRToDisk;
    }

    public void setFileWRToDisk(FileWriter fileWRToDisk) {
        this.fileWRToDisk = fileWRToDisk;
    }

    public FileWriter getFileWRToOutput() {
        return fileWRToOutput;
    }

    public void setFileWRToOutput(FileWriter fileWRToOutput) {
        this.fileWRToOutput = fileWRToOutput;
    }
    
    
    

}
