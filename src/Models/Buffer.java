package Models;

import constants.Constants;

import java.util.PriorityQueue;

public class Buffer {

    private PriorityQueue<Packet> queue = new PriorityQueue<>();
    private Receiver receiver;

    public Buffer(Receiver receiver) {
        this.receiver = receiver;
    }

    public void addPacket(Packet packet) {
        if (queue.size() < Constants.MAX_BUFFER_PACKET_COUNT)
            queue.add(packet);
        else
            System.out.println("Buffer overflow");
    }

    public void simulate(){
        receiver.receive(queue.poll());
    }

}
