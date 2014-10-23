import javax.crypto.SealedObject;
import java.util.*;
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
 * Main
 *
 * Main launches an arbitrary, user defined amount of threads (less than 101) known as valves.
 * It handles all user input, and many calculations needed.
 *
 * Main will take in all user input and spawn threads based on the amount entered by the user.
 * It also has various housekeeping methods that facilitate the program as a whole.
 *
 * This class requires SemaphoresUtil.java and Valve.java to run.
 *
 * @author Aaron Weaver         (waaronl@okstate.edu)
 * @version 1.0
 * @since 2014-10-23
 */
public class Main
{
    private static boolean priorityDone = false;
    private static int numPriorityValves;
    private static int countNum = 0;

    /**
     * Handles basic user input for number of valves, wrenches, and the magic number.
     * Then spawns the appropriate amount of threads based on user input.
     *
     * @param args
     *          - not used
     */
    public static void main(String[] args)
    {
        int numValves = 0;
        int numWrench = 0;
        int magicNum = 0;
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter the amount of Valves: ");
        numValves = scan.nextInt();

        if(numValves <= 5 || numValves > 100 )
        {
            System.out.println("The number of valves must be greater than 5 and less than 101.");
            System.exit(1);
        }

        System.out.println("Enter the amount of Wrenches: ");
        numWrench = scan.nextInt();

        if(numWrench <= 3 || numWrench > 20 || numWrench > numValves)
        {
            System.out.println("The number of wrenches must be greater than 3 and less than 21, and cannot be greater than number of valves.");
            System.exit(1);
        }

        System.out.println("Enter the \"Magic Number\" you wish to use: ");
        magicNum = scan.nextInt();

        if(magicNum < 0 || magicNum > 9)
        {
            System.out.println("The magic number must be a non-negative number less than 10.");
            System.exit(1);
        }

        SemaphoresUtil.T_Number_Valves = numValves;
        SemaphoresUtil.T_Number_Wrenches = numWrench;
        SemaphoresUtil.T_Magic_Number = magicNum;

        fillOddsAndEvens();

        HashMap<Integer, Thread> threadMap = new HashMap<Integer, Thread>();

        for(int i = 0; i < SemaphoresUtil.T_Number_Valves; i++)
        {
            Thread thread = new Thread(new Valve(i + 1));
            threadMap.put(i + 1, thread);
        }

        startValveOpeningSequence(threadMap);
    }

    /**
     * Starts the valve opening sequence within the threads.
     * NOTE: This does not synchronize the threads, this only sets in motion the ability for
     * the utilities that the threads will use to synchronize.
     *
     * @param threadMap
     *          - the map of all of the threads used.
     */
    protected static void startValveOpeningSequence(HashMap<Integer, Thread> threadMap)
    {
        if(SemaphoresUtil.T_Magic_Number > 2)
        {
            numPriorityValves = findNumPriorityValves();
        }
        getRandomEven();
        for(int i = 0; i < threadMap.size(); i++)
        {
            threadMap.get(i + 1).start();
        }
    }

    /**
     * Finds the number of priority valves based on the magic number entered by the user.
     *
     * @return
     *      - The count of how many priority valves that will be necessary
     */
    public static int findNumPriorityValves()
    {
        int count = 0;
        for(int i = 1; i <= SemaphoresUtil.T_Number_Valves; i++)
        {
            if(i % SemaphoresUtil.T_Magic_Number == 0 && i % 2 == 0)
            {
                count++;
            }
        }
        return count;
    }

    /**
     * Function used to retrieve a completely (pseudo)-random even number to allow
     * for random opening of even numbered valves.
     *
     * NOTE: This function takes into account prioritized valves.
     */
    public static void getRandomEven()
    {
        int maxEven = SemaphoresUtil.T_Number_Valves;
        Random rand = new Random();
        int randomNum = 0;

        while(true)
        {
            if(SemaphoresUtil.globalDebug)
            {
                System.out.println("GETTING RANDOM EVEN...");
            }
            randomNum = rand.nextInt(maxEven + 1);
            if(SemaphoresUtil.T_Magic_Number <= 2)
            {
                priorityDone = true;
            }
            if(!priorityDone)
            {
                if (SemaphoresUtil.T_Magic_Number > 2)
                {
                    if(numPriorityValves == 0)
                    {
                        priorityDone = true;
                    }

                    if(SemaphoresUtil.usedEvens.contains(randomNum))
                    {
                        if(SemaphoresUtil.globalDebug)
                        {
                            System.out.println("NumPriorityValves: " + numPriorityValves);
                            System.out.println("countNum: " + countNum);
                        }
                        continue;
                    }
                    else if (randomNum % SemaphoresUtil.T_Magic_Number == 0 && randomNum % 2 == 0 && randomNum > 0)
                    {
                        SemaphoresUtil.Global_Even = randomNum;
                        SemaphoresUtil.usedEvens.add(randomNum);
                        if(SemaphoresUtil.globalDebug)
                        {
                            System.out.println("GLOBAL EVEN AT MAGIC NUM: " + SemaphoresUtil.Global_Even);
                        }
                        countNum++;
                        if(countNum == numPriorityValves)
                        {
                            priorityDone = true;
                        }
                        break;
                    }
                }
            }

            if(priorityDone)
            {
                if (SemaphoresUtil.usedEvens.size() == SemaphoresUtil.evenValves.size())
                {
                    if(SemaphoresUtil.globalDebug)
                    {
                        System.out.println("Releasing odds");
                    }
                    SemaphoresUtil.odds.release();
                    break;
                }
                if (randomNum % 2 != 0)
                {
                    continue;
                }
                if (SemaphoresUtil.usedEvens.contains(randomNum))
                {
                    continue;
                }
                if (randomNum == 0)
                {
                    continue;
                }

                SemaphoresUtil.Global_Even = randomNum;
                SemaphoresUtil.usedEvens.add(randomNum);
                if(SemaphoresUtil.globalDebug)
                {
                    System.out.println("GLOBAL EVEN : " + SemaphoresUtil.Global_Even);
                }
                break;
            }
        }
    }

    /**
     * Fills the odd and even array lists with appropriate valves.
     */
    private static void fillOddsAndEvens()
    {
        ArrayList<Integer> evenList = new ArrayList<Integer>();
        ArrayList<Integer> oddList = new ArrayList<Integer>();

        for(int i = 1; i <= SemaphoresUtil.T_Number_Valves; i++)
        {
            if(i % 2 == 0)
            {
                evenList.add(i);
            }
            else
            {
                oddList.add(i);
            }
        }

        SemaphoresUtil.evenValves = evenList;
        SemaphoresUtil.oddValves = oddList;
    }
}
