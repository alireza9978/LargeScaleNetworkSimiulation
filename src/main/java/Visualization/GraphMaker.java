package Visualization;

import Models.InfrastructureConnections.NodeConnection;
import Models.InfrastructureConnections.SwitchConnection;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static constants.Constants.GRAPH_DIR;
import static guru.nidi.graphviz.attribute.Rank.RankType.SAME;
import static guru.nidi.graphviz.attribute.LinkAttr.CONSTRAINT_NOT;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;

public class GraphMaker {

    public static void create(ArrayList<SwitchConnection> switchConnections, ArrayList<NodeConnection> nodeConnections) {
        Graph g = graph("example1")
                .nodeAttr().with(Font.name("Arial"));
        for (SwitchConnection connection : switchConnections) {
            g = g.with(node("" + connection.getStart()).link(node("" + connection.getEnd())));
        }
        for (NodeConnection nodeConnection : nodeConnections) {

            g = g.with(node("" + nodeConnection.getSwitchId()).with(Color.RED));
        }
        try {
            Graphviz.fromGraph(g).height(5000).width(5000).render(Format.PNG).toFile(new File(GRAPH_DIR + "network.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
