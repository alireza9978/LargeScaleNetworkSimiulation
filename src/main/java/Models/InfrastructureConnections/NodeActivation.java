package Models.InfrastructureConnections;

public class NodeActivation {

    private final int toActivate;
    private final long time;

    public NodeActivation(int toActivate, long time) {
        this.toActivate = toActivate;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public int getToActivate() {
        return toActivate;
    }
}
