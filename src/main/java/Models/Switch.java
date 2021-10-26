package Models;

import constants.Constants;
import constants.Pair;

import java.util.ArrayList;
import java.util.Hashtable;

public class Switch implements Receiver, Runnable {

    private final int id;
    private static int ID = 0;
    private final Buffer[] buffers;
    private Hashtable<Integer, Integer> routingSetting = null;
    private ArrayList<Packet> inputPackets = new ArrayList<>();
    private int inputPacketsCount = 0;
    private int sumPacketCountInQueue = 0;
    private int clockForAverage = 0;
    private ArrayList<Pair<Integer, Integer>> switchesConnections = new ArrayList<>();
    private ArrayList<Pair<Integer, Integer>> serversConnections = new ArrayList<>();

    public Switch() {
        this.id = ID;
        ID++;
        buffers = new Buffer[Constants.SWITCH_MAX_CONNECTION_COUNT];
    }

    public boolean isConnected() {
        for (Buffer buffer : buffers) {
            if (buffer != null) {
                return true;
            }
        }
        return false;
    }

    public boolean checkSetting() {
        if (isConnected()) {
            return routingSetting != null;
        } else {
            return true;
        }
    }

    public void connect(Receiver receiver, int port) {
        buffers[port] = new Buffer(receiver);
        if (receiver instanceof Switch) {
            switchesConnections.add(new Pair<>(((Switch) receiver).getId(), port));
        }
        if (receiver instanceof Server) {
            serversConnections.add(new Pair<>(((Server) receiver).getId(), port));
        }
    }

    public void updateRoutingSetting(Hashtable<Integer, Integer> routingSetting) {
        this.routingSetting = routingSetting;
    }

    public void addRoutingSetting(Integer key, Integer value) {
        routingSetting.put(key, value);
    }

    public void routeReceivedPackets() {
        inputPacketsCount += inputPackets.size();
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
            if (buffer != null) {
                buffer.simulate();
                sumPacketCountInQueue += buffer.packetInQueue();
            }
        }
        clockForAverage++;
    }

    public int getInputPacketsCount() {
        int temp = inputPacketsCount;
        inputPacketsCount = 0;
        return temp;
    }

    public Float getQueuePacketsCount() {
        float temp = (float) sumPacketCountInQueue / (float) clockForAverage;
        sumPacketCountInQueue = 0;
        clockForAverage = 0;
        return temp;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Pair<Integer, Integer>> getSwitchesConnections() {
        return switchesConnections;
    }

    public ArrayList<Pair<Integer, Integer>> getServersConnections() {
        return serversConnections;
    }
}
