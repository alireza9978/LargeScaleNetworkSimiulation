package Models;

import Models.InfrastructureConnections.*;
import Visualization.Visualization;
import constants.Constants;
import constants.NetworkStructureUtil;
import constants.NodeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static constants.Constants.*;

public class Network {

    private Server[] servers;
    private Switch[] switches;
    private Node[] nodes;
    private final long[] activeNodeCount;
    private final ArrayList<Node> deactivateNodes = new ArrayList<>();
    private final ArrayList<Node> activateNodes = new ArrayList<>();
    private final NodeActivation[] nodeActivations;

    public Network(int serverCount, int switchCount, int nodeCount) {
        activeNodeCount = new long[NodeType.getCount()];
        initialize(serverCount, switchCount, nodeCount);
        connectSwitches(NetworkStructureUtil.getSwitchesStructure());
        connectNodes(NetworkStructureUtil.getNodeStructure());
        connectServers(NetworkStructureUtil.getServerStructure());
        startVirtualMachines(NetworkStructureUtil.getVirtualMachinesStructure());
        nodeActivations = NetworkStructureUtil.getActiveNodesStructure().toArray(new NodeActivation[0]);
    }

    private void startVirtualMachines(ArrayList<VirtualMachineConnection> virtualMachineConnections) {
        for (VirtualMachineConnection connection : virtualMachineConnections) {
            servers[connection.getServerId()].addVM(new VirtualMachine(NodeType.getInstance(connection.getVirtualMachineType()), connection.getServerPort()));
        }
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
            switches[connection.getEnd()].connect(switches[connection.getStart()], connection.getEndPort());
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
            deactivateNodes.add(nodes[i]);
        }
        activateRandomNode(Constants.MINIMUM_ACTIVE_NODE_COUNT);
    }

    public void activateRandomNode(int count) {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            Node node = deactivateNodes.remove(random.nextInt(deactivateNodes.size()));
            activeNodeCount[node.getType().toInt()]++;
            node.setOn(true);
            activateNodes.add(node);
        }
    }

    public void deactivateRandomNode(int count) {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            Node node = activateNodes.remove(random.nextInt(deactivateNodes.size()));
            activeNodeCount[node.getType().toInt()]--;
            node.setOn(false);
            deactivateNodes.add(node);
        }
    }

    public void simulate() {
        Controller.setPath(this);

        for (Switch aSwitch : switches) {
            if (!aSwitch.checkSetting()) {
                System.out.println("switch setting error");
                return;
            }
        }

        System.out.println("link Speed in clock = " + Constants.LINK_SPEED_PER_CLOCK);

        long start = System.currentTimeMillis();
        int hour = 0;
        int activationPointer = 0;
        System.out.println("total clock = " + SMALL_TOTAL_CLOCK_COUNT);
        Visualization visualization = new Visualization(hour);

        for (long clock = 1; clock <= Constants.SMALL_TOTAL_CLOCK_COUNT; clock++) {

            Arrays.stream(nodes).forEachOrdered(Node::run);

            Arrays.stream(switches).forEachOrdered(Switch::routeReceivedPackets);

            Arrays.stream(switches).forEachOrdered(Switch::run);

            Arrays.stream(servers).forEachOrdered(Server::run);

            if (clock % TEN_MINUTE_CLOCK_COUNT == 0) {
                System.out.println("second = " + clock / CLOCK_IN_SECOND);
                if (activationPointer < nodeActivations.length)
                    if (nodeActivations[activationPointer].getTime() == clock) {
                        activateRandomNode(nodeActivations[activationPointer].getToActivate());
                        activationPointer++;
                    }
            }

            if (clock % ONE_HOUR_CLOCK_COUNT == 0) {
                hour += 1;
                visualization.plot();
                visualization = new Visualization(hour);
                System.out.println("hour = " + hour);
                System.gc();
            }

            if (clock % CLOCK_IN_SECOND == 0) {
                visualization.getData(this);
            }

        }

        System.out.println("run time = " +
                TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start) + " seconds");

        Arrays.stream(servers).map(Server::toString).forEachOrdered(System.out::println);

    }

    public Node getNode(int id) {
        if (id < nodes.length)
            return nodes[id];
        return null;
    }

    public Switch getSwitch(int id) {
        if (id < switches.length)
            return switches[id];
        return null;
    }

    public Server getServer(int id) {
        if (id < servers.length)
            return servers[id];
        return null;
    }

    public long getActiveNodeCount(int type) {
        return activeNodeCount[type];
    }

}
