package FileGenerator;

import constants.Constants;
import constants.Gaussian;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XYChart;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static constants.Constants.*;

public class NodeStateCSVGeneratorGaussian {

    private final ArrayList<Long> timeArray = new ArrayList<>();
    private final ArrayList<Long> activeNodeCount = new ArrayList<>();
    private final ArrayList<Long> stepArray = new ArrayList<>();

    public void create() {
        String path = Constants.ROOT_DIR + "src/main/java/NetworkStructureFiles/activeNode.csv";
        try (
                Writer writer = Files.newBufferedWriter(Paths.get(path));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Time", "ActiveNode", "ToActivate"))
        ) {
            int minActiveCount = MINIMUM_ACTIVE_NODE_COUNT;
            int smallPeak = Constants.MAX_NODE_COUNT / 2 - minActiveCount;
            int bigPeak = (int) (Constants.MAX_NODE_COUNT * 0.9) - minActiveCount;
            long totalStep = NODE_ACTIVATION_STEPS_COUNT;
            long timeStep = Constants.TOTAL_CLOCK_COUNT / totalStep;
            long time = 0;

            Gaussian small = new Gaussian(12, 110);
            Gaussian big = new Gaussian(9, 270);
            Long tempActiveNodeCount = null;
            for (int i = 0; i < totalStep; i++) {
                timeArray.add(time);
                if (tempActiveNodeCount == null) {
                    tempActiveNodeCount = Math.round(small.getY(i) * smallPeak + big.getY(i) * bigPeak + minActiveCount);
                    stepArray.add(tempActiveNodeCount);
                } else {
                    long temp = Math.round(small.getY(i) * smallPeak + big.getY(i) * bigPeak + minActiveCount);
                    stepArray.add(temp - tempActiveNodeCount);
                    tempActiveNodeCount = temp;
                }
                activeNodeCount.add(tempActiveNodeCount);
                time += timeStep;
            }
            for (int i = 0; i < totalStep; i++) {
                csvPrinter.printRecord(timeArray.get(i), activeNodeCount.get(i), stepArray.get(i));
            }
            csvPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Long> getTimeArray() {
        return timeArray;
    }

    public ArrayList<Long> getActiveNodeCount() {
        return activeNodeCount;
    }

    public static void main(String[] args) {
        NodeStateCSVGeneratorGaussian nodeStateCSVGenerator = new NodeStateCSVGeneratorGaussian();
        nodeStateCSVGenerator.create();
        XYChart inputPacketChart = QuickChart.getChart("Node State Gaussian", "Time", "Active Node Count",
                "active node count", nodeStateCSVGenerator.getTimeArray(), nodeStateCSVGenerator.getActiveNodeCount());
        try {
            BitmapEncoder.saveBitmapWithDPI(inputPacketChart, ROOT_DIR + "src/main/resources/generated/gaussian",
                    BitmapEncoder.BitmapFormat.PNG, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
