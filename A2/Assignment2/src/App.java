package COEN346_Pro_02;

import java.util.*; 
import java.nio.charset.StandardCharsets; 
import java.nio.file.*; 
import java.io.*; 

public class App {

    // ---- GLOBAL VARS ----
    // Quantum Time
    private static int QUANTUM_TIME;
    private static final ArrayList<User> users = new ArrayList<User>();

    public static void main(String[] args) throws Exception {

        // Read input file. This also sets the quantum time and the users
        //readInputFile("input.txt");

        // DO MORE STUFF
        
        //testing algorithm
        ArrayList<Process> plist = createProcessList();
        Scheduler sch = new Scheduler(1,2);
        plist.forEach(p -> {
            sch.addProcess(p);
        });
        //hardcode the total service time
        sch.setTotalServiceTime(30);
        sch.start();
        
    }

    public static void readInputFile(String fileName) {
        List<String> inputLines = Collections.emptyList();

        // Get Working Directory. Code is one folder lower.
        String workingDirectory = Paths.get("src").toAbsolutePath().toString();

        try {
            inputLines = Files.readAllLines(Paths.get(workingDirectory, fileName), StandardCharsets.UTF_8);
        }

        catch (IOException exception) {
        }

        int inputSize = inputLines.size();
        int lineCounter = 0;

        // Reading Quantum Time
        QUANTUM_TIME = Integer.parseInt(inputLines.get(lineCounter++));
        System.out.print("QUANTUM TIME: " + QUANTUM_TIME + "\n");

        if(inputSize > 1){
            while(lineCounter < inputSize){
                User newUser = new User(inputLines.get(lineCounter++));
                for (int i = 0; i < newUser.getProcessCount(); i++) {
                    newUser.addProcess(new Process(inputLines.get(lineCounter++)));
                }
                users.add(newUser);
                newUser.printUser();
            }
        }
    } 
    
    // testing algorithm
    public static ArrayList<Process> createProcessList(){
        Process p1=new Process(1, 5, 0);
        Process p2=new Process(8, 5, 1);
        Process p3=new Process(15, 5, 2);
        ArrayList<Process> plist = new ArrayList<>();
        plist.add(p1);
        plist.add(p2);
        plist.add(p3);
        return plist;
    }
    
    public  static void printMsg(String s){
       System.out.println(s);
   }

   

}
