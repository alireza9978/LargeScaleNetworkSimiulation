package constants;

public class Constants {

    public static final int MAX_SERVER_COUNT = 5;
    public static final int MAX_VM_IN_SINGLE_SERVER_COUNT = 10;
    public static final int MAX_SWITCH_COUNT = 1000;

    public static final int MAX_NODE_COUNT = 27_000;
    // we have 90 edge node maxNodeCount must be dividable by 90
    public static final int MINIMUM_ACTIVE_NODE_COUNT = 1080;
    // maxNodeCount - minimumActiveNodeCount must be dividable by 24*12

    public static final int SWITCH_MAX_CONNECTION_COUNT = 32;
    public static final int SERVER_MAX_VM_COUNT = 10;

    // packets that each server can process in a second
    public static final int SERVER_MAX_PROCESSING_PACKET_IN_SECOND = 15000;

    // packets that each vm can process in a second
    public static final int MAX_VM_PACKET_COUNT_PROCESS_SPEED = 5000;

    public static final int MAX_BUFFER_PACKET_COUNT = 10000;

    // classes packets size in b
    public static final int CLASS_ONE_SIZE = 4000;
    public static final int CLASS_TWO_SIZE = 2000;
    public static final int CLASS_THREE_SIZE = 2000;
    public static final int CLASS_FOUR_SIZE = 4000;

    // classes packets count in second
    public static final int CLASS_ONE_COUNT = 1;
    public static final int CLASS_TWO_COUNT = 2;
    public static final int CLASS_THREE_COUNT = 4;
    public static final int CLASS_FOUR_COUNT = 1;

    // link speeds
    public static final long SWITCH_LINK_SPEED_SLOW = 30_000_000L;
    public static final String SWITCH_LINK_SPEED_SLOW_NAME = "10Mb";
    public static final long SWITCH_LINK_SPEED_FAST = 40_000_000L;
    public static final String SWITCH_LINK_SPEED_FAST_NAME = "20Mb";
    public static final long SERVER_LINK_SPEED = 50_000_000L;
    public static final String SERVER_LINK_SPEED_NAME = "50Mb";

    public static final long MAX_LINK_SPEED = Math.max(SERVER_LINK_SPEED, Math.max(SWITCH_LINK_SPEED_SLOW, SWITCH_LINK_SPEED_FAST));
    public static final int GCD_CLASSES_PACKET_SIZE = gcd(gcd(gcd(CLASS_ONE_SIZE, CLASS_TWO_SIZE), CLASS_THREE_SIZE), CLASS_FOUR_SIZE);

    public static final long RATIO = 12;
    public static long CLOCK_IN_SECOND = MAX_LINK_SPEED / GCD_CLASSES_PACKET_SIZE;

    public static final long TOTAL_CLOCK_COUNT = 24L * 60L * 60L * CLOCK_IN_SECOND / RATIO;
    public static final int LINK_SPEED_PER_CLOCK = GCD_CLASSES_PACKET_SIZE;
    public static final long ONE_HOUR_CLOCK_COUNT = 60L * 60L * CLOCK_IN_SECOND / RATIO;
    public static final long FIVE_MINUTE_CLOCK_COUNT = 5L * 60L * CLOCK_IN_SECOND / RATIO;

    // classes packets generation speed in clock
    public static final int CLASS_ONE_CYCLE = (int) (CLOCK_IN_SECOND / CLASS_ONE_COUNT);
    public static final int CLASS_TWO_CYCLE = (int) (CLOCK_IN_SECOND / CLASS_TWO_COUNT);
    public static final int CLASS_THREE_CYCLE = (int) (CLOCK_IN_SECOND / CLASS_THREE_COUNT);
    public static final int CLASS_FOUR_CYCLE = (int) (CLOCK_IN_SECOND / CLASS_FOUR_COUNT);

    public static final String FIGURE_DIR = "src/main/resources/charts/";
    public static final String GRAPH_DIR = "src/main/resources/graphs/";
    public static final String ROOT_DIR = "/home/alireza/projects/java/largeScaleNetworkSimulation/";


    // Recursive function to return gcd of a and b
    static int gcd(int a, int b) {
        // Everything divides 0
        if (a == 0)
            return b;
        if (b == 0)
            return a;

        // base case
        if (a == b)
            return a;

        // a is greater
        if (a > b)
            return gcd(a - b, b);
        return gcd(a, b - a);
    }

}
