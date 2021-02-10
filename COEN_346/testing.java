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
class MergeSort
{
    
    public static int count = 0;

    void merge(int array[], int a, int b, int c)// merge method need be revised!!!
    {
        // Find sizes of two subarrays to be merged
        int size1 = b - a + 1;
        int size2 = c - b;
 
        /* Create temp arrays */
        int left[] = new int[size1];
        int right[] = new int[size2];
        
        int printArray[] = new int[size1+size2];
 
        /*Copy data to temp arrays*/
        while (i<size1 && j<size2) {
            left[i] = array[a + i];
            right[j] = array[b + 1 + j];
        }
//        /*
//        for (int i = 0; i < size1; ++i)
//            left[i] = array[a + i];
//        for (int j = 0; j < size2; ++j)
//            right[j] = array[b + 1 + j];
//
//         */
        
        
        /* Merge the temp arrays */
 
        // Initial indexes of first and second subarrays
        int i = 0, j = 0;
 
        // Initial index of merged subarry array
        int k = a;
        for (int i = 0; i < size1; ++i) {
            if (left[i] <= right[j]) {
                array[k] = left[i];
                i++;
            }
            else {
                array[k] = right[j];
                j++;
            }
            k++;
        }
//        while (i < size1 && j < size2) {
//            if (left[i] <= right[j]) {
//                array[k] = left[i];
//                i++;
//            }
//            else {
//                array[k] = right[j];
//                j++;
//            }
//            k++;
//        }
 
        /* Copy remaining elements of L[] if any */
        for (int i=0; i<size1; i++){
            array[k] = left[i];
            i++;
            k++;
        }
//        while (i < size1) {
//            array[k] = left[i];
//            i++;
//            k++;
//        }
        for (int j=0; j<size2; j++){
            array[k] = left[j];
            j++;
            k++;
        }
//        /* Copy remaining elements of R[] if any */
//        while (j < size2) {
//            array[k] = right[j];
//            j++;
//            k++;
//        }
//
        for(int k=1; k<(size1+size2); k++){
            printArray[k] = array[k];
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
        int arrayp[] = {1,10,100,1000,10000};
        System.out.println("Array before sorted:");
        printArray(array);
        MergeSort merge= new MergeSort();
        merge.sort(array, 0, array.length - 1);
        System.out.println("\nArray after sorted:");
        printArray(array);
//        int arr[] = { 12, 11, 13, 5, 69, 17, 1000, 900};
//        System.out.println("Given Array");
//        printArray(arr);
//        MergeSort_online ob = new MergeSort_online();
//        ob.sort(arr, 0, arr.length - 1);
//        System.out.println("\nSorted array");
//        printArray(arr);
        
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
