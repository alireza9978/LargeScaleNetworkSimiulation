package Models.InfrastructureConnections;

import constants.NodeType;

public class NodeConnection {

    private int switchId;
    private int nodeCount;

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
