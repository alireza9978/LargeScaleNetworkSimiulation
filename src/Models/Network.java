package Models;

import Models.InfrastructureConnections.NodeConnection;
import Models.InfrastructureConnections.ServerConnection;
import Models.InfrastructureConnections.SwitchConnection;
import constants.Constants;
import constants.NetworkStructureUtil;

import java.util.ArrayList;

import static constants.Constants.ONE_HOUR_CLOCK_COUNT;

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
            index = index + connection.getNodeCount();
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
        Controller.setPath(this);

        System.out.println("link Speed in clock = " + Constants.LINK_SPEED_PER_CLOCK);
        System.out.println("Class one count = " + Constants.SMALL_TOTAL_CLOCK_COUNT / Constants.CLASS_ONE_CYCLE);
        System.out.println("Class two count = " + Constants.SMALL_TOTAL_CLOCK_COUNT / Constants.CLASS_TWO_CYCLE);

        int hour = 0;
        for (long clock = 0; clock < Constants.SMALL_TOTAL_CLOCK_COUNT; clock++) {
            for (Node node : nodes) {
                node.simulate(clock);
            }

            for (Switch aSwitch: switches){
                aSwitch.routeReceivedPackets();
            }

            for (Switch aSwitch: switches){
                aSwitch.simulate();
            }

            if (clock % ONE_HOUR_CLOCK_COUNT == 0){
                hour+=1;
                System.out.println("hour = " + hour);
            }

        }

        for (Server server: servers){
            System.out.println(server.getTotalPacket());
        }

    }

    public Node getNode(int id){
        if (id < nodes.length)
            return nodes[id];
        return null;
    }

    public Switch getSwitch(int id){
        if (id < switches.length)
            return switches[id];
        return null;
    }

    public Server getServer(int id){
        if (id < servers.length)
            return servers[id];
        return null;
    }

}
