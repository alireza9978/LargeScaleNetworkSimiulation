package Models;

import Models.StatsModels.EndToEndDelay;
import constants.Constants;
import constants.NodeType;

public class VirtualMachine implements Receiver {

    private final NodeType type;
    private long cycleProcessedPackets = 0;
    private int cycleDroppedPackets = 0;
    public final int listeningPort;
    public final int id;
    public long clock = 1;
    public long passedClock = 0;
    public boolean turnOn = false;
    private static int ID = 0;
    private EndToEndDelay endToEndDelay = new EndToEndDelay();

    public VirtualMachine(NodeType type, int listeningPort) {
        this.type = type;
        this.listeningPort = listeningPort;
        this.id = ID;
        ID++;
    }

    public void turnOn(int clock) {
        this.clock = clock;
        this.turnOn = true;
    }

    public void turnOff() {
        this.turnOn = false;
    }

    @Override
    public void receive(Packet packet) {
        if (packet.getSender().getType().equals(type)) {
            if (cycleProcessedPackets < Constants.MAX_VM_INPUT_PACKET_SIZE_IN_SECOND - packet.getSize()){
                cycleProcessedPackets += packet.getSize();
                endToEndDelay.addPacket(packet.getEndToEndDelay(clock), packet.getSender().getType().toInt());
            }else{
                cycleDroppedPackets++;
            }
        } else {
            System.out.println("VM id=" + id + " has a wrong input packet");
        }
    }

    public void simulate() {
        if (turnOn) {
            passedClock++;
            clock++;
        }
    }

    public NodeType getType() {
        return type;
    }

    public long getCycleProcessedPackets() {
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
        return (float) (cycleProcessedPackets) / (float) (Constants.MAX_VM_INPUT_PACKET_SIZE_IN_CLOCK * passedClock);
    }

    public EndToEndDelay getEndToEndDelay() {
        return this.endToEndDelay;
    }

    public void resetDataCycle() {
        passedClock = 0;
        cycleProcessedPackets = 0;
        cycleDroppedPackets = 0;
        this.endToEndDelay = new EndToEndDelay();
    }

    public long getCyclePacketsCount() {
        return (cycleProcessedPackets / type.getSize()) + cycleDroppedPackets;
    }
}
