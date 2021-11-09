package FileGenerator;

import constants.Constants;
import constants.Gaussian;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static constants.Constants.MINIMUM_ACTIVE_NODE_COUNT;

public class NodeStateCSVGeneratorGaussian {

    public void create() {
        String path = Constants.ROOT_DIR + "src/main/java/NetworkStructureFiles/activeNode.csv";
        try (
                Writer writer = Files.newBufferedWriter(Paths.get(path));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Time", "ActiveNode", "ToActivate"))
        ) {
            int minActiveCount = MINIMUM_ACTIVE_NODE_COUNT;
            int smallPeak = Constants.MAX_NODE_COUNT / 2 - minActiveCount;
            int bigPeak = Constants.MAX_NODE_COUNT - minActiveCount;
            long totalStep = 360;
            long timeStep = Constants.TOTAL_CLOCK_COUNT / totalStep;
            long time = 0;

            ArrayList<Long> timeArray = new ArrayList<>();
            ArrayList<Long> activeNodeCount = new ArrayList<>();
            ArrayList<Long> stepArray = new ArrayList<>();
            Gaussian small = new Gaussian(12, 110);
            Gaussian big = new Gaussian(9, 270);
            Long tempActiveNodeCount = null;
            for (int i = 0; i < totalStep; i++) {
                timeArray.add(time);
                if (tempActiveNodeCount == null){
                    tempActiveNodeCount = Math.round(small.getY(i) * smallPeak + big.getY(i) * bigPeak + minActiveCount);
                    stepArray.add(tempActiveNodeCount);
                }else{
                    long temp = Math.round(small.getY(i) * smallPeak + big.getY(i) * bigPeak + minActiveCount);
                    stepArray.add(temp - tempActiveNodeCount);
                    tempActiveNodeCount = temp;
                }
                activeNodeCount.add(tempActiveNodeCount);
                time+=timeStep;
            }
            for (int i = 0; i < totalStep; i++){
                csvPrinter.printRecord(timeArray.get(i), activeNodeCount.get(i), stepArray.get(i));
            }
            csvPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NodeStateCSVGeneratorGaussian nodeStateCSVGenerator = new NodeStateCSVGeneratorGaussian();
        nodeStateCSVGenerator.create();
    }

}
