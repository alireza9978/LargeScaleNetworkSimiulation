package Models;

public class Packet {

    public int flowNumber = -1;
    public final Node sender;
    public final int size;

    public Packet(int flowNumber, Node sender, int size) {
        this.flowNumber = flowNumber;
        this.sender = sender;
        this.size = size;
    }

    public Packet(Node sender, int size) {
        this.sender = sender;
        this.size = size;
    }

    public int getFlowNumber() {
        return flowNumber;
    }

    public void setFlowNumber(int flowNumber) {
        this.flowNumber = flowNumber;
    }

    public Node getSender() {
        return sender;
    }

    public int getSize() {
        return size;
    }
}