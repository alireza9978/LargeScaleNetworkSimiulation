package Models.controllers;

import Models.FlowNumberSetting.NodeFlowNumber;
import Models.FlowNumberSetting.SwitchFlowSetting;
import Models.Network;
import Models.Node;
import constants.NetworkStructureUtil;

import java.util.ArrayList;

public abstract class Controller {

    public abstract void initialize(Network network);
    public abstract void updatePath(Network network);
    public abstract void updatePath(Network network, ArrayList<Node> activatedNode, ArrayList<Node> deactivatedNode);

}
