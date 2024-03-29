package Models;

import Models.InfrastructureConnections.*;
import Models.controllers.Controller;
import Visualization.GraphMaker;
import Visualization.Visualization;
import constants.Constants;
import constants.NetworkStructureUtil;
import constants.NodeType;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static constants.Constants.*;

public class Network {

    private Server[] servers;
    private Switch[] switches;
    private Node[] nodes;
    private final int switchCount;
    private final int serverCount;
    private final int nodeCount;
    private final long[] activeNodeCount;
    private final ArrayList<Node> deactivateNodes = new ArrayList<>();
    private final ArrayList<Node> activateNodes = new ArrayList<>();
    private final ArrayList<Node> recentlyActivatedNodes = new ArrayList<>();
    private final ArrayList<Node> recentlyDeactivatedNodes = new ArrayList<>();
    private final NodeActivation[] nodeActivations;
    private final int[] singleServerVmCount;

    public Network() {
        activeNodeCount = new long[NodeType.getCount()];
        initialize();
        ArrayList<SwitchConnection> switchConnections = NetworkStructureUtil.getSwitchesStructure();
        ArrayList<NodeConnection> nodeConnections = NetworkStructureUtil.getNodeStructure();
        ArrayList<ServerConnection> serverConnections = NetworkStructureUtil.getServerStructure();
        ArrayList<VirtualMachineConnection> vmConnections = NetworkStructureUtil.getVirtualMachinesStructure();
        switchCount = connectSwitches(switchConnections);
        nodeCount = connectNodes(nodeConnections);
        serverCount = connectServers(serverConnections);
        singleServerVmCount = startVirtualMachines(vmConnections);
        nodeActivations = NetworkStructureUtil.getActiveNodesStructure().toArray(new NodeActivation[0]);
//        GraphMaker.create(switchConnections, nodeConnections, serverConnections, vmConnections);
    }

    private int[] startVirtualMachines(ArrayList<VirtualMachineConnection> virtualMachineConnections) {
        int[] singleServerVmCount = new int[serverCount];
        for (VirtualMachineConnection connection : virtualMachineConnections) {
            servers[connection.getServerId()].addVM(new VirtualMachine(NodeType.getInstance(connection.getVirtualMachineType()), connection.getServerPort()));
            singleServerVmCount[connection.getServerId()]++;
        }
        return singleServerVmCount;
    }

    private int connectServers(ArrayList<ServerConnection> serverConnections) {
        Set<Integer> uniqueId = new HashSet<>();
        for (ServerConnection connection : serverConnections) {
            uniqueId.add(connection.getId());
            switches[connection.getSwitchId()].connect(servers[connection.getId()], connection.getPort());
        }
        return uniqueId.size();
    }

    private int connectNodes(ArrayList<NodeConnection> nodeConnections) {
        int index = 0;
        for (NodeConnection connection : nodeConnections) {
            for (int i = index; i < index + connection.getNodeCount(); i++) {
                nodes[i].setConnection(switches[connection.getSwitchId()]);
            }
            index = index + connection.getNodeCount();
        }
        return index;
    }

    private int connectSwitches(ArrayList<SwitchConnection> switchConnections) {
        Set<Integer> uniqueId = new HashSet<>();
        for (SwitchConnection connection : switchConnections) {
            uniqueId.add(connection.getStart());
            uniqueId.add(connection.getEnd());
            switches[connection.getStart()].connect(switches[connection.getEnd()], connection.getStartPort());
            switches[connection.getEnd()].connect(switches[connection.getStart()], connection.getEndPort());
        }
        return uniqueId.size();
    }

