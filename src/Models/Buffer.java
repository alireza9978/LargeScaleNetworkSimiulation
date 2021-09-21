package Models;

import constants.Constants;

import java.util.PriorityQueue;

public class Buffer {

    private final PriorityQueue<Packet> queue = new PriorityQueue<>();
    private final Receiver receiver;

    private Packet sendingPacket;
    private int sendingPacketCyclePassed;
    private int sendingPacketTotalCycle;

    public Buffer(Receiver receiver) {
        this.receiver = receiver;
    }

    public void addPacket(Packet packet) {
        if (queue.size() < Constants.MAX_BUFFER_PACKET_COUNT)
            queue.add(packet);
        else
            System.out.println("Buffer overflow");
    }

    public void simulate() {
        if (sendingPacket == null) {
            if (!queue.isEmpty()) {
                sendingPacket = queue.poll();
                sendingPacketCyclePassed = 0;
                sendingPacketTotalCycle = sendingPacket.size / Constants.LINK_SPEED_PER_CLOCK;
            }
        } else {
            sendingPacketCyclePassed++;
            if (sendingPacketCyclePassed == sendingPacketTotalCycle) {
                receiver.receive(sendingPacket);
                sendingPacket = null;
                sendingPacketTotalCycle = 0;
                sendingPacketCyclePassed = 0;
            }
        }
    }

}
