package Models.InfrastructureConnections;

public class NodeActivation {

    private final int toActivate;
    private final int time;

    public NodeActivation(int toActivate, int time) {
        this.toActivate = toActivate;
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public int getToActivate() {
        return toActivate;
    }
}
