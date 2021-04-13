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
public class Page {
    private int id=0;
    private int value=-1;

    // override constructor
    public Page() {
    }
    public Page(int id, int value){
        this.id =id;
        this.value=value;
    }
    public Page(String inputLine){
        String[] splitInput = inputLine.split(" ");
        this.id = Integer.parseInt(splitInput[0]);
        this.value = Integer.parseInt(splitInput[1]);
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
    
    
}
