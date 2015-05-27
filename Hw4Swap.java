
/**
 * CS149 HW4
 * @author CherylRuo, Adrian Lin, Zeran Wang
 */
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Hw4Swap
{
    static final int TIME_FOR_EACH_RUN = 60; // 60 minutes
    
    static int[] mainMemory = new int[100]; // 100 megabytes
    static ArrayList<Process> processesExecuting = new ArrayList<Process>();

    static List<Process> processQueue = new LinkedList<Process>();   
    static String[] memoryMap = new String[100];
    
    static void clear()
    {
        mainMemory = new int[100];
        processesExecuting = new ArrayList<Process>();
        processQueue = new LinkedList<Process>();
        memoryMap = new String[100];
    }

    /**
     * 
     * @param amount 
     */
    static void generateProcesses(int amount)
    {
        int amountToRepeat = amount / 20; //
        int[] processSizes =
        {
            5, 11, 17, 31
        };
        int[] processDurations =
        {
            1, 2, 3, 4, 5
        };

        int processCounter = 0;
        for (int k = 0; k < amountToRepeat; k++)
        {
            for (int i = 0; i < processSizes.length; i++)
            {
                for (int j = 0; j < processDurations.length; j++)
                {
                    String currentProcessName = "P" + processCounter;
                    Process p = new Process(currentProcessName, processSizes[i], processDurations[j]);
                    processQueue.add(p);
                    processCounter++;
                }
            }
        }
        Collections.shuffle(processQueue);
    }
   /**
    * Allocates memory from the first hole that is large enough to fit.
    * Starts from where the last allocation was.
    * @return 
    */
    // first fit
    
    static int runFirstFit()
    {
    	   int swappedIn = 0;
           int location = 0;
           Process currentProcess;
           for (int currentTime = 0; currentTime < Hw4Swap.TIME_FOR_EACH_RUN; currentTime++)
           {
               
               currentProcess = processQueue.get(0);
               if (nextFit_processFits(currentProcess, location))
               {
                   location = allocate(currentProcess, location) % 100; //
                   processQueue.remove(0);
                   swappedIn++;
                   //System.out.println("\n\nFirst Fit Algorithm");
                   System.out.println(currentProcess.name + " swapped in. Size: " + 
                           currentProcess.size + ". Duration: " + currentProcess.duration);
                   printMemoryMap();
                   location = updateLocation(location);
                   updateProcessDurations();
                   location = 0;
               }
               else
               {
            	   location = updateLocation(location);
            	   updateProcessDurations();
               }
           }
           return swappedIn;
    }
    
    // shared method for First Fit and Next Fit to find next location which fits
    static boolean nextFit_processFits(Process process, int location)
    {
        int end = process.size + location;
        if (end > 100)
            return false;
        for (int i = location; i < end; i++)
        {
            if (mainMemory[i] == 1)
                return false;
        }
        return true;
    }
    static int runNextFit()
    {
        int swappedIn = 0;
        int location = 0;
        Process currentProcess;
        for (int currentTime = 0; currentTime < Hw4Swap.TIME_FOR_EACH_RUN; currentTime++)
        {
            
            currentProcess = processQueue.get(0);
            if (nextFit_processFits(currentProcess, location))
            {
                location = allocate(currentProcess, location) % 100; //
                processQueue.remove(0);
                swappedIn++;
                //System.out.println("\n\nNext Fit Algorithm");
                System.out.println(currentProcess.name + " swapped in. Size: " + 
                        currentProcess.size + ". Duration: " + currentProcess.duration);
                printMemoryMap();
            }
            location = updateLocation(location);
            updateProcessDurations();
        }
        return swappedIn;
    }
    
    // best fit
    static int findBestFit(Process process)
    {
        int count = 0;       
        int preCount = 100;
        int location = 0;
        for (int i = 0; i < 100; i++)
        {
            if (mainMemory[i] == 1)
            {
                if(count >= process.size)
                {
                    preCount = Math.min(preCount, count);
                    location = i-count; 
                }
                count = 0;
            }
            else
            {
                count++;
            }               
        }
        return location;      
    }
    
    static int runBestFit()
    {
        int swappedIn = 0;
        Process currentProcess;
        for (int currentTime = 0; currentTime < Hw4Swap.TIME_FOR_EACH_RUN; currentTime++)
        {       
            currentProcess = processQueue.get(0);
            int value = findBestFit(currentProcess);
            if (value >= 0 && value <100)
            {
                allocate(currentProcess, findBestFit(currentProcess)); //
                processQueue.remove(0);
                swappedIn++;
                //System.out.println("\n\nBest Fit Algorithm");
                System.out.println(currentProcess.name + " swapped in. Size: " + 
                            currentProcess.size + ". Duration: " + currentProcess.duration);
                printMemoryMap();
            }
                updateProcessDurations();
        }
        return swappedIn;
    }
    
    static int allocate(Process process, int location)
    {
        int end = process.size + location;
        for (int i = location; i < end; i++)
        {
            mainMemory[i] = 1;
            memoryMap[i] = process.name;
        }
        processesExecuting.add(process);
        return end;
    }
    
    static void deallocate(Process process)
    {
        for (int i = 0; i < memoryMap.length; i++)
        {
            if (memoryMap[i].equals(process.name))
            {
                memoryMap[i] = ".";
                mainMemory[i] = 0;
            }
        }
    }
    
    /**
     * One second has passed.
     */
    static void updateProcessDurations()
    {
        Process currentProcess;
        for (int i = 0; i < processesExecuting.size(); i++)
        {
            currentProcess = processesExecuting.get(i);
            currentProcess.duration--;
            if (currentProcess.duration == 0)
            {
                processesExecuting.remove(i);
                i--; //
                deallocate(currentProcess);
                System.out.println(currentProcess.name + " swapped out.");
                printMemoryMap();
            }
        }
    }
    
    static int updateLocation(int location)
    {
        int counter = 0; // preventing an infinite loop
        int newLocation = location;
        if (mainMemory[newLocation] == 0)
            newLocation = 0;
        while (mainMemory[newLocation] == 1)
        {
            newLocation++;
            counter++;
            if (newLocation == 100)
                newLocation = 0;
            if (counter == 100)
                break;
        }
        return newLocation;
    }
    
    
    static void printProcesses()
    {
        Iterator<Process> li = processQueue.iterator();
        while (li.hasNext())
        {
            Process currentp = li.next();
            System.out.println("Process name: " + currentp.name + " Process size: " + 
                    currentp.size + " Process duration: " + currentp.duration);
        }
    }
    
    static void initializeMemoryMap()
    {
        for (int i = 0; i < memoryMap.length; i++)
            memoryMap[i] = ".";
    }
    
    static void printMemoryMap()
    {
        System.out.println("Memory map:");
        for (int i = 0; i < memoryMap.length; i++)
            System.out.print(memoryMap[i]);
        System.out.println();
    }

    public static void main(String[] args)
    {
        int sumOfSwapInsFF = 0;
        int sumOfSwapInsNF = 0;
        int sumOfSwapInsBF = 0;

        int avgFF;
        int avgNF;
        int avgBF;

        for (int i = 0; i < 5; i++)
        {
            System.out.println("First fit run: " + (i + 1) + " ****************************");
            initializeMemoryMap();
            //printMemoryMap();
            generateProcesses(160);
            //printProcesses();
            sumOfSwapInsFF += runFirstFit();
            clear();
			System.out.println();
        }
        for (int i = 0; i < 5; i++)
        {
            System.out.println("Next fit run: " + (i + 1) + " ****************************");
            initializeMemoryMap();
            //printMemoryMap();
            generateProcesses(160);
            //printProcesses();
            sumOfSwapInsNF += runNextFit();
            clear();
			System.out.println();
        }
        for (int i = 0; i < 5; i++)
        {
            System.out.println("Best fit run: " + (i + 1) + " ****************************");
            initializeMemoryMap();
            //printMemoryMap();
            generateProcesses(160);
            //printProcesses();
            sumOfSwapInsBF += runBestFit();
            clear();
			System.out.println();
        }
        avgFF = sumOfSwapInsFF / 5;
        avgNF = sumOfSwapInsNF / 5;
        avgBF = sumOfSwapInsBF / 5;
        
        System.out.println("Average swap ins of First Fit after 5 runs: " + avgFF);
        System.out.println("Average swap ins of Next Fit after 5 runs: " + avgNF);
        System.out.println("Average swap ins of Best Fit after 5 runs: " + avgBF);
    }
}