    private void initialize() {
        servers = new Server[Constants.MAX_SERVER_COUNT];
        switches = new Switch[Constants.MAX_SWITCH_COUNT];
        nodes = new Node[Constants.MAX_NODE_COUNT];

        for (int i = 0; i < servers.length; i++) {
            servers[i] = new Server();
        }

        for (int i = 0; i < switches.length; i++) {
            switches[i] = new Switch();
        }

        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node();
            deactivateNodes.add(nodes[i]);
        }
    }

    public void activateRandomNode(int count, long clock) {
        Random random = new Random();
        System.out.println("random node activation = " + count);
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                Node node = deactivateNodes.remove(random.nextInt(deactivateNodes.size()));
                activeNodeCount[node.getType().toInt()]++;
                node.setOn(true, clock);
                recentlyActivatedNodes.add(node);
            }
        } else {
            count = -count;
            for (int i = 0; i < count; i++) {
                Node node = activateNodes.remove(random.nextInt(activateNodes.size()));
                activeNodeCount[node.getType().toInt()]--;
                node.setOn(false, clock);
                recentlyDeactivatedNodes.add(node);
            }
        }
    }

    private int updateNodesActivationState(Controller controller, int activationPointer, long clock) {
        if (activationPointer < nodeActivations.length)
            if (nodeActivations[activationPointer].getTime() == clock) {
                activateRandomNode(nodeActivations[activationPointer].getToActivate(), clock);
                activationPointer++;
                controller.updatePath(this, recentlyActivatedNodes, recentlyDeactivatedNodes);
                activateNodes.addAll(recentlyActivatedNodes);
                deactivateNodes.addAll(recentlyDeactivatedNodes);
                recentlyActivatedNodes.clear();
                recentlyDeactivatedNodes.clear();
            }
        return activationPointer;
    }

    public void simulate(Controller controller) {
        controller.initialize(this);

        for (Switch aSwitch : switches) {
            if (!aSwitch.checkSetting()) {
                System.out.println("switch setting error");
                return;
            }
        }

        for (Server server : servers)
            for (VirtualMachine vm : server.getVirtualMachines()) {
                if (vm != null)
                    vm.turnOn(1);
            }

        int activationPointer = 0;
        activationPointer = updateNodesActivationState(controller, activationPointer, 0);
        int hour = 0;
        System.out.println("link Speed in clock = " + Constants.LINK_SPEED_PER_CLOCK);
        System.out.println("total clock = " + TOTAL_CLOCK_COUNT);
        System.out.println("total node in network = " + nodeCount);
        Visualization visualization = new Visualization(hour, serverCount, switchCount, singleServerVmCount);

        long start = System.currentTimeMillis();
        for (long clock = 1; clock <= TOTAL_CLOCK_COUNT; clock++) {

            activateNodes.parallelStream().forEach(Node::run);

            Arrays.stream(switches).forEachOrdered(Switch::routeReceivedPackets);

            Arrays.stream(switches).forEachOrdered(Switch::run);

            Arrays.stream(servers).forEachOrdered(Server::run);

            if (clock % CHECK_NODE_ACTIVATION_CLOCK == 0) {
                activationPointer = updateNodesActivationState(controller, activationPointer, clock);
            }

            if (clock % CLOCK_IN_SECOND == 0) {
                System.out.println("simulated second = " + clock / CLOCK_IN_SECOND);
                System.out.println("simulation time in millisecond = " + (System.currentTimeMillis() - start));
            }

            if (clock % UPDATE_NETWORK_PATH_CLOCK_COUNT == 0) {
                controller.updatePath(this);
            }
            if (clock % SAVE_NETWORK_STATE_CLOCK_COUNT == 0) {
                visualization.getData(this);
            }
        }

        visualization.saveCSV(controller.getName());
        System.gc();

        System.out.println("run time = " +
                TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start) + " seconds");
        System.out.println("run time = " + (System.currentTimeMillis() - start) + " milli second");

    }

    public Node getNode(int id) {
        if (id < nodeCount)
            return nodes[id];
        return null;
    }

    public Switch getSwitch(int id) {
        if (id < switchCount)
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

    public long getActiveNodeCount() {
        long sum = 0;
        for (int i = 0; i < NodeType.getCount(); i++) {
            sum += activeNodeCount[i];
        }
        return sum;
    }

    public int getSwitchCount() {
        return switchCount;
    }

    public int getServerCount() {
        return serverCount;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public void resetDataCycle() {
        Arrays.stream(switches).forEach(Switch::resetDataCycle);
        Arrays.stream(servers).forEach(Server::resetDataCycle);
    }

    public ArrayList<Node> getDeactivateNodes() {
        return deactivateNodes;
    }

    public ArrayList<Node> getActivateNodes() {
        return activateNodes;
    }
}
