package Models;

import constants.Constants;

import java.util.ArrayList;
import java.util.Arrays;

public class Server implements Receiver, Runnable {

    private final int id;
    private static int ID = 0;
    private final VirtualMachine[] virtualMachines;
    private int virtualMachinesCount = 0;
    private long passedClocks = 0;
    private ArrayList<Packet> inputPackets = new ArrayList<>();

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
    synchronized public void receive(Packet packet) {
        if (packet == null) {
            System.out.println("null income");
        } else
            inputPackets.add(packet);
    }

    @Override
    public void run() {
        for (Packet packet : inputPackets) {
            if (packet != null) {
                int packetType = packet.getSender().getType().toInt();
                VirtualMachine targetVM = virtualMachines[packetType];
                targetVM.receive(packet);
            } else {
                System.out.println("null packet received");
            }
        }
        inputPackets = new ArrayList<>();

        for (VirtualMachine virtualMachine : virtualMachines) {
            if (virtualMachine != null) {
                virtualMachine.simulate();
            }
        }
        passedClocks++;
    }

    @Override
    public String toString() {
        return "Server{" +
                "id=" + id +
                ", virtualMachines=" + Arrays.toString(virtualMachines) +
                '}';
    }


    public Float getUtilization() {
        long cycleProcessedPackets = 0;
        for (VirtualMachine virtualMachine : virtualMachines) {
            if (virtualMachine != null) {
                cycleProcessedPackets += virtualMachine.getCycleProcessedPackets();
            }
        }
        return (float) (cycleProcessedPackets) / (float) (Constants.MAX_SERVER_INPUT_PACKET_SIZE_IN_CLOCK * passedClocks);
    }

    public void resetDataCycle(){
        passedClocks = 0;
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

    public Long getCyclePackets() {
        long cyclePackets = 0;
        for (VirtualMachine virtualMachine : virtualMachines) {
            if (virtualMachine != null) {
                cyclePackets += virtualMachine.getCyclePacketsCount();
            }
        }
        return cyclePackets;
    }
}
