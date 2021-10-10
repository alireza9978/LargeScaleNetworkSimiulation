package Models;

import constants.NodeType;

public class Node implements Runnable {

    public final int id;
    private boolean on = false;
    private static int ID = 0;
    private final NodeType type;
    private Switch connection;
    private int flowNumber = -1;
    private long clock = 0;

    public Node() {
        this.id = ID;
        ID++;
        type = NodeType.getInstanceRandom();
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
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
            if (clock % type.getPeriod() == 0) {
                connection.receive(new Packet(flowNumber, this, type.getSize()));
            }
        }
        clock++;
    }
}
