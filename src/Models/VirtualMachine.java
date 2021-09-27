package Models;

import constants.Constants;
import constants.NodeType;

public class VirtualMachine implements Receiver {

    private final NodeType type;
    private int totalProcessedPackets = 0;
    private int cycleProcessedPackets = 0;
    public final int listeningPort;
    public final int id;
    private static int ID = 0;

    public VirtualMachine(NodeType type, int listeningPort) {
        this.type = type;
        this.listeningPort = listeningPort;
        this.id = ID;
        ID++;
    }

    @Override
    public void receive(Packet packet) {
        if (packet.getSender().getType().equals(type)) {
            cycleProcessedPackets += 1;
        }
        if (cycleProcessedPackets >= Constants.MAX_VM_PACKET_COUNT_PROCESS_SPEED) {
            System.out.println("VM CRASH");
        }
    }

    public void simulate() {
        totalProcessedPackets += cycleProcessedPackets;
        cycleProcessedPackets = 0;
    }

    public float getUsage() {
        return ((float) cycleProcessedPackets) / Constants.MAX_VM_PACKET_COUNT_PROCESS_SPEED;
    }

    public NodeType getType() {
        return type;
    }

    public int getTotalProcessedPackets() {
        return totalProcessedPackets;
    }

    public int getCycleProcessedPackets() {
        return cycleProcessedPackets;
    }

    public int getListeningPort() {
        return listeningPort;
    }

    @Override
    public String toString() {
        return "VirtualMachine{" +
                "type=" + type +
                ", totalProcessedPackets=" + totalProcessedPackets +
                ", id=" + id +
                '}';
    }
}
