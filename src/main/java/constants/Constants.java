package constants;

public class Constants {

    public static final int MAX_SERVER_COUNT = 5;
    public static final int MAX_VM_IN_SINGLE_SERVER_COUNT = 10;
    public static final int MAX_SWITCH_COUNT = 1000;

    public static final int MAX_NODE_COUNT = 186_000;
    // we have 186 edge node maxNodeCount must be dividable by 186
    public static final int MINIMUM_ACTIVE_NODE_COUNT = 6_000;
    // maxNodeCount - minimumActiveNodeCount must be dividable by 24*12

    public static final int EDGE_SWITCH_COUNT = 3;
    public static int EDGE_SWITCH_NODE_COUNT = MAX_NODE_COUNT / EDGE_SWITCH_COUNT;
    public static int NODE_COUNT_TOLERANCE = EDGE_SWITCH_NODE_COUNT;

    public static final int SWITCH_MAX_CONNECTION_COUNT = 32;
    public static final int SERVER_MAX_VM_COUNT = 10;
    public static final int SERVER_MAX_PROCESSING_PACKET_IN_HOUR = 120000;
    public static final int SERVER_MAX_PROCESSING_PACKET_IN_SECOND = SERVER_MAX_PROCESSING_PACKET_IN_HOUR / 60;

    // packets that each vm can process in a single cycle
    public static final int MAX_VM_PACKET_COUNT_PROCESS_SPEED = 10000;

    public static final int MAX_BUFFER_PACKET_COUNT = 10000;

    // classes packets size in b
    public static final int CLASS_ONE_SIZE = 1000;
    public static final int CLASS_TWO_SIZE = 2000;
    public static final int CLASS_THREE_SIZE = 4000;
    public static final int CLASS_FOUR_SIZE = 4000;

    // classes packets count in second
    public static final int CLASS_ONE_COUNT = 1;
    public static final int CLASS_TWO_COUNT = 2;
    public static final int CLASS_THREE_COUNT = 4;
    public static final int CLASS_FOUR_COUNT = 1;

    // link speed 10Mb/s
    public static final long LINK_SPEED_IN_SECOND = 10_000_000L;
    public static final int GCD_CLASSES_PACKET_SIZE = gcd(gcd(gcd(CLASS_ONE_SIZE, CLASS_TWO_SIZE), CLASS_THREE_SIZE), CLASS_FOUR_SIZE);
    public static final long CLOCK_IN_SECOND = LINK_SPEED_IN_SECOND / GCD_CLASSES_PACKET_SIZE;
    public static final long TOTAL_CLOCK_COUNT = 24L * 60L * 60L * CLOCK_IN_SECOND;
    public static final long SMALL_TOTAL_CLOCK_COUNT = 60L * 60L * CLOCK_IN_SECOND;
    public static final int LINK_SPEED_PER_CLOCK = GCD_CLASSES_PACKET_SIZE;
    public static final long ONE_HOUR_CLOCK_COUNT = 60L * 60L * CLOCK_IN_SECOND;
    public static final long HALF_HOUR_CLOCK_COUNT = 30L * 60L * CLOCK_IN_SECOND;
    public static final long TEN_MINUTE_CLOCK_COUNT = 10L * 60L * CLOCK_IN_SECOND;
    public static final long FIVE_MINUTE_CLOCK_COUNT = 5L * 60L * CLOCK_IN_SECOND;

    // classes packets generation speed in clock
    public static final int CLASS_ONE_CYCLE = (int) CLOCK_IN_SECOND / CLASS_ONE_COUNT;
    public static final int CLASS_TWO_CYCLE = (int) CLOCK_IN_SECOND / CLASS_TWO_COUNT;
    public static final int CLASS_THREE_CYCLE = (int) CLOCK_IN_SECOND / CLASS_THREE_COUNT;
    public static final int CLASS_FOUR_CYCLE = (int) CLOCK_IN_SECOND / CLASS_FOUR_COUNT;

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
