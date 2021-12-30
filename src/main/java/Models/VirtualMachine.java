package Models;

import Models.StatsModels.EndToEndDelay;
import constants.Constants;
import constants.NodeType;

public class VirtualMachine implements Receiver {

    private final NodeType type;
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
            endToEndDelay.addPacket(packet.getEndToEndDelay(clock), packet.getSender().getType().toInt());
        } else {
            System.out.println("VM id=" + id + " has a wrong input packet");
        }
    }

    public void simulate(long clock) {
        this.clock = clock;
    }

    public float getUsage() {
        return ((float) cycleProcessedPackets) / Constants.MAX_VM_PACKET_COUNT_PROCESS_SPEED;
    }

    public NodeType getType() {
        return type;
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
                ", id=" + id +
                '}';
    }

    public Float getUtilization() {
        return (float) (cycleProcessedPackets) / (float) Constants.MAX_VM_PACKET_COUNT_PROCESS_SPEED;
    }

    public EndToEndDelay getEndToEndDelay() {
        return this.endToEndDelay;
    }

    public void resetDataCycle() {
        cycleProcessedPackets = 0;
        this.endToEndDelay = new EndToEndDelay();
    }

}
