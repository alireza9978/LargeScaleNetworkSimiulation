package Models;

public class Network {

    private Server[] servers;
    private Switch[] switches;
    private Node[] nodes;

    public Network(int serverCount, int switchCount, int nodeCount) {
        initialize(serverCount, switchCount, nodeCount);
    }

    private void initialize(int serverCount, int switchCount, int nodeCount) {
        servers = new Server[serverCount];
        switches = new Switch[switchCount];
        nodes = new Node[nodeCount];
    }

    public void simulate() {

    }
}
