package Models;

import constants.Constants;

import java.util.ArrayList;
import java.util.Hashtable;

public class Switch implements Receiver {

    public final int id;
    private static int ID = 0;
    private final Buffer[] buffers;
    private int connectionsCount = 0;
    private Hashtable<Integer, Integer> routingSetting;
    private ArrayList<Packet> inputPackets = new ArrayList<>();

    public Switch() {
        this.id = ID;
        ID++;
        buffers = new Buffer[Constants.SWITCH_MAX_CONNECTION_COUNT];
    }

    public void connect(Receiver receiver, int port) {
        buffers[port] = new Buffer(receiver);
        connectionsCount++;
    }

    public void updateRoutingSetting(Hashtable<Integer, Integer> routingSetting) {
        this.routingSetting = routingSetting;
    }

    public void routeReceivedPackets() {
        for (Packet packet : inputPackets) {
            Integer targetBuffer = routingSetting.get(packet.flowNumber);
            if (targetBuffer != null) {
                buffers[targetBuffer].addPacket(packet);
            }
        }
        inputPackets = new ArrayList<>();
    }

    public void simulate() {
        for (Buffer buffer : buffers) {
            if (buffer != null)
                buffer.simulate();
        }
    }

    @Override
    public void receive(Packet packet) {
        inputPackets.add(packet);
    }


}
