package Models;

import constants.Constants;
import constants.NodeType;

public class VirtualMachine implements Receiver {

    public final NodeType type;
    public int totalProcessedPackets = 0;
    public int cycleProcessedPackets = 0;

    public VirtualMachine(NodeType type) {
        this.type = type;
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
}
