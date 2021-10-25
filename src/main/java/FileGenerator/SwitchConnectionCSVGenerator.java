package FileGenerator;

import constants.Constants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;

public class SwitchConnectionCSVGenerator {

    public static void main(String[] args) {
        String switchesPath = Constants.ROOT_DIR + "src/main/java/NetworkStructureFiles/switches_generated.csv";
        String serversPath = Constants.ROOT_DIR + "src/main/java/NetworkStructureFiles/servers_generated.csv";
        String edgeSwitchesPath = Constants.ROOT_DIR + "src/main/java/NetworkStructureFiles/edge_switches_generated.csv";
        try (
                Writer switchesWriter = Files.newBufferedWriter(Paths.get(switchesPath));
                Writer serversWriter = Files.newBufferedWriter(Paths.get(serversPath));
                Writer edgeSwitchesWriter = Files.newBufferedWriter(Paths.get(edgeSwitchesPath));
                CSVPrinter switchersCsvPrinter = new CSVPrinter(switchesWriter, CSVFormat.DEFAULT.withHeader("id", "port", "target", "target_port"));
                CSVPrinter serversCsvPrinter = new CSVPrinter(serversWriter, CSVFormat.DEFAULT.withHeader("id", "switch", "port"));
                CSVPrinter edgeSwitchesCsvPrinter = new CSVPrinter(edgeSwitchesWriter, CSVFormat.DEFAULT.withHeader("id"));
        ) {
            Sample sample = createNetwork(5, "");

            serversCsvPrinter.printRecord(0, sample.getTopConnection().getId(), 2);
            serversCsvPrinter.printRecord(1, sample.getRightConnection().getId(), 2);
            serversCsvPrinter.printRecord(2, sample.getLeftConnection().getId(), 2);
            serversCsvPrinter.flush();

            for (Integer switchId : sample.getEdgeSwitches()) {
                edgeSwitchesCsvPrinter.printRecord(switchId);
            }
            edgeSwitchesCsvPrinter.flush();

            for (SampleConnection sampleConnection : sample.getConnections()) {
                switchersCsvPrinter.printRecord(sampleConnection.getSampleIdStart(), sampleConnection.getSampleStartPort(),
                        sampleConnection.getSampleIdEnd(), sampleConnection.getSampleEndPort());
            }
            switchersCsvPrinter.flush();
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

        public ArrayList<SampleConnection> getConnections() {
            ArrayList<SampleConnection> connections = new ArrayList<>();
            if (a != null && b != null && c != null) {
                connections.addAll(getA().getConnections());
                connections.addAll(getB().getConnections());
                connections.addAll(getC().getConnections());
                if (a instanceof SwitchConnectionCSVGenerator.Switch
                        && b instanceof SwitchConnectionCSVGenerator.Switch
                        && c instanceof SwitchConnectionCSVGenerator.Switch) {

                    connections.add(new SampleConnection(((Switch) a).getId(), ((Switch) b).getId(), 0, 0));
                    connections.add(new SampleConnection(((Switch) a).getId(), ((Switch) c).getId(), 1, 0));
                    connections.add(new SampleConnection(((Switch) b).getId(), ((Switch) c).getId(), 1, 1));
                } else {
                    connections.add(new SampleConnection(a.getRightConnection().getId(), b.getTopConnection().getId(), 2, 2));
                    connections.add(new SampleConnection(a.getLeftConnection().getId(), c.getTopConnection().getId(), 2, 2));
                    connections.add(new SampleConnection(c.getRightConnection().getId(), b.getLeftConnection().getId(), 2, 2));
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

        public SampleConnection(int sampleIdStart, int sampleIdEnd, int sampleStartPort, int sampleEndPort) {
            this.sampleIdStart = sampleIdStart;
            this.sampleIdEnd = sampleIdEnd;
            this.sampleStartPort = sampleStartPort;
            this.sampleEndPort = sampleEndPort;
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
    }

}
