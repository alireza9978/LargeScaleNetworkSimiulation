package Models.InfrastructureConnections;

public class ServerConnection {

    private final int id;
    private final int switchId;
    private final int port;
    private final long linkSpeed;

    public ServerConnection(int id, int switchId, int port, long linkSpeed) {
        this.id = id;
        this.switchId = switchId;
        this.port = port;
        this.linkSpeed = linkSpeed;
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

    public long getLinkSpeed() {
        return linkSpeed;
    }
}
