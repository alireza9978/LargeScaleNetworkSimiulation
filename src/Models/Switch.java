package Models;

import constants.Constants;

import java.util.ArrayList;
import java.util.Hashtable;

public class Switch implements Receiver, Runnable {

    public final int id;
    private static int ID = 0;
    private final Buffer[] buffers;
    private Hashtable<Integer, Integer> routingSetting = null;
    private ArrayList<Packet> inputPackets = new ArrayList<>();

    public Switch() {
        this.id = ID;
        ID++;
        buffers = new Buffer[Constants.SWITCH_MAX_CONNECTION_COUNT];
    }

    public boolean checkSetting() {
        return routingSetting != null;
    }

    public void connect(Receiver receiver, int port) {
        buffers[port] = new Buffer(receiver);
    }

    public void updateRoutingSetting(Hashtable<Integer, Integer> routingSetting) {
        this.routingSetting = routingSetting;
    }


    public void routeReceivedPackets() {
        for (Packet packet : inputPackets) {
            if (packet != null) {
                Integer targetBuffer = routingSetting.get(packet.flowNumber);
                if (targetBuffer != null) {
                    buffers[targetBuffer].addPacket(packet);
                }
            } else {
                System.out.println("null packet received");
            }
        }
        inputPackets = new ArrayList<>();
    }


    @Override
    synchronized public void receive(Packet packet) {
        if (packet == null) {
            System.out.println("null income");
        } else
            inputPackets.add(packet);
    }


    @Override
    public void run() {
        for (Buffer buffer : buffers) {
            if (buffer != null)
                buffer.simulate();
        }
    }
}
