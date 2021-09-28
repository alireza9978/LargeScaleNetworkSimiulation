package Models.FlowNumberSetting;

public class NodeFlowNumber {

    private int flowNumber;
    private int nodeId;

    public NodeFlowNumber(int flowNumber, int nodeId) {
        this.flowNumber = flowNumber;
        this.nodeId = nodeId;
    }

    public int getFlowNumber() {
        return flowNumber;
    }

    public void setFlowNumber(int flowNumber) {
        this.flowNumber = flowNumber;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }
}
