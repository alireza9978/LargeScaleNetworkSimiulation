package Models.InfrastructureConnections;

public class NodeConnection {

    private final int switchId;
    private final int nodeCount;

    public NodeConnection(int switchId, int nodeCount) {
        this.switchId = switchId;
        this.nodeCount = nodeCount;
    }

    public int getSwitchId() {
        return switchId;
    }

    public int getNodeCount() {
        return nodeCount;
    }
}
