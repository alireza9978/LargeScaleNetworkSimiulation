package Models.InfrastructureConnections;

public class ServerConnection {

    private int id;
    private int switchId;
    private int port;

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
