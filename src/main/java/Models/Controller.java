package Models;

import Models.FlowNumberSetting.NodeFlowNumber;
import Models.FlowNumberSetting.SwitchFlowSetting;
import constants.NetworkStructureUtil;

import java.util.ArrayList;

public class Controller {

    public static void setPath(Network network) {
        ArrayList<NodeFlowNumber> nodeFlows = NetworkStructureUtil.getNodeFlowNumber();
        for (NodeFlowNumber temp : nodeFlows) {
            network.getNode(temp.getNodeId()).setFlowNumber(temp.getFlowNumber());
        }

        ArrayList<SwitchFlowSetting> switchesFlow = NetworkStructureUtil.getSwitchFlowNumber();
        for (SwitchFlowSetting temp : switchesFlow) {
            network.getSwitch(temp.getSwitchId()).updateRoutingSetting(temp.getSetting());
        }
    }

}
