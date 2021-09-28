package Models.InfrastructureConnections;

public class SwitchConnection {

    private final int start;
    private final int end;
    private final int startPort;
    private final int endPort;

    public SwitchConnection(int start, int end, int startPort, int endPort) {
        this.start = start;
        this.end = end;
        this.startPort = startPort;
        this.endPort = endPort;
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
}
