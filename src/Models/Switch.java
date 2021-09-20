package Models;

import constants.Constants;

import java.util.ArrayList;
import java.util.Hashtable;

public class Switch implements Receiver {

    public final int id;
    private static int ID = 0;
    private final ArrayList<Node> nodes;
    private final Buffer[] buffers;
    private int connectionsCount = 0;
    private Hashtable<Integer, Integer> routingSetting;

    public Switch() {
        this.id = ID;
        ID++;
        nodes = new ArrayList<>();
        buffers = new Buffer[Constants.SWITCH_MAX_CONNECTION_COUNT];
    }

    public void addNode(Node node) {
        if (node != null)
            nodes.add(node);
    }

    public void connect(Receiver receiver, int port) {
        buffers[port] = new Buffer(receiver);
        connectionsCount++;
    }

    public void updateRoutingSetting(Hashtable<Integer, Integer> routingSetting) {
        this.routingSetting = routingSetting;
    }

    @Override
    public void receive(Packet packet) {
        Integer targetBuffer = routingSetting.get(packet.flowNumber);
        if (targetBuffer != null){
            buffers[targetBuffer].addPacket(packet);
        }
    }


}
