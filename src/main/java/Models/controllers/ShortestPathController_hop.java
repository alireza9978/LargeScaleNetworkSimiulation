package Models.controllers;

import Models.Network;
import Models.Node;
import Models.Switch;
import constants.Pair;
import constants.PairTriple;

import java.util.*;

public class ShortestPathController_hop extends Controller {


    @Override
    public void initialize(Network network) {
        for (int i = 0; i < network.getNodeCount(); i++) {
            network.getNode(i).setFlowNumber(i);
        }
        for (int i = 0; i < network.getSwitchCount(); i++) {
            network.getSwitch(i).updateRoutingSetting(new Hashtable<>());
        }
    }

    @Override
    public void updatePath(Network network) {

    }

    private void setPathBFS(Network network, int switchId, int nodeId) {
        Queue<PairTriple<Integer, String, String>> switchesId = new ArrayDeque<>();
        HashSet<Integer> visited = new HashSet<>();
        visited.add(switchId);
        switchesId.add(new PairTriple<>(switchId, switchId + "", ""));

        while (!switchesId.isEmpty()) {
            PairTriple<Integer, String, String> tempPair = switchesId.poll();
            int tempId = tempPair.getA();
            String path = tempPair.getB();
            String port = tempPair.getC();

            Switch tempSwitch = network.getSwitch(tempId);

            ArrayList<Pair<Integer, Integer>> serverConnections = tempSwitch.getServersConnections();
            if (serverConnections.size() > 0) {
                port = port + serverConnections.get(0).getValue();
                String[] portsInPath = port.split("-");
                String[] switchesInPath = path.split("-");
                for (int i = 0; i < switchesInPath.length; i++) {
                    int startSwitchId = Integer.parseInt(switchesInPath[i]);
                    int startSwitchPort = Integer.parseInt(portsInPath[i]);
                    network.getSwitch(startSwitchId).addRoutingSetting(nodeId, startSwitchPort);
                }
                break;
            } else {
                for (Pair<Integer, Integer> connection : tempSwitch.getSwitchesConnections()) {
                    int targetSwitchId = connection.getKey();
                    int targetPort = connection.getValue();
                    if (!visited.contains(targetSwitchId)) {
                        switchesId.add(new PairTriple<>(targetSwitchId, path + "-" + targetSwitchId, port + targetPort + "-"));
                        visited.add(targetSwitchId);
                    }
                }
            }


        }
    }

    @Override
    public void updatePath(Network network, ArrayList<Node> activatedNode, ArrayList<Node> deactivatedNode) {
        for (Node node : activatedNode) {
            setPathBFS(network, node.getConnection().getId(), node.id);
        }
    }

    @Override
    public String getName() {
        return "nearest_server_hop";
    }

}
