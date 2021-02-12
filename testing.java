package COEN346_Pro_01;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*; 
import java.nio.charset.StandardCharsets; 
import java.nio.file.*; 
import java.io.*; 

class MergeSort_COEN346 
{    
    public static void main(String args[]) throws InterruptedException
    {
        // Reading input file and converting it to integer array before starting sort
        List<String> inputAsString = readInputFile("input.txt"); 
        List<Integer> inputAsInt = convertStringListToIntList(inputAsString);
        Integer[] inputAsIntegerArray = inputAsInt.toArray(new Integer[inputAsInt.size()]);
        int[] intArray = Arrays.stream(inputAsIntegerArray).mapToInt(Integer::intValue).toArray();

        // Print input to console (TEST)
        System.out.println("\n-----------------------\nInput - Before Sorting:\n-----------------------");
        printArray(intArray);
        System.out.println("\n\n-----------------------\nStarting Merge-Sort:\n-----------------------");

        try{
            //Get working 
            String workingDirectory = Paths.get("").toAbsolutePath().toString();

            // Open the output file
            FileWriter fileWriter = new FileWriter(Paths.get(workingDirectory, "output.txt").toAbsolutePath().toString());

            // Start the merge-sort
            sort(intArray, 0, intArray.length - 1,fileWriter);

            // Close the output file
            fileWriter.close();
            
            // Print results to console
            System.out.println("\n-----------------------\nAfter Sorting:\n-----------------------");
            printArray(intArray);
        }
        catch(IOException e){
            System.out.print(e);
            e.printStackTrace();
        }

        
        System.out.println("\n\n-----------------------\nDone!\n-----------------------");

  } 

    /**
     * Merge method used for the merge sort
     * @param array Array (or sub-array) to be sorted
     * @param left index
     * @param mid index
     * @param right index
     */
    static void merge(int array[], int left, int mid, int right)
    {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Split into smaller arrays
        int leftArray[] = new int[n1];
        int rightArray[] = new int[n2];
       
        // Copy data to sub-arrays
        for (int i = 0; i < n1; ++i)
            leftArray[i] = array[left + i];
        for (int j = 0; j < n2; ++j)
            rightArray[j] = array[mid + 1 + j];
 
        // Index
        int i = 0, j = 0;
        int k = left;

        // Merge
        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            }
            else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }

        // Copy leftovers (leftArray)
        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }
        // Copy leftovers (rightArray)
        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }      
    }
    /**
     * Recursive sorting function. Utilizes multithreading.
     * @param array Array (or sub-array) to be sorted
     * @param left Index
     * @param right Index
     * @param fileWriter Output file
     * @throws IOException
     */
    static void sort(int array[], int left, int right, FileWriter fileWriter) throws IOException
    {  
        Thread thread_test_01;
        thread_test_01 = new Thread(new Runnable(){
            public void run(){
                try{
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
        
        // Print out progress to console and output file
        try {
            thread_test_01.start();
            System.out.print(thread_test_01.getName() + " started\n");
            fileWriter.write(thread_test_01.getName() + " started\n");
       
            thread_test_01.join();
            System.out.print(thread_test_01.getName() + " finished: ");
            fileWriter.write(thread_test_01.getName() + " finished: ");
        } catch (InterruptedException ex) {
            Logger.getLogger(MergeSort_COEN346.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(int i =left; i<=right; ++i){
            System.out.print(array[i]+" ");
            fileWriter.write(array[i]+" ");
        }
            System.out.println();
            fileWriter.write("\n");
    }        
    
    /**
     * This function is used to read from an text file.
     * @param fileName
     * @return List<String> of the text contained in the file specified
     */
    public static List<String> readInputFile(String fileName)
    {
        List<String> inputLines = Collections.emptyList(); 

        // Get Working Directory. Code is one folder lower.
        String workingDirectory = Paths.get("").toAbsolutePath().toString();

        try
        { 
            inputLines = Files.readAllLines(Paths.get(workingDirectory, fileName), StandardCharsets.UTF_8); 
        } 
  
        catch (IOException exception) 
        {   
            exception.printStackTrace(); 
        } 
        return inputLines;
    }

    /**
     * Convert a list of string to a list of integers
     * @param stringList
     * @return integerList
     */
    public static List<Integer> convertStringListToIntList(List<String> stringList)
    {
        List<Integer> intList = new ArrayList<Integer>(stringList.size());

        try
        { 
            for(String s : stringList) intList.add(Integer.parseInt(s));
        } 
  
        catch (NumberFormatException exception) 
        {   
            exception.printStackTrace(); 
        } 
        return intList;
    }
   
    /**
     * Print an array of integers to the console
     * @param arr Array to be printed
     */
    static void printArray(int arr[])
    {
        int n = arr.length;
        for (int i = 0; i < n; ++i)
            System.out.print(arr[i] + " ");
    }

}
