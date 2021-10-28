package Models;

import constants.Constants;

import java.util.Arrays;

public class Server implements Receiver {

    private final int id;
    private static int ID = 0;
    private final VirtualMachine[] virtualMachines;
    private int virtualMachinesCount = 0;
    private long totalPacket = 0;
    private long totalPacketUntilLastSecond = 0;

    public Server() {
        this.id = ID;
        ID++;
        virtualMachines = new VirtualMachine[Constants.SERVER_MAX_VM_COUNT];
    }

    public boolean isFull() {
        return virtualMachinesCount >= Constants.SERVER_MAX_VM_COUNT;
    }

    public void addVM(VirtualMachine virtualMachine) {
        if (virtualMachines[virtualMachine.listeningPort] == null) {
            virtualMachines[virtualMachine.listeningPort] = virtualMachine;
            virtualMachinesCount++;
        }
    }

    public void removeVM(VirtualMachine virtualMachine) {
        if (virtualMachines[virtualMachine.listeningPort] != null) {
            virtualMachines[virtualMachine.listeningPort] = null;
            virtualMachinesCount--;
        }
    }

    @Override
    public void receive(Packet packet) {
        virtualMachines[packet.getSender().getType().toInt()].receive(packet);
        totalPacket++;
    }

    public void run(long clock) {
        for (VirtualMachine virtualMachine : virtualMachines) {
            if (virtualMachine != null) {
                virtualMachine.simulate(clock);
            }
        }
    }

    @Override
    public String toString() {
        return "Server{" +
                "id=" + id +
                ", virtualMachines=" + Arrays.toString(virtualMachines) +
                '}';
    }

    public long getTotalPacket() {
        return totalPacket;
    }

    public Float getUtilization() {
        float temp = (float) (totalPacket - totalPacketUntilLastSecond) / (float) Constants.SERVER_MAX_PROCESSING_PACKET_IN_SECOND;
        totalPacketUntilLastSecond = totalPacket;
        return temp;
    }

    public VirtualMachine[] getVirtualMachines() {
        return virtualMachines;
    }

    public int getVirtualMachinesCount() {
        return virtualMachinesCount;
    }

    public int getId() {
        return id;
    }
}
