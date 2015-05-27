
import java.util.Random;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Hw4Page
{

    static PageReference[] memory = new PageReference[4]; // memory holds up to 4 pages frames
    static PageReference[] pageReferences = new PageReference[100]; // 100 page references
    static int pagesInMemory = 0;
    static int currentPageReference = 0;

    static Queue<Integer> memory_cheryl = new LinkedList<Integer>();
    static List<Integer> memory1_cheryl = new LinkedList<Integer>();

    static void clear()
    {
        for (int i = 0; i < memory.length; i++)
        {
            memory[i] = new PageReference(-1);
        }
        pageReferences = new PageReference[100];
        pagesInMemory = 0;
        currentPageReference = 0;
    }

    static void generatePageReferences()
    {
        for (int i = 0; i < pageReferences.length; i++)
        {
            Random r = new Random();
            int a = r.nextInt(10); // 0 to 9
            int delta_i;
            if (0 <= a && a < 7)
            {
                delta_i = r.nextInt((1 - -1) + 1) + -1;
            } else
            {
                delta_i = r.nextInt((8 - 2) + 1) + 2;
            }
            int b = a + delta_i;
            if (b < 0)
            {
                pageReferences[i] = new PageReference(b + 10);
            } else
            {
                pageReferences[i] = new PageReference(b % 10);
            }
        }
    }

    // Cheryl
    static int generateRandomNo(int value)
    {
        int randomNo = 0 + (int) (Math.random() * 9);

        if (randomNo >= 0 && randomNo < 7)
        {
            if (value == 0)
            {
                randomNo = 0 + (int) (Math.random() * 1);
            } else
            {
                if (value == 9)
                {
                    randomNo = 8 + (int) (Math.random() * 1);
                } else
                {
                    randomNo = (value - 1) + (int) (Math.random() * 2);
                }
            }
        } else    // 7 =< randomNo <= 9
        {
            randomNo = 0 + (int) (Math.random() * 9);
            while (Math.abs(randomNo - value) <= 1)
            {
                randomNo = 0 + (int) (Math.random() * 9);
            }
        }
        return randomNo;
    }

    static void printMemory()
    {
        for (int i = 0; i < memory.length; i++)
        {
            System.out.print(memory[i].data + " ");
        }
        System.out.println("");
    }

    static void printMemoryCheryl()
    {
        Iterator<Integer> iter = memory_cheryl.iterator();
        while (iter.hasNext())
        {
            int current = iter.next();
            System.out.print(current + " ");
        }
        System.out.println("");
    }

    // Cheryl
    static double runFIFO(int value)
    {

        double count = 0;

        if (!memory_cheryl.contains(value))
        {
            if (memory_cheryl.size() < 4)
            {
                printMemoryCheryl();
                memory_cheryl.add(value);
                //System.out.print("No page out. ");
                //System.out.print("Page in: " + value + ". ");
                System.out.println(value + " is going to be paged in");
                System.out.println();
            } else
            {
                printMemoryCheryl();
                //System.out.print("Page out: " + memory_cheryl.peek() + ". ");
                System.out.println(memory_cheryl.peek() + " evicted");
                memory_cheryl.poll();
                memory_cheryl.add(value);
                //System.out.print("Page in: " + value + ". ");
                System.out.println(value + " is going to be paged in");
                System.out.println();
            }
        } else
        {
            count++;
            //System.out.print("No page in or out. ");
        }
        //System.out.println("The number of pages in memory: " + memory_cheryl.size() + ".");
        return count;
    }

    // Cheryl
    static double runLRU(int value)
    {
        int count = 0;

        if (!memory1_cheryl.contains(value))
        {
            if (memory1_cheryl.size() < 4)
            {
                memory1_cheryl.add(0, value);
                //System.out.print("No page out. ");
                //System.out.print("Page in: " + value + ". ");
                System.out.println(value + " is going to be paged in");
                System.out.println();
            } else
            {
                printMemoryCheryl();
                //System.out.print("Page out: " + memory1_cheryl.get(3) + ". ");
                System.out.println(memory_cheryl.peek() + " evicted");
                memory1_cheryl.remove(3);
                memory1_cheryl.add(0, value);
                //System.out.print("Page in: " + value + ". ");
                System.out.println(value + " is going to be paged in");
                System.out.println();
            }
        } else
        {
            memory1_cheryl.remove(memory1_cheryl.indexOf(value));
            memory1_cheryl.add(0, value);
            count++;
            //System.out.print("No page in or out. ");
        }
        //System.out.println("The number of pages in memory: " + memory_cheryl.size()+ ".");
        return count;
    }

    /**
     * Least Frequently Used (LFU) paging algorithm Zeran
     *
     * @return
     */
    static double runLFU()
    {
        double pageHitCount = 0;
        for (int j = 0; j < pageReferences.length; j++)
        {
            PageReference current = pageReferences[currentPageReference];
            printMemory();
            System.out.println(current.data + " is going to be paged in");
            boolean found = false;
            for (int k = 0; k < memory.length; k++)
            {
                if (memory[k].data == current.data) // page hit
                {
                    found = true;
                    pageHitCount++;
                    break; // break for loop, stop searching memory array
                }
            } //memory loop

            // reference not in memory or page fault encountered
            if (found == false)
            {
                //System.out.println("Page_" + current.data + " paged in.");
                if (pagesInMemory < 4)
                {
                    memory[pagesInMemory] = current;
                    // Increment the frequency
                    memory[pagesInMemory].counter++;
                    pagesInMemory++;
                } else //RAM is full
                {
                    // now find reference with smallest frequency
                    int index = 0;
                    int frequency = memory[0].counter;

                    for (int i = 0; i < memory.length; i++)
                    {
                        if (memory[i].counter < frequency)
                        {
                            index = i;
                            frequency = memory[i].counter;
                        }
                    }
    				//now we have the index indicating the page reference that is least frequently used
                    //System.out.println("Page_" + memory[index].data + " evicted.");
                    System.out.println(memory[index].data + " evicted");
                    memory[index] = current;
                    memory[index].counter++;
                }
            } // if evict existing block or not loop

            currentPageReference++;
            System.out.println();
        } // the 100 pageReferences loop
        return pageHitCount / 100;
    } // runLFU

    static double runMFU()
    {
        double pagesAlreadyInMemoryCount = 0;

        for (int i = 0; i < pageReferences.length; i++)
        {
            PageReference newPageReference = pageReferences[currentPageReference];
            printMemory();
            System.out.println(newPageReference.data + " is going to be paged in");
            boolean containsPage = false;
            for (int a = 0; a < memory.length; a++)
            {
                if (memory[a].data == newPageReference.data)
                {
                    memory[a].counter++;
                    containsPage = true;

                }
            }
            if (!containsPage)
            {
                if (pagesInMemory < 4)
                {
                    memory[pagesInMemory] = pageReferences[currentPageReference];
                    memory[pagesInMemory].counter++;
                    pagesInMemory++;
                } else
                {
                    int indexMFU = 0;
                    for (int j = 1; j < memory.length; j++)
                    {
                        if (memory[indexMFU].counter < memory[j].counter)
                        {
                            indexMFU = j;
                        }
                    }
                    System.out.println(memory[indexMFU].data + " evicted");
                    memory[indexMFU] = newPageReference;
                }
            } else
            {
                pagesAlreadyInMemoryCount++;
            }
            System.out.println();
            currentPageReference++;
        }
        return pagesAlreadyInMemoryCount / 100;
    }

    /**
     * Random pick
     */
    static double runRP()
    {
        double pagesAlreadyInMemoryCount = 0;
        for (int i = 0; i < pageReferences.length; i++)
        {
            PageReference newPageReference = pageReferences[currentPageReference];
            printMemory();
            System.out.println(newPageReference.data + " is going to be paged in");
            boolean containsPage = false;
            for (int a = 0; a < memory.length; a++)
            {
                if (memory[a].data == newPageReference.data)
                {
                    containsPage = true;
                }
            }
            if (!containsPage)
            {
                if (pagesInMemory < 4)
                {
                    memory[pagesInMemory] = pageReferences[currentPageReference];
                    pagesInMemory++;
                } else
                {
                    Random r = new Random();
                    int randomIndex = r.nextInt(4);
                    System.out.println(memory[randomIndex].data + " evicted");
                    memory[randomIndex] = newPageReference;
                }
            } else
            {
                pagesAlreadyInMemoryCount++;
            }
            currentPageReference++;
            System.out.println();
        }
        return pagesAlreadyInMemoryCount / 100;
    }

    // Cheryl
    static double runAlgorithm(String algorithm)
    {
        int count = 0;
        int value = 0 + (int) (Math.random() * 9);
        for (int i = 0; i < 100; i++)
        {
            value = generateRandomNo(value);
            if (algorithm.equals("FIFO"))
            {
                count += runFIFO(value);
            } else
            {
                if (algorithm.equals("LRU"))
                {
                    count += runLRU(value);
                }
            }
            // else if...run other algorithm
        }
        return (double) count / 100;
    }

    public static void main(String[] args)
    {
        double ratio = 0;
        for (int i = 0; i < 5; i++)
        {
            System.out.println("FIFO run " + (i + 1) + " ************************************");
            ratio += runAlgorithm("FIFO");
        }
        double FIFOAvgRatio = ratio / 5;

        ratio = 0;
        for (int i = 0; i < 5; i++)
        {
            System.out.println("LRU run " + (i + 1) + " ************************************");
            ratio += runAlgorithm("LRU");
        }
        double LRUAvgRatio = ratio / 5;

        ratio = 0;
        for (int i = 0; i < 5; i++)
        {
            System.out.println("LFU run " + (i + 1) + " ************************************");
            clear();
            generatePageReferences();
            ratio += runLFU();
            System.out.println();
        }
        double LFUAvgRatio = ratio / 5;

        ratio = 0;
        for (int i = 0; i < 5; i++)
        {
            System.out.println("MFU run " + (i + 1) + " ************************************");
            clear();
            generatePageReferences();
            ratio += runMFU();
            System.out.println();
        }
        double MFUAvgRatio = ratio / 5;

        ratio = 0;
        for (int i = 0; i < 5; i++)
        {
            System.out.println("RP run " + (i + 1) + " ************************************");
            clear();
            generatePageReferences();
            ratio += runRP();
            System.out.println();
        }
        double RPAvgRatio = ratio / 5;

        System.out.println("Average hit ratio FIFO: " + String.format("%.2f", FIFOAvgRatio * 100) + "%");
        System.out.println("Average hit ratio LRU: " + String.format("%.2f", LRUAvgRatio * 100) + "%");
        System.out.println("Average hit ratio LFU: " + String.format("%.2f", LFUAvgRatio * 100) + "%");
        System.out.println("Average hit ratio MFU: " + String.format("%.2f", MFUAvgRatio * 100) + "%");
        System.out.println("Average hit ratio RP: " + String.format("%.2f", RPAvgRatio * 100) + "%");
    }
}
