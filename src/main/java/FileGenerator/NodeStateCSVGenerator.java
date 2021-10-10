package FileGenerator;

import constants.Constants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static constants.Constants.MINIMUM_ACTIVE_NODE_COUNT;

public class NodeStateCSVGenerator {

    public static void main(String[] args) {
        String path = "/home/alireza/projects/java/largeScaleNetworkSimiulation/src/main/java/NetworkStructureFiles/activeNode.csv";
        try (
                Writer writer = Files.newBufferedWriter(Paths.get(path));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Time", "ActiveNode", "ToActivate"));
        ) {

            int minimumNode = MINIMUM_ACTIVE_NODE_COUNT;
            int maximumNode = Constants.NODE_COUNT;
            long totalStep = 5;
            long step = (maximumNode - minimumNode) / totalStep;
            int activeCount = minimumNode;
            long time = Constants.TEN_MINUTE_CLOCK_COUNT;
            for (int i = 0; i < totalStep; i++) {
                activeCount += step;
                csvPrinter.printRecord(time, activeCount, step);
                time += Constants.TEN_MINUTE_CLOCK_COUNT;
            }
            csvPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
