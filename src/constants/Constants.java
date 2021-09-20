package constants;

public class Constants {

    public static final int SERVER_COUNT = 1;
    public static final int SWITCH_COUNT = 6;
    public static final int NODE_COUNT = 60;
    public static final int EDGE_SWITCH_COUNT = 3;
    public static final int EDGE_SWITCH_NODE_COUNT = NODE_COUNT / EDGE_SWITCH_COUNT;
    public static final int NODE_COUNT_TOLERANCE = 5;

    public static final int SWITCH_MAX_CONNECTION_COUNT = 32;
    public static final int SERVER_MAX_VM_COUNT = 10;

    // packets that each vm can process in a single cycle
    public static final int MAX_VM_PACKET_COUNT_PROCESS_SPEED = 10000;

    public static final int MAX_BUFFER_PACKET_COUNT = 10000;

    // 100Mb/s ==> 1Mb/cycle ==> 1000000
    public static final int TOTAL_CLOCK_COUNT = 24 * 60 * 60 * 100;
    public static final int LINK_SPEED_PER_CLOCK = 1000000;

    //classes packets size in Kb/cycle
    public static final int CLASS_ONE_SIZE = 1000;
    public static final int CLASS_TWO_SIZE = 2000;
    public static final int CLASS_THREE_SIZE = 3500;

    //classes packets generation speed in clock
    public static final int CLASS_ONE_CYCLE = 10;
    public static final int CLASS_TWO_CYCLE = 7;
    public static final int CLASS_THREE_CYCLE = 4;

}
