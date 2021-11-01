package Visualization;

import Models.InfrastructureConnections.NodeConnection;
import Models.InfrastructureConnections.ServerConnection;
import Models.InfrastructureConnections.SwitchConnection;
import Models.InfrastructureConnections.VirtualMachineConnection;
import constants.Constants;
import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static constants.Constants.GRAPH_DIR;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Link.to;

public class GraphMaker {

    public static void create(ArrayList<SwitchConnection> switchConnections, ArrayList<NodeConnection> nodeConnections,
                              ArrayList<ServerConnection> serverConnections, ArrayList<VirtualMachineConnection> vmConnections) {
        Graph g = graph("example1").nodeAttr().with(Font.name("Arial"));
        String speed;
        for (SwitchConnection connection : switchConnections) {
            Color color;
            if (connection.getLinkSpeed() == Constants.SWITCH_LINK_SPEED_SLOW) {
                color = Color.BLUEVIOLET;
                speed = Constants.SWITCH_LINK_SPEED_SLOW_NAME;
            } else {
                color = Color.ORANGE;
                speed = Constants.SWITCH_LINK_SPEED_FAST_NAME;
            }
            g = g.with(node("" + connection.getStart()).link(to(node("" + connection.getEnd())).with(Label.of(speed), color)));
        }
        for (NodeConnection nodeConnection : nodeConnections) {
            Node tempSwitch = node("" + nodeConnection.getSwitchId()).with(Color.RED);
            g = g.with(tempSwitch);
            Node nodes = node("id=" + nodeConnection.getSwitchId() + "_" + nodeConnection.getNodeCount())
                    .with(Shape.DIAMOND).with(Color.GREEN);
            g = g.with(nodes.link(tempSwitch));
        }
        speed = Constants.SERVER_LINK_SPEED_NAME;
        for (ServerConnection serverConnection : serverConnections) {
            ArrayList<String> recs = new ArrayList<>();
            for (VirtualMachineConnection virtualMachineConnection : vmConnections) {
                if (virtualMachineConnection.getServerId() == serverConnection.getId()) {
                    recs.add(Records.rec("vm_port=" +
                            virtualMachineConnection.getServerPort()
                            + "_type=" + virtualMachineConnection.getVirtualMachineType()));
                }
            }
            recs.add(Records.rec("server_" + serverConnection.getId()));
            Node server = node("server_" + serverConnection.getId()).with(Shape.SQUARE).with(Color.BLUE).
                    with(Records.of(Records.turn(recs.toArray(new String[0]))));

            g = g.with(server.link(to(node("" + serverConnection.getSwitchId())).with(Label.of(speed), Color.RED2)));
        }

        try {
            Graphviz.fromGraph(g).height(5000).width(5000).render(Format.PNG).toFile(new File(GRAPH_DIR + "network.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
