package Models.controllers;

import Models.Network;
import Models.Node;
import Utils.NetworkUtil;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class ShortestPathController_queue extends Controller {


    @Override
    public void initialize(Network network) {
        Random random = new Random();
        for (int i = 0; i < network.getNodeCount(); i++) {
            network.getNode(i).setFlowNumber(i);
            int targetServerId = random.nextInt(network.getServerCount());
            network.getNode(i).setTargetServer(targetServerId);
        }
        for (int i = 0; i < network.getSwitchCount(); i++) {
            network.getSwitch(i).updateRoutingSetting(new Hashtable<>());
        }
    }

    @Override
    public void updatePath(Network network) {
        float[][] connectionsCost = NetworkUtil.getSwitchesConnectionCost(network);
        int[][] connectionsPort = NetworkUtil.getSwitchesConnectionPort(network);
        ArrayList<Node> activeNodes = network.getActivateNodes();
        ArrayList<Integer> path;

        for (Node node : activeNodes) {
            path = NetworkUtil.dijkstra(connectionsCost, node.getConnection().getId(), network.getSwitchCount() + node.getTargetServer());
            for (int i = 0; i < path.size() - 1; i++) {
                int endSwitch = path.get(i);
                int startSwitch = path.get(i + 1);
                int port = connectionsPort[startSwitch][endSwitch];
                network.getSwitch(startSwitch).addRoutingSetting(node.id, port);
            }
        }
    }

    @Override
    public void updatePath(Network network, ArrayList<Node> activatedNode, ArrayList<Node> deactivatedNode) {
        float[][] connections = NetworkUtil.getSwitchesConnectionCost(network);
        int[][] connectionsPort = NetworkUtil.getSwitchesConnectionPort(network);
        ArrayList<Integer> path;

        for (Node node : activatedNode) {
            path = NetworkUtil.dijkstra(connections, node.getConnection().getId(), network.getSwitchCount() + node.getTargetServer());
            for (int i = 0; i < path.size() - 1; i++) {
                int endSwitch = path.get(i);
                int startSwitch = path.get(i + 1);
                int port = connectionsPort[startSwitch][endSwitch];
                network.getSwitch(startSwitch).addRoutingSetting(node.id, port);
            }
        }
    }

}
