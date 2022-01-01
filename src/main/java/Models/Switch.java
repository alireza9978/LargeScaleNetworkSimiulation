package Models;

import constants.Constants;
import constants.NodeType;
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
    private int[] sumPacketCountInQueues;
    private int clockForAverage = 0;
    private final ArrayList<Pair<Integer, Integer>> switchesConnections = new ArrayList<>();
    private final ArrayList<Pair<Integer, Integer>> serversConnections = new ArrayList<>();
    private Integer[] droppedPacket;

    public Switch() {
        this.id = ID;
        ID++;
        buffers = new Buffer[Constants.SWITCH_MAX_CONNECTION_COUNT];
        sumPacketCountInQueues  = new int[buffers.length];
        droppedPacket = new Integer[NodeType.getCount()];
        for (int i = 0; i < NodeType.getCount(); i++) {
            droppedPacket[i] = 0;
        }
        for (int i = 0; i < buffers.length; i++) {
            sumPacketCountInQueues[i] = 0;
        }
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
        buffers[port] = new Buffer(receiver, getId(), port);
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

    synchronized public void routeReceivedPackets() {
        inputPacketsCount += inputPackets.size();
        for (Packet packet : inputPackets) {
            if (packet != null) {
                Integer targetBuffer = routingSetting.get(packet.flowNumber);
                if (targetBuffer != null) {
                    if (!buffers[targetBuffer].addPacket(packet)) {
                        int temp = packet.getSender().getType().toInt();
                        if (temp < NodeType.getCount() && temp > -1)
                            droppedPacket[temp]++;
                    }
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
        for (int i = 0; i < buffers.length; i++) {
            Buffer buffer = buffers[i];
            if (buffer != null) {
                buffer.simulate();
                sumPacketCountInQueues[i] += buffer.packetInQueue();
                sumPacketCountInQueue += buffer.packetInQueue();
            }
        }
        clockForAverage++;
    }

    public int getInputPacketsCount() {
        return inputPacketsCount;
    }

    public Float getQueueSize() {
        if (clockForAverage == 0){
            return 0.1f;
        }
        return (float) sumPacketCountInQueue / (float) clockForAverage;
    }

    public Float getQueueSize(int port) {
        if (clockForAverage == 0){
            return 0.1f;
        }
        return (float) sumPacketCountInQueues[port] / (float) clockForAverage;
    }

    public Integer[] getDroppedPacketsCount() {
        return droppedPacket;
    }

    public void resetDataCycle(){
        inputPacketsCount = 0;
        sumPacketCountInQueue = 0;
        sumPacketCountInQueues = new int[buffers.length];
        clockForAverage = 0;
        droppedPacket = new Integer[NodeType.getCount()];
        for (int i = 0; i < NodeType.getCount(); i++) {
            droppedPacket[i] = 0;
        }
        for (int i = 0; i < buffers.length; i++) {
            sumPacketCountInQueues[i] = 0;
        }
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
