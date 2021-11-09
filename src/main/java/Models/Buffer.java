package Models;

import constants.Constants;

import java.util.ArrayDeque;

public class Buffer {

    private final ArrayDeque<Packet> queue = new ArrayDeque<>();
    private final Receiver receiver;
    private final int switchId;
    private final int port;

    private Packet sendingPacket;
    private int sendingPacketCyclePassed;
    private int sendingPacketTotalCycle;

    public Buffer(Receiver receiver, int switchId, int port) {
        this.receiver = receiver;
        this.switchId = switchId;
        this.port = port;
    }

    public boolean addPacket(Packet packet) {
        if (queue.size() < Constants.MAX_BUFFER_PACKET_COUNT) {
            queue.add(packet);
            return true;
        } else {
//            System.out.println("Buffer overflow in switch " + switchId + " port " + port);
            return false;
        }
    }

    public void simulate() {
        if (sendingPacket == null) {
            if (!queue.isEmpty()) {
                sendingPacket = queue.pop();
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

    public int packetInQueue() {
        return queue.size();
    }

    public int getSwitchId() {
        return switchId;
    }

    public int getPort() {
        return port;
    }
}
