package Models;

import Models.InfrastructureConnections.NodeConnection;
import Models.InfrastructureConnections.ServerConnection;
import Models.InfrastructureConnections.SwitchConnection;
import constants.NetworkStructureUtil;

import java.util.ArrayList;

public class Network {

    private Server[] servers;
    private Switch[] switches;
    private Node[] nodes;

    public Network(int serverCount, int switchCount, int nodeCount) {
        initialize(serverCount, switchCount, nodeCount);
        connectSwitches(NetworkStructureUtil.getSwitchesStructure());
        connectNodes(NetworkStructureUtil.getNodeStructure());
        connectServers(NetworkStructureUtil.getServerStructure());
    }

    private void connectServers(ArrayList<ServerConnection> serverConnections) {
        for (ServerConnection connection : serverConnections) {
            switches[connection.getSwitchId()].connect(servers[connection.getId()], connection.getPort());
        }
    }

    private void connectNodes(ArrayList<NodeConnection> nodeConnections) {
        int index = 0;
        for (NodeConnection connection : nodeConnections) {
            for (int i = index; i < index + connection.getNodeCount(); i++) {
                nodes[i].setConnection(switches[connection.getSwitchId()]);
            }
        }
    }

    private void connectSwitches(ArrayList<SwitchConnection> switchConnections) {
        for (SwitchConnection connection : switchConnections) {
            switches[connection.getStart()].connect(switches[connection.getEnd()], connection.getStartPort());
        }
    }

    private void initialize(int serverCount, int switchCount, int nodeCount) {
        servers = new Server[serverCount];
        switches = new Switch[switchCount];
        nodes = new Node[nodeCount];

        for (int i = 0; i < serverCount; i++) {
            servers[i] = new Server();
        }

        for (int i = 0; i < switchCount; i++) {
            switches[i] = new Switch();
        }

        for (int i = 0; i < nodeCount; i++) {
            nodes[i] = new Node();
        }

    }

    public void simulate() {

    }
}
