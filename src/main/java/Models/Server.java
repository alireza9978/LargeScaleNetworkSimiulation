package Models;

import constants.Constants;

import java.util.Arrays;

public class Server implements Receiver {

    private final int id;
    private static int ID = 0;
    private final VirtualMachine[] virtualMachines;
    private int virtualMachinesCount = 0;
    private long cycleProcessedPackets = 0;

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
        cycleProcessedPackets += 1;
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

    public Long getCyclePackets() {
        return cycleProcessedPackets;
    }

    public Float getUtilization() {
        return (float) (cycleProcessedPackets) / (float) Constants.SERVER_MAX_PROCESSING_PACKET_IN_SECOND;
    }

    public void resetDataCycle(){
        cycleProcessedPackets = 0;
        for (VirtualMachine virtualMachine : virtualMachines) {
            if (virtualMachine != null) {
                virtualMachine.resetDataCycle();
            }
        }
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
