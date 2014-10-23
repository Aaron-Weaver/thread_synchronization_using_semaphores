import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Semaphore;

/**
 * =================================== Program Two =============================================
 * Name: Aaron Weaver
 * Student Name: waaronl
 * Student ID: 0211
 * Date: 2014-10-23
 * Instructor: Dr. A. T. Burrell
 * Class: CS4323 Des. & Impl. of Operating Systems
 * Assignment: Programming Homework 2
 *
 * Valve
 *
 * Valve is the meat of program two, where the threads are synchronized using semaphores.
 *
 * Valve Starts by blocking all odd valves and only allowing for the evens to go through.
 * The even valves are opened in a random order, with magic number modulus defined by the
 * user taking precedence. After the evens are done opening, the odds are then opened based
 * on two criteria. 1: They are to go in descending order 2: Based on the number of wrenches, there
 * can be that many valves opened at random, as long as the descending order is kept (i.e. the lowest number
 * of the first set of concurrent wrenches is still higher than the highest of the second set)
 * After all of the valves have opened, the threads then close, and the program exits.
 *
 * This class requires Main.java and SemaphoresUtil.java to run.
 *
 * @author Aaron Weaver         (waaronl@okstate.edu)
 * @since 2014-10-23
 * @version 1.0
 */
public class Valve implements Runnable
{
    /* Declare variables */
    private static boolean reachedEnd = false;
    private static boolean calculatedHighestOdds = false;
    private static ArrayList<Integer> highestOdds = new ArrayList<Integer>(0);
    private int valveId;
    private long threadId;
    private static int evenCount = 0;
    private static int oddCount = 0;
    private static int wrenchCount = 0;
    private static int currentValveId = 0;
    private static int greatestValveId = 0;

    /**
     * Constructor to set valveId
     *
     * @param valveId
     *          - ID of this particular valve.
     */
    public Valve(int valveId)
    {
        this.valveId = valveId;
    }

    /**
     * @see java.lang.Runnable
     *
     * This is an overrided method from java's Runnable class that allows for Threading to be
     * possible.
     *
     * This is where all thread logic, including use of semaphores and critical sections, is handled.
     */
    @Override
    public void run()
    {
        threadId = Thread.currentThread().getId();
        try
        {
            SemaphoresUtil.mutex.acquire();
            if(valveId % 2 == 1)
            {
                /* Block odd threads until evens are done */
                SemaphoresUtil.mutex.release();
                SemaphoresUtil.odds.acquire();
                SemaphoresUtil.mutex.acquire();

                if(!calculatedHighestOdds)
                {
                    SemaphoresUtil.mutex.release();
                    /* Calculate the highest amount of odds for this set of concurrent wrenches */
                    highestOdds = getHighestOddValves();
                    calculatedHighestOdds = true;
                    for(int i = 0; i < highestOdds.size(); i++)
                    {
                        if(SemaphoresUtil.globalDebug)
                        {
                            System.out.printf("This is a highest odd %d \n", highestOdds.get(i));
                        }
                    }
                    SemaphoresUtil.mutex.acquire();
                }

                /* Block until a valve contained within the set of highest odds is executed */
                while(!highestOdds.contains(valveId))
                {
                    oddCount++;
                    SemaphoresUtil.mutex.release();
                    SemaphoresUtil.odds.release();
                    SemaphoresUtil.mutex.acquire();
                    oddCount--;
                }

                if(SemaphoresUtil.globalDebug)
                {
                    System.out.println("I AM OVER HERE");
                }
            }
            if(valveId % 2 == 0)
            {
                /* Block until the thread determined randomly is executed */
                while (SemaphoresUtil.Global_Even != valveId)
                {
                    evenCount++;
                    SemaphoresUtil.mutex.release();
                    SemaphoresUtil.evens.acquire();
                    SemaphoresUtil.mutex.acquire();
                    evenCount--;
                }
            }

            /* Critical section call for opening valve */
            SemaphoresUtil.Open_Valve(this.valveId, threadId);

            /* If odd valves, this removes an odd valve from the highest odds, signifying it has opened */
            if(valveId % 2 == 1 && highestOdds.size() > 0 && highestOdds.indexOf(this.valveId) < highestOdds.size())
            {
                try {
                    SemaphoresUtil.mutex.release();
                    if(SemaphoresUtil.globalDebug)
                    {
                        System.out.println("HIGHEST ODDS REMOVE");
                    }
                    highestOdds.remove(highestOdds.indexOf(this.valveId));
                    SemaphoresUtil.mutex.acquire();
                } catch(ArrayIndexOutOfBoundsException e)
                {
                    if(SemaphoresUtil.globalDebug)
                    {
                        e.printStackTrace();
                    }
                }
            }

            /* Gets a new random valve for the evens */
            if(valveId % 2 == 0)
            {
                SemaphoresUtil.mutex.release();
                Main.getRandomEven();
                SemaphoresUtil.mutex.acquire();
            }

            SemaphoresUtil.mutex.release();
            /* Allows for concurrency with a set of wrenches */
            if(!highestOdds.isEmpty() && valveId % 2 == 1) {
                //System.out.println("Odd Count");
                SemaphoresUtil.odds.release();
            }

            /* Recalculates highest odds after the current set of concurrent wrenches is finished */
            if(highestOdds.isEmpty() && calculatedHighestOdds)
            {
                SemaphoresUtil.mutex.release();
                highestOdds = getHighestOddValves();
                SemaphoresUtil.mutex.acquire();
            }

            /* Unblocks evens after random even valve is opened */
            while(evenCount > 0)
            {
                SemaphoresUtil.evens.release();
            }
            if(SemaphoresUtil.globalDebug)
            {
                System.out.println("I made it after release");
            }
        } catch (InterruptedException e)
        {
            if(SemaphoresUtil.globalDebug)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves the highest odd valves based on the number of wrenches defined by the user.
     * If the user defines four wrenches, this method will return the 4 highest numbered valves.
     *
     * @return
     *      - List containing highest number of valves within the amount of wrenches available
     */
    public ArrayList<Integer> getHighestOddValves()
    {
        int numWrench = SemaphoresUtil.T_Number_Wrenches;
        int numOdds = SemaphoresUtil.oddValves.size();
        int greatest = 0;
        int current = 0;

        ArrayList<Integer> highestValves = new ArrayList<Integer>();

        for(int i = 0; i < numWrench; i++)
        {
            for (int j = 0; j < numOdds; j++)
            {
                current = SemaphoresUtil.oddValves.get(j);
                if(SemaphoresUtil.usedOdds.size() == SemaphoresUtil.oddValves.size())
                {
                    reachedEnd = true;
                    break;
                }
                if(!SemaphoresUtil.usedOdds.contains(current) && current > greatest)
                {
                    if(SemaphoresUtil.globalDebug)
                    {
                        System.out.println("CURRENT GREATEST = " + greatest);
                    }
                    greatest = current;
                }
            }
            if(reachedEnd)
            {
                break;
            }
            SemaphoresUtil.usedOdds.add(greatest);
            highestValves.add(greatest);
            greatest = 0;
            if(SemaphoresUtil.globalDebug)
            {
                System.out.println("VALUE OF I = " + i);
                System.out.println("VALUE OF HIGHESTODD AT I = " + highestValves.get(i));
            }
        }

        return highestValves;
    }
}
