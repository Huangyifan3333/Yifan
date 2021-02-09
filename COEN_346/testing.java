/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package COEN346_Pro_01;

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

//code copied online
class MergeSort_online 
{
    
    public static int count = 0;
    // Merges two subarrays of arr[].
    // First subarray is arr[l..m]
    // Second subarray is arr[m+1..r]
    void merge(int arr[], int l, int m, int r)// merge method need be revised!!!
    {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;
 
        /* Create temp arrays */
        int L[] = new int[n1];
        int R[] = new int[n2];
        
        int print_array[] = new int[n1+n2];
 
        /*Copy data to temp arrays*/
        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];
        
        
        /* Merge the temp arrays */
 
        // Initial indexes of first and second subarrays
        int i = 0, j = 0;
 
        // Initial index of merged subarry array
        int k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            }
            else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }
 
        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }
 
        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
        
        for(int s=0; s<(n1+n2); ++s){
            print_array[s] = arr[s];
        }
    }
 
    
    void sort(int arr[], int l, int r)
    {
        
        Thread thread_test_01;
        thread_test_01 = new Thread(new Runnable(){
            public void run(){
                //count++;// to test thread quantity
                //System.out.print("count :"+count);
                try{
                    System.out.println();
                    if (l < r) {
                        // Find the middle point
                        int m =l+ (r-l)/2;
                        sort(arr, l, m);
                        sort(arr, m + 1, r);
                        // Merge the sorted halves
                        merge(arr, l, m, r);
                    }
                }
                catch (Exception ex){
                    System.out.println("Exception: " + ex);
                }
            } 
        });
        thread_test_01.start();
        System.out.println("Thread start: " + thread_test_01.getName());
        
        try {
            thread_test_01.join();
            System.out.println("Thread finish: " + thread_test_01.getName());
        } catch (InterruptedException ex) {
            Logger.getLogger(MergeSort_online.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(int i =l; i<=r; ++i){
            System.out.print(arr[i]+" ");
        }
            System.out.println();
        
    }
    
    /***
    class test implements Runnable{
        public void run(){
            int arr[] = { 12, 11, 13, 5, 6, 7, 1000, 900, 800, 700 ,600, 850, 330, 990, 399, 550};
 
            //System.out.println("Given Array");
            //printArray(arr);
            MergeSort_online ob = new MergeSort_online();
            ob.sort(arr, 0, arr.length - 1);
        }
      
    }
    ***/
    
     //code copied online
    public static void main(String args[]) throws InterruptedException
    {
        
        int arr[] = { 12, 11, 13, 5, 69, 17, 1000, 900};
        System.out.println("Given Array");
        printArray(arr);
        MergeSort_online ob = new MergeSort_online();
        ob.sort(arr, 0, arr.length - 1);
        System.out.println("\nSorted array");
        printArray(arr);
        
        /***
        Thread thread_test = new Thread(new Runnable(){
            public void run(){
                try{
                    ob.sort(arr, 0, arr.length - 1);
                }
                catch (Exception ex){
                    System.out.println("Exception: " + ex);
                }
                System.out.println("\nSorted array");
                printArray(arr);
            }
        });
        thread_test.start();
        thread_test.join();
        ***/
    }
        
    static void printArray(int arr[])
    {
        int n = arr.length;
        for (int i = 0; i < n; ++i)
            System.out.print(arr[i] + " ");
        System.out.println();
    }

}
