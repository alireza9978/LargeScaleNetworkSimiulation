package Models.InfrastructureConnections;

public class SwitchConnection {

    private final int start;
    private final int end;
    private final int startPort;
    private final int endPort;
    private final long linkSpeed;

    public SwitchConnection(int start, int end, int startPort, int endPort, long linkSpeed) {
        this.start = start;
        this.end = end;
        this.startPort = startPort;
        this.endPort = endPort;
        this.linkSpeed = linkSpeed;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getStartPort() {
        return startPort;
    }

    public int getEndPort() {
        return endPort;
    }

    public long getLinkSpeed() {
        return linkSpeed;
    }
}
