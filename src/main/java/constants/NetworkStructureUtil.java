package constants;

import Models.FlowNumberSetting.NodeFlowNumber;
import Models.FlowNumberSetting.SwitchFlowSetting;
import Models.InfrastructureConnections.*;
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

import static constants.Constants.MAX_NODE_COUNT;

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


    public static ArrayList<NodeActivation> getActiveNodesStructure() {
        List<CSVRecord> records = readCsv("src/main/java/NetworkStructureFiles/activeNode.csv");
        ArrayList<NodeActivation> nodeActivations = new ArrayList<>();
        assert records != null;
        for (CSVRecord record : records) {
            nodeActivations.add(new NodeActivation(Integer.parseInt(record.get(2)), Long.parseLong(record.get(0))));
        }
        return nodeActivations;
    }

    public static ArrayList<SwitchConnection> getSwitchesStructure() {
        List<CSVRecord> records = readCsv("src/main/java/NetworkStructureFiles/switches_generated.csv");
        ArrayList<SwitchConnection> switchConnections = new ArrayList<>();
        assert records != null;
        for (CSVRecord record : records) {
            switchConnections.add(new SwitchConnection(Integer.parseInt(record.get(0)), Integer.parseInt(record.get(2)),
                    Integer.parseInt(record.get(1)), Integer.parseInt(record.get(3)), Long.parseLong(record.get(4))));
        }
        return switchConnections;
    }

    public static ArrayList<NodeConnection> getNodeStructure() {
        List<CSVRecord> records = readCsv("src/main/java/NetworkStructureFiles/edge_switches_generated.csv");
        assert records != null;

        int edgeSwitchCount = records.size();
        int EDGE_SWITCH_NODE_COUNT = MAX_NODE_COUNT / edgeSwitchCount;
        int NODE_COUNT_TOLERANCE = (int) (EDGE_SWITCH_NODE_COUNT * 0.2f);
        Random random = new Random();
        ArrayList<NodeConnection> nodeConnections = new ArrayList<>();
        int[] switchNodeCount = new int[edgeSwitchCount];
        int maxIteration = edgeSwitchCount;
        if (edgeSwitchCount % 2 == 1) {
            maxIteration -= 1;
            switchNodeCount[edgeSwitchCount - 1] = EDGE_SWITCH_NODE_COUNT;
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

    public static ArrayList<VirtualMachineConnection> getVirtualMachinesStructure() {
        List<CSVRecord> records = readCsv("src/main/java/NetworkStructureFiles/vm_generated.csv");
        assert records != null;
        ArrayList<VirtualMachineConnection> virtualMachineConnections = new ArrayList<>();
        for (CSVRecord record : records) {
            virtualMachineConnections.add(new VirtualMachineConnection(Integer.parseInt(record.get(0)),
                    Integer.parseInt(record.get(1)), Integer.parseInt(record.get(2)), record.get(3)));
        }
        return virtualMachineConnections;
    }


    public static ArrayList<ServerConnection> getServerStructure() {
        List<CSVRecord> records = readCsv("src/main/java/NetworkStructureFiles/servers_generated.csv");
        assert records != null;
        ArrayList<ServerConnection> serverConnections = new ArrayList<>();
        for (CSVRecord record : records) {
            serverConnections.add(new ServerConnection(Integer.parseInt(record.get(0)), Integer.parseInt(record.get(1)),
                    Integer.parseInt(record.get(2)), Long.parseLong(record.get(3))));
        }
        return serverConnections;
    }

    public static ArrayList<NodeFlowNumber> getNodeFlowNumber() {
        List<CSVRecord> records = readCsv("src/main/java/NetworkStructureFiles/flowsNode.csv");
        assert records != null;
        ArrayList<NodeFlowNumber> nodeFlowNumbers = new ArrayList<>();
        for (CSVRecord record : records) {
            nodeFlowNumbers.add(new NodeFlowNumber(Integer.parseInt(record.get(0)), Integer.parseInt(record.get(1))));
        }
        return nodeFlowNumbers;
    }

    public static ArrayList<SwitchFlowSetting> getSwitchFlowNumber() {
        List<CSVRecord> records = readCsv("src/main/java/NetworkStructureFiles/flowsSwitches.csv");
        assert records != null;
        ArrayList<SwitchFlowSetting> nodeFlowNumbers = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            SwitchFlowSetting switchFlowSetting = new SwitchFlowSetting(i);
            switchFlowSetting.addCondition(Integer.parseInt(record.get(0)), Integer.parseInt(record.get(2)));
            nodeFlowNumbers.add(switchFlowSetting);
        }
        return nodeFlowNumbers;
    }
}

