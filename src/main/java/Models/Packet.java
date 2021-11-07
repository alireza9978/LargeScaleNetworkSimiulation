package Models;

import constants.Constants;

public class Packet {

    public int flowNumber = -1;
    public final Node sender;
    public final int size;
    public final long creationTime;

    public Packet(int flowNumber, Node sender, int size, long creationTime) {
        this.flowNumber = flowNumber;
        this.sender = sender;
        this.size = size;
        this.creationTime = creationTime;
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

    public long getCreationTime() {
        return creationTime;
    }

    public float getEndToEndDelay(long now) {
        return (float) (now - creationTime) / Constants.CLOCK_IN_SECOND;
    }

}