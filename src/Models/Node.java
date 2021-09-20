package Models;

import constants.NodeType;

public class Node {

    public final int id;
    private static int ID = 0;
    private NodeType type;
    private Switch connection;

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

    public void simulate() {

    }


}
