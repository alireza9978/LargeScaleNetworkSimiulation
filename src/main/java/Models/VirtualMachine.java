package Models;

import Models.StatsModels.EndToEndDelay;
import constants.Constants;
import constants.NodeType;

public class VirtualMachine implements Receiver {

    private final NodeType type;
    private int totalProcessedPackets = 0;
    private int cycleProcessedPackets = 0;
    public final int listeningPort;
    public final int id;
    public long clock = 1;
    private static int ID = 0;
    private EndToEndDelay endToEndDelay = new EndToEndDelay();

    public VirtualMachine(NodeType type, int listeningPort) {
        this.type = type;
        this.listeningPort = listeningPort;
        this.id = ID;
        ID++;
    }

    @Override
    public void receive(Packet packet) {
        if (cycleProcessedPackets >= Constants.MAX_VM_PACKET_COUNT_PROCESS_SPEED) {
            System.out.println("VM id=" + id + " has crashed");
            return;
        }
        if (packet.getSender().getType().equals(type)) {
            cycleProcessedPackets += 1;
            endToEndDelay.addPacket(packet.getEndToEndDelay(clock));
        } else {
            System.out.println("VM id=" + id + " has a wrong input packet");
        }
    }

    public void simulate(long clock) {
        totalProcessedPackets += cycleProcessedPackets;
        this.clock = clock;
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

    public Float getUtilization() {
        float temp = (float) (cycleProcessedPackets) / (float) Constants.SERVER_MAX_PROCESSING_PACKET_IN_SECOND;
        cycleProcessedPackets = 0;
        return temp;
    }

    public EndToEndDelay getEndToEndDelay() {
        EndToEndDelay temp = this.endToEndDelay;
        this.endToEndDelay = new EndToEndDelay();
        return temp;
    }

}
