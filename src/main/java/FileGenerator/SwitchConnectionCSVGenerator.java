package FileGenerator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SwitchConnectionCSVGenerator {

    public static void main(String[] args) {
        String path = "/home/alireza/projects/java/largeScaleNetworkSimiulation/src/main/java/NetworkStructureFiles/switches_generated.csv";
        try (
                Writer writer = Files.newBufferedWriter(Paths.get(path));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("id", "port", "target"));
        ) {
            Sample sample = createNetwork(5);
            for (SampleConnection sampleConnection : sample.getConnections()) {
                csvPrinter.printRecord(sampleConnection.getSampleIdStart(), sampleConnection.getSampleIdStartPort(), sampleConnection.getSampleIdEnd());
            }
            csvPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Sample createSample() {
        return new Sample(new Switch(), new Switch(), new Switch());
    }

    private static Sample createNetwork(int size) {
        if (size <= 0) {
            return createSample();
        } else {
            int tempSize = size - 1;
            return new Sample(createNetwork(tempSize), createNetwork(tempSize), createNetwork(tempSize));
        }
    }

    private static class Switch extends Sample {

        private static int ID;
        private final int id;

        public Switch() {
            id = ID;
            ID++;
        }

        public int getId() {
            return id;
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

                    connections.add(new SampleConnection(((Switch) a).getId(), ((Switch) b).getId(), 0));
                    connections.add(new SampleConnection(((Switch) a).getId(), ((Switch) c).getId(), 1));
                    connections.add(new SampleConnection(((Switch) b).getId(), ((Switch) a).getId(), 0));
                    connections.add(new SampleConnection(((Switch) b).getId(), ((Switch) c).getId(), 1));
                    connections.add(new SampleConnection(((Switch) c).getId(), ((Switch) a).getId(), 0));
                    connections.add(new SampleConnection(((Switch) c).getId(), ((Switch) b).getId(), 1));
                } else {
                    connections.add(new SampleConnection(a.getRightConnection().getId(), b.getTopConnection().getId(), 2));
                    connections.add(new SampleConnection(a.getLeftConnection().getId(), c.getTopConnection().getId(), 2));
                    connections.add(new SampleConnection(c.getRightConnection().getId(), b.getLeftConnection().getId(), 2));
                    connections.add(new SampleConnection(b.getTopConnection().getId(), a.getRightConnection().getId(), 2));
                    connections.add(new SampleConnection(c.getTopConnection().getId(), a.getLeftConnection().getId(), 2));
                    connections.add(new SampleConnection(b.getLeftConnection().getId(), c.getRightConnection().getId(), 2));
                }
            }
            return connections;
        }

    }

    static class SampleConnection {

        private final int sampleIdStart;
        private final int sampleIdEnd;
        private final int sampleIdStartPort;

        public SampleConnection(int sampleIdStart, int sampleIdEnd, int sampleIdStartPort) {
            this.sampleIdStart = sampleIdStart;
            this.sampleIdEnd = sampleIdEnd;
            this.sampleIdStartPort = sampleIdStartPort;
        }

        public int getSampleIdStart() {
            return sampleIdStart;
        }

        public int getSampleIdEnd() {
            return sampleIdEnd;
        }

        public int getSampleIdStartPort() {
            return sampleIdStartPort;
        }
    }

}
