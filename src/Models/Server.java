package Models;

import constants.Constants;

import java.util.LinkedList;

public class Server implements Receiver {

    public final int id;
    private static int ID = 0;
    private final LinkedList<VirtualMachine> virtualMachines;

    public Server() {
        this.id = ID;
        ID++;
        virtualMachines = new LinkedList<>();
    }

    public boolean isFull() {
        return virtualMachines.size() >= Constants.SERVER_MAX_VM_COUNT;
    }

    public void addVM(VirtualMachine virtualMachine) {
        virtualMachines.add(virtualMachine);
    }

    public void removeVM(VirtualMachine virtualMachine) {
        virtualMachines.remove(virtualMachine);
    }

    @Override
    public void receive(Packet packet) {
        System.out.println("new packet");
        System.exit(0);
    }
}
