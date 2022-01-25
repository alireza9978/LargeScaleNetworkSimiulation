package constants;

public class Constants {

    public static final int MAX_SERVER_COUNT = 3;
    public static final int MAX_VM_IN_SINGLE_SERVER_COUNT = 4;
    public static final int MAX_SWITCH_COUNT = 1000;

    public static final int MAX_NODE_COUNT = 120_000;
    public static final int MINIMUM_ACTIVE_NODE_COUNT = 12000;
    public static final int NODE_ACTIVATION_STEPS_COUNT = 360;

    public static final int SWITCH_MAX_CONNECTION_COUNT = 32;
    public static final int SERVER_MAX_VM_COUNT = 10;

    // classes packets size in b
    public static final int CLASS_ONE_SIZE = 2000;
    public static final int CLASS_TWO_SIZE = 4000;
    public static final int CLASS_THREE_SIZE = 4000;
    public static final int CLASS_FOUR_SIZE = 6000;

    // classes packets count in second
    public static final int CLASS_ONE_COUNT = 4;
    public static final int CLASS_TWO_COUNT = 3;
    public static final int CLASS_THREE_COUNT = 2;
    public static final int CLASS_FOUR_COUNT = 1;

    // link speeds
    public static final long SWITCH_LINK_SPEED_SLOW = 30_000_000L;
    public static final String SWITCH_LINK_SPEED_SLOW_NAME = "30Mb";
    public static final long SWITCH_LINK_SPEED_FAST = 30_000_000L;
    public static final String SWITCH_LINK_SPEED_FAST_NAME = "30Mb";
    public static final long SERVER_LINK_SPEED = 30_000_000L;
    public static final String SERVER_LINK_SPEED_NAME = "30Mb";

    public static final long MAX_LINK_SPEED = Math.max(SERVER_LINK_SPEED, Math.max(SWITCH_LINK_SPEED_SLOW, SWITCH_LINK_SPEED_FAST));
    public static final int GCD_CLASSES_PACKET_SIZE = gcd(gcd(gcd(CLASS_ONE_SIZE, CLASS_TWO_SIZE), CLASS_THREE_SIZE), CLASS_FOUR_SIZE);

    public static long CLOCK_IN_SECOND = MAX_LINK_SPEED / GCD_CLASSES_PACKET_SIZE;

    public static final long SIMULATION_SCALE = 12L * 60L;
    public static final long NETWORK_STATE_SAVED_DATA_COUNT = 24L * 60L * 60L;
    public static final long UPDATE_NETWORK_PATH_DATA_COUNT = 24L * 6L;
    public static final long TIME_RATIO = NETWORK_STATE_SAVED_DATA_COUNT / SIMULATION_SCALE;

    public static final long TOTAL_CLOCK_COUNT = SIMULATION_SCALE * CLOCK_IN_SECOND;
    public static final long CHECK_NODE_ACTIVATION_CLOCK = TOTAL_CLOCK_COUNT / NODE_ACTIVATION_STEPS_COUNT;
    public static final long SAVE_NETWORK_STATE_CLOCK_COUNT = TOTAL_CLOCK_COUNT / NETWORK_STATE_SAVED_DATA_COUNT;
    public static final long UPDATE_NETWORK_PATH_CLOCK_COUNT = TOTAL_CLOCK_COUNT / UPDATE_NETWORK_PATH_DATA_COUNT;
    public static final int LINK_SPEED_PER_CLOCK = GCD_CLASSES_PACKET_SIZE;

    // classes packets generation speed in clock
    public static final int CLASS_ONE_CYCLE = (int) (CLOCK_IN_SECOND / CLASS_ONE_COUNT);
    public static final int CLASS_TWO_CYCLE = (int) (CLOCK_IN_SECOND / CLASS_TWO_COUNT);
    public static final int CLASS_THREE_CYCLE = (int) (CLOCK_IN_SECOND / CLASS_THREE_COUNT);
    public static final int CLASS_FOUR_CYCLE = (int) (CLOCK_IN_SECOND / CLASS_FOUR_COUNT);

    // probability of belonging to a class
    public static final float CLASS_ONE_PROBABILITY = 0.25f;
    public static final float CLASS_TWO_PROBABILITY = 0.17f;
    public static final float CLASS_THREE_PROBABILITY = 0.25f;
    public static final float CLASS_FOUR_PROBABILITY = 0.33f;

    public static final String TEST_DIR = "src/main/resources/tests/";
    public static final String FIGURE_DIR = "src/main/resources/charts/";
    public static final String GRAPH_DIR = "src/main/resources/graphs/";
    //    public static final String ROOT_DIR = "/home/ippbx/IdeaProjects/LargeScaleNetworkSimiulation/";
    //    public static final String ROOT_DIR = "/home/alireza/projects/java/largeScaleNetworkSimulation/";
    //    public static final String ROOT_DIR = "C:\\Users\\Alireza\\IdeaProjects\\LargeScaleNetworkSimulation\\";

    public static final int MAX_BUFFER_PACKET_COUNT = 100;

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
