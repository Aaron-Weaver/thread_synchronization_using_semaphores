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
 * SemaphoresUtil
 *
 * SemaphoresUtil handles utility primitives and objects, as well as containing the global Open_Valve
 * function called by each Valve.
 *
 * @author Aaron Weaver         (waaronl@okstate.edu)
 * @version 1.0
 * @since 2014-10-23
 */
public class SemaphoresUtil
{
    /* User input values */
    public static int T_Number_Valves;
    public static int T_Number_Wrenches;
    public static int T_Magic_Number;

    /* Date formatting tools */
    private static DateFormat timeFormat = new SimpleDateFormat("HH:mm a");
    private static DateFormat dateFormat = new SimpleDateFormat("EEEE MMMM dd, yyyy");
    private static String threadDateAndTime;

    /* Keeps track of even chosen at random */
    public static int Global_Even;

    /* Declare Semaphores */
    public static Semaphore mutex = new Semaphore(1);
    public static Semaphore evens = new Semaphore(0);
    public static Semaphore odds = new Semaphore(0);

    /* Debug boolean, turn true for full command line report, or false for only functional output*/
    public static boolean globalDebug = false;

    /* Keeps track of used odds and evens separately for the posterity of valves */
    public static ArrayList<Integer> usedEvens = new ArrayList<Integer>();
    public static ArrayList<Integer> usedOdds = new ArrayList<Integer>();

    /* Keeps track of all odd and even valves possible */
    public static ArrayList<Integer> oddValves;
    public static ArrayList<Integer> evenValves;

    /* The global function "critical section" that is called to open valves */
    public static void Open_Valve(int valveId, long threadId)
    {
        Date date = new Date();
        threadDateAndTime = timeFormat.format(date) + " on " + dateFormat.format(date);
        System.out.println("Thread " + valveId + ", Thread ID " + threadId + " Opened Valve " + valveId + " at "  + threadDateAndTime + ".\n");
    }
}
