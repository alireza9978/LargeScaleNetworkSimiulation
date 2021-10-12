import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XYChart;

import java.io.File;
import java.io.IOException;

import static constants.Constants.FIGURE_DIR;
import static constants.Constants.GRAPH_DIR;
import static guru.nidi.graphviz.attribute.Attributes.attr;
import static guru.nidi.graphviz.attribute.Rank.RankDir.LEFT_TO_RIGHT;
import static guru.nidi.graphviz.model.Factory.*;

public class MainGraphTest {

    public static void main(String[] args) {
        Graph g = graph("example1").directed()
                .graphAttr().with(Rank.dir(LEFT_TO_RIGHT))
                .nodeAttr().with(Font.name("Arial"))
                .linkAttr().with("class", "link-class")
                .with(
                        node("a").with(Color.RED).link(node("b")),
                        node("b").link(
                                to(node("c")).with(attr("weight", 5), Style.DASHED)
                        )
                );
        try {
            Graphviz.fromGraph(g).height(100).render(Format.PNG).toFile(new File(GRAPH_DIR + "ex1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
