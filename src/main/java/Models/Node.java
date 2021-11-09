package Models;

import constants.NodeType;

public class Node implements Runnable {

    public final int id;
    private boolean on = false;
    private static int ID = 0;
    private final NodeType type;
    private final int delay;
    private Switch connection;
    private int targetServer;
    private int flowNumber = -1;
    private long clock = 0;

    public Node() {
        this.id = ID;
        ID++;
        type = NodeType.getInstanceRandom();
        delay = NodeType.getStartDelay();
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on, long clock) {
        this.on = on;
        this.clock = clock;
    }

    public Switch getConnection() {
        return connection;
    }

    public void setConnection(Switch connection) {
        this.connection = connection;
    }

    public NodeType getType() {
        return type;
    }

    public int getFlowNumber() {
        return flowNumber;
    }

    public void setFlowNumber(int flowNumber) {
        this.flowNumber = flowNumber;
    }

    @Override
    public void run() {
        if (isOn()) {
            if (clock % type.getPeriod() == delay) {
                connection.receive(new Packet(flowNumber, this, type.getSize(), clock));
            }
        }
        clock++;
    }

    public int getTargetServer() {
        return targetServer;
    }

    public void setTargetServer(int targetServer) {
        this.targetServer = targetServer;
    }
}
