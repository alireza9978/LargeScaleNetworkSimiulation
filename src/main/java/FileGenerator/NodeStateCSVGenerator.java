package FileGenerator;

import constants.Constants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static constants.Constants.MINIMUM_ACTIVE_NODE_COUNT;

public class NodeStateCSVGenerator {

    public void create() {
        String path = "src/main/java/NetworkStructureFiles/activeNode.csv";
        try (
                Writer writer = Files.newBufferedWriter(Paths.get(path));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Time", "ActiveNode", "ToActivate"))
        ) {
            int minimumNode = MINIMUM_ACTIVE_NODE_COUNT;
            int maximumNode = Constants.MAX_NODE_COUNT;
            long totalStep = 120;
            long timeStep = Constants.TOTAL_CLOCK_COUNT / totalStep;
            long step = ((maximumNode - minimumNode) / 50);
            int activeCount = minimumNode;
            long time = 0;
            csvPrinter.printRecord(time, activeCount, minimumNode);
            time += timeStep;
            for (int i = 0; i < 50; i++) {
                activeCount += step;
                csvPrinter.printRecord(time, activeCount, step);
                time += timeStep;
            }
            for (int i = 0; i < 20; i++) {
                csvPrinter.printRecord(time, activeCount, 0);
                time += timeStep;
            }
            for (int i = 0; i < 50; i++) {
                activeCount -= step;
                csvPrinter.printRecord(time, activeCount, -step);
                time += timeStep;
            }
            csvPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NodeStateCSVGenerator nodeStateCSVGenerator = new NodeStateCSVGenerator();
        nodeStateCSVGenerator.create();
    }

}
