package FileGenerator;

import constants.Constants;
import constants.NodeType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;

public class NetworkConnectionCSVGenerator {

    public static void main(String[] args) {
        NetworkConnectionCSVGenerator networkConnectionCSVGenerator = new NetworkConnectionCSVGenerator();
        networkConnectionCSVGenerator.create();
    }

    public void create() {
        String switchesPath = Constants.ROOT_DIR + "src/main/java/NetworkStructureFiles/switches_generated.csv";
        String serversPath = Constants.ROOT_DIR + "src/main/java/NetworkStructureFiles/servers_generated.csv";
        String edgeSwitchesPath = Constants.ROOT_DIR + "src/main/java/NetworkStructureFiles/edge_switches_generated.csv";
        String vmPath = Constants.ROOT_DIR + "src/main/java/NetworkStructureFiles/vm_generated.csv";
        try (
                Writer switchesWriter = Files.newBufferedWriter(Paths.get(switchesPath));
                Writer serversWriter = Files.newBufferedWriter(Paths.get(serversPath));
                Writer edgeSwitchesWriter = Files.newBufferedWriter(Paths.get(edgeSwitchesPath));
                Writer vmWriter = Files.newBufferedWriter(Paths.get(vmPath));
                CSVPrinter switchersCsvPrinter = new CSVPrinter(switchesWriter, CSVFormat.DEFAULT.withHeader("id", "port", "target", "target_port", "link_speed"));
                CSVPrinter serversCsvPrinter = new CSVPrinter(serversWriter, CSVFormat.DEFAULT.withHeader("id", "switch", "port", "link_speed"));
                CSVPrinter edgeSwitchesCsvPrinter = new CSVPrinter(edgeSwitchesWriter, CSVFormat.DEFAULT.withHeader("id"));
                CSVPrinter vmCsvPrinter = new CSVPrinter(vmWriter, CSVFormat.DEFAULT.withHeader("id", "server", "port", "type"))

        ) {
            Sample sample = createNetwork(1, "");

            long serversLinkSpeed = Constants.SERVER_LINK_SPEED;
            serversCsvPrinter.printRecord(0, sample.getTopConnection().getId(), 2, serversLinkSpeed);
            serversCsvPrinter.printRecord(1, sample.getRightConnection().getId(), 2, serversLinkSpeed);
            serversCsvPrinter.printRecord(2, sample.getLeftConnection().getId(), 2, serversLinkSpeed);
            serversCsvPrinter.flush();

            for (Integer switchId : sample.getEdgeSwitches()) {
                edgeSwitchesCsvPrinter.printRecord(switchId);
            }
            edgeSwitchesCsvPrinter.flush();

            for (SampleConnection sampleConnection : sample.getConnections(0)) {
                switchersCsvPrinter.printRecord(sampleConnection.getSampleIdStart(), sampleConnection.getSampleStartPort(),
                        sampleConnection.getSampleIdEnd(), sampleConnection.getSampleEndPort(), sampleConnection.getLinkSpeed());
            }
            switchersCsvPrinter.flush();

            int vmId = 0;
            for (int j = 0; j < 3; j++) { //loop over servers
                for (int i = 0; i < NodeType.getCount(); i++) {
                    vmCsvPrinter.printRecord(vmId, j, i, i);
                    vmId++;
                }
            }
            vmCsvPrinter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Sample createSample(String path) {
        return new Sample(new Switch(path + "a"), new Switch(path + "b"), new Switch(path + "c"));
    }

    private static Sample createNetwork(int size, String path) {
        if (size <= 0) {
            return createSample(path);
        } else {
            int tempSize = size - 1;
            return new Sample(createNetwork(tempSize, path + "a"), createNetwork(tempSize, path + "b"),
                    createNetwork(tempSize, path + "c"));
        }
    }

    private static class Switch extends Sample {

        private static int ID;
        private final int id;
        private final boolean edge;

        public Switch(String path) {
            id = ID;
            ID++;
            HashSet<Character> characterHashSet = new HashSet<>();
            for (int i = 0; i < path.length(); i++) {
                characterHashSet.add(path.charAt(i));
            }
            edge = characterHashSet.size() < 3;
        }

        public int getId() {
            return id;
        }

        public boolean isEdge() {
            return edge;
        }
    }

    private static class Sample {

        private Sample a;
        private Sample b;
        private Sample c;

        public Sample(Sample a, Sample b, Sample c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public Sample() {

        }

        public Sample getA() {
            return a;
        }

        public Sample getB() {
            return b;
        }

        public Sample getC() {
            return c;
        }

        public Switch getTopConnection() {
            if (a != null) {
                if (a instanceof Switch) {
                    return (Switch) a;
                } else {
                    return a.getTopConnection();
                }
            } else {
                return null;
            }
        }

        public Switch getRightConnection() {
            if (b != null) {
                if (b instanceof Switch) {
                    return (Switch) b;
                } else {
                    return b.getRightConnection();
                }
            } else {
                return null;
            }
        }

        public Switch getLeftConnection() {
            if (c != null) {
                if (c instanceof Switch) {
                    return (Switch) c;
                } else {
                    return c.getLeftConnection();
                }
            } else {
                return null;
            }
        }

        public ArrayList<SampleConnection> getConnections(int depth) {
            long linkSpeed;
            if (depth < 1) {
                linkSpeed = Constants.SWITCH_LINK_SPEED_FAST;
            } else {
                linkSpeed = Constants.SWITCH_LINK_SPEED_SLOW;
            }
            ArrayList<SampleConnection> connections = new ArrayList<>();
            if (a != null && b != null && c != null) {
                connections.addAll(getA().getConnections(depth + 1));
                connections.addAll(getC().getConnections(depth + 1));
                connections.addAll(getB().getConnections(depth + 1));
                if (a instanceof NetworkConnectionCSVGenerator.Switch
                        && b instanceof NetworkConnectionCSVGenerator.Switch
                        && c instanceof NetworkConnectionCSVGenerator.Switch) {
                    connections.add(new SampleConnection(((Switch) a).getId(), ((Switch) b).getId(), 0, 0, linkSpeed));
                    connections.add(new SampleConnection(((Switch) b).getId(), ((Switch) c).getId(), 1, 1, linkSpeed));
                    connections.add(new SampleConnection(((Switch) a).getId(), ((Switch) c).getId(), 1, 0, linkSpeed));
                } else {
                    connections.add(new SampleConnection(a.getRightConnection().getId(), b.getTopConnection().getId(), 2, 2, linkSpeed));
                    connections.add(new SampleConnection(c.getRightConnection().getId(), b.getLeftConnection().getId(), 2, 2, linkSpeed));
                    connections.add(new SampleConnection(a.getLeftConnection().getId(), c.getTopConnection().getId(), 2, 2, linkSpeed));
                }
            }
            return connections;
        }

        public void getInnerEdge(Sample sample, ArrayList<Integer> edgeSwitchesId) {
            if (sample instanceof Switch) {
                if (((Switch) sample).isEdge()) {
                    edgeSwitchesId.add(((Switch) sample).getId());
                }
            } else {
                getInnerEdge(sample.getA(), edgeSwitchesId);
                getInnerEdge(sample.getB(), edgeSwitchesId);
                getInnerEdge(sample.getC(), edgeSwitchesId);
            }
        }

        public ArrayList<Integer> getEdgeSwitches() {
            ArrayList<Integer> edgeSwitchesId = new ArrayList<>();
            getInnerEdge(getA(), edgeSwitchesId);
            getInnerEdge(getB(), edgeSwitchesId);
            getInnerEdge(getC(), edgeSwitchesId);
            ArrayList<Integer> serverConnectionSwitches = new ArrayList<>();
            serverConnectionSwitches.add(getRightConnection().getId());
            serverConnectionSwitches.add(getLeftConnection().getId());
            serverConnectionSwitches.add(getTopConnection().getId());
            edgeSwitchesId.removeAll(serverConnectionSwitches);
            return edgeSwitchesId;
        }

    }

    static class SampleConnection {

        private final int sampleIdStart;
        private final int sampleIdEnd;
        private final int sampleStartPort;
        private final int sampleEndPort;
        private final long linkSpeed;

        public SampleConnection(int sampleIdStart, int sampleIdEnd, int sampleStartPort, int sampleEndPort, long linkSpeed) {
            this.sampleIdStart = sampleIdStart;
            this.sampleIdEnd = sampleIdEnd;
            this.sampleStartPort = sampleStartPort;
            this.sampleEndPort = sampleEndPort;
            this.linkSpeed = linkSpeed;
        }

        public int getSampleIdStart() {
            return sampleIdStart;
        }

        public int getSampleIdEnd() {
            return sampleIdEnd;
        }

        public int getSampleStartPort() {
            return sampleStartPort;
        }

        public int getSampleEndPort() {
            return sampleEndPort;
        }

        public long getLinkSpeed() {
            return linkSpeed;
        }
    }

}
