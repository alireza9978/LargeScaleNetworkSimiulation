package Models.controllers;

import Models.FlowNumberSetting.NodeFlowNumber;
import Models.FlowNumberSetting.SwitchFlowSetting;
import Models.Network;
import Models.Node;
import constants.NetworkStructureUtil;

import java.util.ArrayList;

public class SimpleFileBasedController extends Controller {


    @Override
    public void initialize(Network network) {
        ArrayList<NodeFlowNumber> nodeFlows = NetworkStructureUtil.getNodeFlowNumber();
        for (NodeFlowNumber temp : nodeFlows) {
            network.getNode(temp.getNodeId()).setFlowNumber(temp.getFlowNumber());
        }

        ArrayList<SwitchFlowSetting> switchesFlow = NetworkStructureUtil.getSwitchFlowNumber();
        for (SwitchFlowSetting temp : switchesFlow) {
            network.getSwitch(temp.getSwitchId()).updateRoutingSetting(temp.getSetting());
        }
    }

    @Override
    public void updatePath(Network network) {

    }

    @Override
    public void updatePath(Network network, ArrayList<Node> activatedNode, ArrayList<Node> deactivatedNode) {

    }

}
