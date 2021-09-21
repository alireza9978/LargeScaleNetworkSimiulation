package constants;

import Models.FlowNumberSetting.NodeFlowNumber;
import Models.FlowNumberSetting.SwitchFlowSetting;
import Models.InfrastructureConnections.NodeConnection;
import Models.InfrastructureConnections.ServerConnection;
import Models.InfrastructureConnections.SwitchConnection;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static constants.Constants.*;

public class NetworkStructureUtil {

    public static List<CSVRecord> readCsv(String path) {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(path));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {
            return csvParser.getRecords().subList(1, (int) csvParser.getRecordNumber());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<SwitchConnection> getSwitchesStructure() {
        List<CSVRecord> records = readCsv("/home/alireza/projects/java/largeScaleNetworkSimiulation/src/NetworkStructureFiles/switches.csv");
        ArrayList<SwitchConnection> switchConnections = new ArrayList<>();
        assert records != null;
        for (CSVRecord record : records) {
            switchConnections.add(new SwitchConnection(Integer.parseInt(record.get(0)), Integer.parseInt(record.get(2)),
                    Integer.parseInt(record.get(1)), Integer.parseInt(record.get(3))));
        }
        return switchConnections;
    }

    public static ArrayList<NodeConnection> getNodeStructure() {
        List<CSVRecord> records = readCsv("/home/alireza/projects/java/largeScaleNetworkSimiulation/src/NetworkStructureFiles/edgesSwitches.csv");
        assert records != null;

        Random random = new Random();
        ArrayList<NodeConnection> nodeConnections = new ArrayList<>();
        int[] switchNodeCount = new int[EDGE_SWITCH_COUNT];
        int maxIteration = EDGE_SWITCH_COUNT;
        if (EDGE_SWITCH_COUNT % 2 == 1) {
            maxIteration -= 1;
            switchNodeCount[EDGE_SWITCH_COUNT - 1] = EDGE_SWITCH_NODE_COUNT;
        }
        for (int i = 0; i < maxIteration; i += 2) {
            int temp = Math.round((random.nextFloat() - 0.5f) * 2 * NODE_COUNT_TOLERANCE);
            switchNodeCount[i] = EDGE_SWITCH_NODE_COUNT + temp;
            switchNodeCount[i + 1] = EDGE_SWITCH_NODE_COUNT - temp;
        }

        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            nodeConnections.add(new NodeConnection(Integer.parseInt(record.get(0)), switchNodeCount[i]));
        }
        return nodeConnections;
    }


    public static ArrayList<ServerConnection> getServerStructure() {
        List<CSVRecord> records = readCsv("/home/alireza/projects/java/largeScaleNetworkSimiulation/src/NetworkStructureFiles/servers.csv");
        assert records != null;
        ArrayList<ServerConnection> serverConnections = new ArrayList<>();
        for (CSVRecord record : records) {
            serverConnections.add(new ServerConnection(Integer.parseInt(record.get(0)), Integer.parseInt(record.get(1)),
                    Integer.parseInt(record.get(2))));
        }
        return serverConnections;
    }

    public static ArrayList<NodeFlowNumber> getNodeFlowNumber() {
        List<CSVRecord> records = readCsv("/home/alireza/projects/java/largeScaleNetworkSimiulation/src/NetworkStructureFiles/flowsNode.csv");
        assert records != null;
        ArrayList<NodeFlowNumber> nodeFlowNumbers = new ArrayList<>();
        for (CSVRecord record : records) {
            nodeFlowNumbers.add(new NodeFlowNumber(Integer.parseInt(record.get(0)), Integer.parseInt(record.get(1))));
        }
        return nodeFlowNumbers;
    }

    public static ArrayList<SwitchFlowSetting> getSwitchFlowNumber() {
        List<CSVRecord> records = readCsv("/home/alireza/projects/java/largeScaleNetworkSimiulation/src/NetworkStructureFiles/flowsSwitches.csv");
        assert records != null;
        ArrayList<SwitchFlowSetting> nodeFlowNumbers = new ArrayList<>();
        for (int i = 0; i < SWITCH_COUNT; i++) {
            nodeFlowNumbers.add(new SwitchFlowSetting(i));
        }
        for (CSVRecord record : records) {
            nodeFlowNumbers.get(Integer.parseInt(record.get(1))).addCondition(Integer.parseInt(record.get(0)),
                    Integer.parseInt(record.get(2)));
        }
        return nodeFlowNumbers;
    }
}

