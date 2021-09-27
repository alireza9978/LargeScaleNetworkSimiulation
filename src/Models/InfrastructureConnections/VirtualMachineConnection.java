package Models.InfrastructureConnections;

public class VirtualMachineConnection {

    private final int virtualMachineId;
    private final int serverId;
    private final int serverPort;
    private final String virtualMachineType;

    public VirtualMachineConnection(int virtualMachineId, int serverId, int serverPort, String virtualMachineType) {
        this.virtualMachineId = virtualMachineId;
        this.serverId = serverId;
        this.serverPort = serverPort;
        this.virtualMachineType = virtualMachineType;
    }

    public int getVirtualMachineId() {
        return virtualMachineId;
    }

    public int getServerId() {
        return serverId;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getVirtualMachineType() {
        return virtualMachineType;
    }
}
