package Models;

import constants.NodeType;

public class Node {

    public final int id;
    private static int ID = 0;
    private final NodeType type;
    private Switch connection;
    private int flowNumber = -1;

    public Node() {
        this.id = ID;
        ID++;
        type = NodeType.getInstanceRandom();
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

    public void simulate(long clock) {
        if (clock % type.getPeriod() == 0){
            connection.receive(new Packet(this, type.getSize()));
        }
    }

    public int getFlowNumber() {
        return flowNumber;
    }

    public void setFlowNumber(int flowNumber) {
        this.flowNumber = flowNumber;
    }
}
