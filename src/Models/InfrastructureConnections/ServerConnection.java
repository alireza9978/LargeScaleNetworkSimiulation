package Models.InfrastructureConnections;

public class ServerConnection {

    private final int id;
    private final int switchId;
    private final int port;

    public ServerConnection(int id, int switchId, int port) {
        this.id = id;
        this.switchId = switchId;
        this.port = port;
    }

    public int getId() {
        return id;
    }

    public int getSwitchId() {
        return switchId;
    }

    public int getPort() {
        return port;
    }
}
