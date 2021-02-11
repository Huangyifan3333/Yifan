/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package COEN346_Pro_01;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yifan
 */
public class testing {
   // empty class in the pacakge
   //delete it if used in another package
}

class MergeSort_COEN346 
{
    //public static int count = 0;// for testing only
    void merge(int array[], int left, int mid, int right)// merge method need be revised!!!
    {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        /* Create temp arrays */
        int L[] = new int[n1];
        int R[] = new int[n2];
       
        /*Copy data to temp arrays*/
        for (int i = 0; i < n1; ++i)
            L[i] = array[left + i];
        for (int j = 0; j < n2; ++j)
            R[j] = array[mid + 1 + j];
       
        /* Merge the temp arrays */
 
        // Initial indexes of first and second subarrays
        int i = 0, j = 0;
        // Initial index of merged array
        int k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                array[k] = L[i];
                i++;
            }
            else {
                array[k] = R[j];
                j++;
            }
            k++;
        }
        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            array[k] = L[i];
            i++;
            k++;
        }
        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            array[k] = R[j];
            j++;
            k++;
        }      
    }
    // recursive sort by using thread 
    void sort(int array[], int left, int right, FileWriter fileWriter) throws IOException
    {  
        Thread thread_test_01;
        thread_test_01 = new Thread(new Runnable(){
            public void run(){
                //count++;// for testing only
                //System.out.print("count :"+count);
                try{
                    //System.out.println();
                    if (left < right) {
                        // Find the middle point
                        int mid =left+ (right-left)/2;
                        sort(array, left, mid, fileWriter);
                        sort(array, mid + 1, right, fileWriter);
                        // Merge the sorted halves
                        merge(array, left, mid, right);
                    }
                }
                catch (Exception ex){
                    System.out.println("Exception: " + ex);
                }
            } 
        });
        
        try {
            thread_test_01.start();
            System.out.println(thread_test_01.getName() + "start");//for testing only
            fileWriter.write(thread_test_01.getName() + " start \n");
       
            thread_test_01.join();
            System.out.println(thread_test_01.getName() + " finish: ");//for testing only
            fileWriter.write(thread_test_01.getName() + " finish: ");
        } catch (InterruptedException ex) {
            Logger.getLogger(MergeSort_COEN346.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(int i =left; i<=right; ++i){
            System.out.print(array[I]+" ");//for testing only
            fileWriter.write(array[I]+" ");
        }
            System.out.println();//for testing only
            fileWriter.write("\n");
    }
    
    public static void main(String args[]) throws InterruptedException
    {
        System.out.println("Before Sorting");//for testing only
        try{
            ArrayList<String> arrayIn_String = new ArrayList(Files.readAllLines(Paths.get("input.txt")));
            int[] arrayIn_Int = new int[arrayIn_String.size()];
            
            for(int i=0; i<arrayIn_String.size();i++){
                arrayIn_Int[i]=Integer.parseInt(arrayIn_String.get(i));
                System.out.print(arrayIn_Int[i]+" ");//for testing only
                System.out.println();//for testing only
            }
            FileWriter fileWriter = new FileWriter("output.txt");
            MergeSort_COEN346 Obj = new MergeSort_COEN346();
            Obj.sort(arrayIn_Int, 0, arrayIn_Int.length - 1,fileWriter);//call the mergesort
            fileWriter.close();
            
            System.out.println("\nAfter Sorting");//for testing only
            printArray(arrayIn_Int);//for testing only
        }
        catch(IOException e){
            System.out.print(e);
            e.printStackTrace();
        }
        
    }
   
    static void printArray(int arr[])
    {
        int n = arr.length;
        for (int i = 0; i < n; ++i)
            System.out.print(arr[i] + " ");
        System.out.println();
    }

}
