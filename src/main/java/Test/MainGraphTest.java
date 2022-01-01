package Test;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;

import java.io.File;
import java.io.IOException;

import static constants.Constants.GRAPH_DIR;
import static guru.nidi.graphviz.attribute.Rank.RankDir.TOP_TO_BOTTOM;
import static guru.nidi.graphviz.model.Factory.*;

public class MainGraphTest {

    public static void main(String[] args) {
        Graph g = graph("example1")
                .graphAttr().with(Rank.dir(TOP_TO_BOTTOM))
                .nodeAttr().with(Font.name("Arial"))
                .linkAttr().with("class", "link-class");
        g = g.with(node("a").with(Color.RED).link(node("b")));
        g = g.with(node("a").link(node("c")));
        g = g.with(node("b").link(node("c")));
        g = g.with(node("c").link(node("b")));

        try {
            Graphviz.fromGraph(g).height(100).render(Format.PNG).toFile(new File(GRAPH_DIR + "test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
