package Visualization;

import Models.Network;
import Models.Server;
import Models.StatsModels.EndToEndDelay;
import Models.Switch;
import Models.VirtualMachine;
import constants.Constants;
import constants.NodeType;
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
import java.util.stream.IntStream;

import static constants.Constants.*;

public class Visualization {

    private final ArrayList<Integer> xAxis = new ArrayList<>();
    private int clock = 0;
    private final int hour;
    private final int serverCount;
    private final int switchCount;
    private final int[] singleServerVmCount;
    private final ArrayList<Integer>[] switchesInputPackets = new ArrayList[MAX_SWITCH_COUNT];
    private final ArrayList<Integer[]>[] switchesDroppedPackets = new ArrayList[MAX_SWITCH_COUNT];
    private final ArrayList<Float>[] switchesQueuePackets = new ArrayList[MAX_SWITCH_COUNT];
    private final ArrayList<Float>[] serverUtilization = new ArrayList[MAX_SERVER_COUNT];
    private final ArrayList<Long>[] serverInputPackets = new ArrayList[MAX_SERVER_COUNT];
    private final ArrayList<Float>[][] vmUtilization = new ArrayList[MAX_SERVER_COUNT][MAX_VM_IN_SINGLE_SERVER_COUNT];
    private final ArrayList<Long> activeNodeCount = new ArrayList<>();
    private final ArrayList<EndToEndDelay> endToEndDelays = new ArrayList<>();

    public Visualization(int hour, int serverCount, int switchCount, int[] singleServerVmCount) {
        this.hour = hour;
        this.serverCount = serverCount;
        this.switchCount = switchCount;
        this.singleServerVmCount = singleServerVmCount;
        for (int i = 0; i < serverCount; i++) {
            serverUtilization[i] = new ArrayList<>();
            serverInputPackets[i] = new ArrayList<>();
            for (int j = 0; j < vmUtilization[i].length; j++) {
                vmUtilization[i][j] = new ArrayList<>();
            }
        }
        for (int i = 0; i < switchCount; i++) {
            switchesInputPackets[i] = new ArrayList<>();
            switchesQueuePackets[i] = new ArrayList<>();
            switchesDroppedPackets[i] = new ArrayList<>();
        }
    }

    public void getData(Network network) {
        xAxis.add(clock);
        clock++;
        for (int i = 0; i < switchCount; i++) {
            Switch networkSwitch = network.getSwitch(i);
            switchesInputPackets[i].add(networkSwitch.getInputPacketsCount());
            switchesQueuePackets[i].add(networkSwitch.getQueueSize());
            switchesDroppedPackets[i].add(networkSwitch.getDroppedPacketsCount());
        }
        EndToEndDelay endToEndDelay = new EndToEndDelay();
        for (int i = 0; i < serverCount; i++) {
            Server server = network.getServer(i);
            serverUtilization[i].add(server.getUtilization());
            serverInputPackets[i].add(server.getCyclePackets());
            VirtualMachine[] virtualMachines = server.getVirtualMachines();
            for (int j = 0; j < server.getVirtualMachinesCount(); j++) {
                VirtualMachine tempVirtualMachines = virtualMachines[j];
                if (tempVirtualMachines != null) {
                    vmUtilization[i][j].add(tempVirtualMachines.getUtilization());
                    endToEndDelay = endToEndDelay.merge(tempVirtualMachines.getEndToEndDelay());
                }
            }
        }
        endToEndDelays.add(endToEndDelay);
        activeNodeCount.add(network.getActiveNodeCount());
        network.resetDataCycle();
    }

    synchronized void plotSwitch(int i) {
        ArrayList<Integer> inputPacketData = switchesInputPackets[i];
        ArrayList<Integer[]> droppedPacketDataClass = switchesDroppedPackets[i];
        ArrayList<Long> droppedPacketData = new ArrayList<>();
        for (Integer[] packetDataClass : droppedPacketDataClass) {
            long temp = 0;
            for (int k = 0; k < NodeType.getCount(); k++) {
                temp += packetDataClass[k];
            }
            droppedPacketData.add(temp);
        }
        ArrayList<Float> queuePacketData = switchesQueuePackets[i];
        // Create Chart
        XYChart inputPacketChart = QuickChart.getChart("switch input packet in " + hour + "H", "Time", "input packet count",
                "switch id = " + i, xAxis, inputPacketData);
        XYChart droppedPacketChart = QuickChart.getChart("switch dropped packet in " + hour + "H", "Time", "dropped packet count",
                "switch id = " + i, xAxis, droppedPacketData);
        XYChart queuePacketChart = QuickChart.getChart("switch queue packet in " + hour + "H", "Time", "average queue packet count",
                "switch id = " + i, xAxis, queuePacketData);

        try {
            // or save it in high-res
            BitmapEncoder.saveBitmapWithDPI(inputPacketChart, FIGURE_DIR + "switches/switch_" + i + "_hour_" + hour + "_input",
                    BitmapEncoder.BitmapFormat.PNG, 500);
            BitmapEncoder.saveBitmapWithDPI(queuePacketChart, FIGURE_DIR + "switches/switch_" + i + "_hour_" + hour + "_queue",
                    BitmapEncoder.BitmapFormat.PNG, 500);
            BitmapEncoder.saveBitmapWithDPI(droppedPacketChart, FIGURE_DIR + "switches/switch_" + i + "_hour_" + hour + "_dropped",
                    BitmapEncoder.BitmapFormat.PNG, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized void plotServer(int i) {
        {
            ArrayList<Float> serverUtilizationData = serverUtilization[i];

            XYChart serverUtilizationChart = QuickChart.getChart("Server utilization in " + hour + "H", "Time", "Server utilization",
                    "server id = " + i, xAxis, serverUtilizationData);

            try {
                BitmapEncoder.saveBitmapWithDPI(serverUtilizationChart, FIGURE_DIR + "servers/server_utilization_" + i + "_hour_" + hour,
                        BitmapEncoder.BitmapFormat.PNG, 500);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        {
            ArrayList<Long> serverInputPacket = serverInputPackets[i];

            XYChart serverInputPacketsChart = QuickChart.getChart("Server input packets count in " + hour + "H", "Time", "Server utilization",
                    "server id = " + i, xAxis, serverInputPacket);

            try {
                BitmapEncoder.saveBitmapWithDPI(serverInputPacketsChart, FIGURE_DIR + "servers/server_packets_" + i + "_hour_" + hour,
                        BitmapEncoder.BitmapFormat.PNG, 500);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (int j = 0; j < singleServerVmCount[i]; j++) {
            ArrayList<Float> vm = vmUtilization[i][j];
            XYChart vmUtilizationChart = QuickChart.getChart("VM utilization in " + hour + "H", "Time", "VM utilization",
                    "server id = " + i + ", vm_id = " + j, xAxis, vm);

            try {
                BitmapEncoder.saveBitmapWithDPI(vmUtilizationChart,
                        FIGURE_DIR + "vms/server_" + i + "_vm_" + j + "_hour_" + hour,
                        BitmapEncoder.BitmapFormat.PNG, 500);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveCSV(String suffix) {
        {
            String path = "src/main/resources/results/network_" + suffix + ".csv";
            ArrayList<String> headers = new ArrayList<>();
            headers.add("Time");
            headers.add("ActiveNodeCount");

            ArrayList<Float> delay = new ArrayList<>();
            headers.add("End-to-End Delay");
            for (EndToEndDelay endToEndDelay : endToEndDelays
            ) {
                delay.add(endToEndDelay.getAverage());
            }

            ArrayList<ArrayList<Float>> delayTyped = new ArrayList<>();
            for (int i = 0; i < NodeType.getCount(); i++) {
                headers.add("End-to-End Delay " + NodeType.getInstance(i));
                ArrayList<Float> tempDelay = new ArrayList<>();
                for (EndToEndDelay endToEndDelay : endToEndDelays
                ) {
                    tempDelay.add(endToEndDelay.getAverage(i));
                }
                delayTyped.add(tempDelay);
            }

            ArrayList<Long> droppedPackets = new ArrayList<>();
            headers.add("dropped packets");
            for (int i = 0; i < switchesDroppedPackets[0].size(); i++) {
                long temp = 0;
                for (int j = 0; j < switchCount; j++) {
                    for (int k = 0; k < NodeType.getCount(); k++) {
                        temp += switchesDroppedPackets[j].get(i)[k];
                    }
                }
                droppedPackets.add(temp);
            }

            ArrayList<Float> queueSizePackets = new ArrayList<>();
            headers.add("queue size packets");
            for (int i = 0; i < switchesQueuePackets[0].size(); i++) {
                float temp = 0;
                for (int j = 0; j < switchCount; j++) {
                    temp += switchesQueuePackets[j].get(i);
                }
                queueSizePackets.add(temp / switchCount);
            }

            ArrayList<Float> totalServerUtilization = new ArrayList<>();
            headers.add("total server utilization");
            for (int i = 0; i < serverUtilization[0].size(); i++) {
                float temp = 0;
                for (int j = 0; j < serverCount; j++) {
                    temp += serverUtilization[j].get(i);
                }
                totalServerUtilization.add(temp / serverCount);
            }

            ArrayList<Long> totalPackets = new ArrayList<>();
            headers.add("total packets");
            for (int i = 0; i < serverInputPackets[0].size(); i++) {
                long temp = 0;
                for (int j = 0; j < serverCount; j++) {
                    temp += serverInputPackets[j].get(i);
                }
                totalPackets.add(temp);
            }

            try (
                    Writer writer = Files.newBufferedWriter(Paths.get(path));
                    CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers.toArray(new String[0])))
            ) {
                for (int i = 0; i < xAxis.size(); i++) {
                    csvPrinter.printRecord(xAxis.get(i), activeNodeCount.get(i), delay.get(i), delayTyped.get(0).get(i),
                            delayTyped.get(1).get(i), delayTyped.get(2).get(i), delayTyped.get(3).get(i),
                            droppedPackets.get(i), queueSizePackets.get(i), totalServerUtilization.get(i),
                            totalPackets.get(i));
                }
                csvPrinter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        {
            String path = "src/main/resources/results/switches_" + suffix + ".csv";
            ArrayList<String> headers = new ArrayList<>();
            headers.add("Time");
            for (int i = 0; i < switchCount; i++) {
                headers.add("switch_" + i + "_input_packets");
                headers.add("switch_" + i + "_queue_packets");
                headers.add("switch_" + i + "_dropped_packets");
            }

            long[][] dropped = new long[switchCount][switchesInputPackets[0].size()];
            for (int i = 0; i < switchCount; i++) {
                ArrayList<Integer[]> switchesDroppedPacket = switchesDroppedPackets[i];
                for (int j = 0; j < switchesDroppedPacket.size(); j++) {
                    Integer[] tempArray = switchesDroppedPacket.get(j);
                    long droppedCount = 0;
                    for (Integer integer : tempArray) {
                        droppedCount += integer;
                    }
                    dropped[i][j] = droppedCount;
                }
            }

            try (
                    Writer writer = Files.newBufferedWriter(Paths.get(path));
                    CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers.toArray(new String[0])))
            ) {
                for (int i = 0; i < xAxis.size(); i++) {
                    ArrayList<Object> objects = new ArrayList<>();
                    objects.add(xAxis.get(i));
                    for (int j = 0; j < switchCount; j++) {
                        objects.add(switchesInputPackets[j].get(i));
                        objects.add(switchesQueuePackets[j].get(i));
                        objects.add(dropped[j][i]);
                    }
                    csvPrinter.printRecord(objects.toArray(new Object[0]));
                }
                csvPrinter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        {
            String path = "src/main/resources/results/servers_" + suffix + ".csv";
            ArrayList<String> headers = new ArrayList<>();
            headers.add("Time");
            for (int i = 0; i < serverCount; i++) {
                headers.add("servers_" + i + "_input_packets");
                headers.add("servers_" + i + "_utilization");
                for (int j = 0; j < vmUtilization[i].length; j++) {
                    if (vmUtilization[i][j] != null) {
                        headers.add("servers_" + i + "_vm_" + j);
                    }
                }
            }

            try (
                    Writer writer = Files.newBufferedWriter(Paths.get(path));
                    CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers.toArray(new String[0])))
            ) {
                for (int i = 0; i < xAxis.size(); i++) {
                    ArrayList<Object> objects = new ArrayList<>();
                    objects.add(xAxis.get(i));
                    for (int j = 0; j < serverCount; j++) {
                        objects.add(serverInputPackets[j].get(i));
                        objects.add(serverUtilization[j].get(i));
                        for (int k = 0; k < vmUtilization[j].length; k++) {
                            if (vmUtilization[j][k] != null) {
                                if (vmUtilization[j][k].size() > i) {
                                    objects.add(vmUtilization[j][k].get(i));
                                }
                            }
                        }
                    }
                    csvPrinter.printRecord(objects.toArray(new Object[0]));
                }
                csvPrinter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void plot() {
        IntStream.range(0, switchCount).parallel().forEach(this::plotSwitch);
        IntStream.range(0, serverCount).parallel().forEach(this::plotServer);
        {
            XYChart queuePacketChart = QuickChart.getChart("Active Node Count in " + hour + "H", "Time", "Node Count",
                    "all node type", xAxis, activeNodeCount);
            try {
                BitmapEncoder.saveBitmapWithDPI(queuePacketChart, FIGURE_DIR + "nodes/activeNodeCount_hour_" + hour,
                        BitmapEncoder.BitmapFormat.PNG, 500);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        {
            ArrayList<Float> delay = new ArrayList<>();
            for (EndToEndDelay endToEndDelay : endToEndDelays
            ) {
                delay.add(endToEndDelay.getAverage());
            }
            XYChart endToEndDelaysChart = QuickChart.getChart("End-to-End Delay in " + hour + "H", "Time", "End-To-End Delay",
                    "all node type", xAxis, delay);
            try {
                BitmapEncoder.saveBitmapWithDPI(endToEndDelaysChart, FIGURE_DIR + "nodes/endToEndDelay_hour_" + hour,
                        BitmapEncoder.BitmapFormat.PNG, 500);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        {
            for (int i = 0; i < NodeType.getCount(); i++) {
                ArrayList<Float> delay = new ArrayList<>();
                for (EndToEndDelay endToEndDelay : endToEndDelays
                ) {
                    delay.add(endToEndDelay.getAverage(i));
                }
                XYChart endToEndDelaysChart = QuickChart.getChart("End-to-End Delay in " + hour + "H", "Time", "End-To-End Delay",
                        "all node type= " + i, xAxis, delay);
                try {
                    BitmapEncoder.saveBitmapWithDPI(endToEndDelaysChart, FIGURE_DIR + "nodes/endToEndDelay_type_" + i + "_hour_" + hour,
                            BitmapEncoder.BitmapFormat.PNG, 500);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        {
            ArrayList<Long> droppedPackets = new ArrayList<>();
            for (int i = 0; i < switchesDroppedPackets[0].size(); i++) {
                long temp = 0;
                for (int j = 0; j < switchCount; j++) {
                    for (int k = 0; k < NodeType.getCount(); k++) {
                        temp += switchesDroppedPackets[j].get(i)[k];
                    }
                }
                droppedPackets.add(temp);
            }
            XYChart endToEndDelaysChart = QuickChart.getChart("total dropped packets in " + hour + "H", "Time", "Count",
                    "all switches", xAxis, droppedPackets);
            try {
                BitmapEncoder.saveBitmapWithDPI(endToEndDelaysChart, FIGURE_DIR + "total/dropped_packets_hour_" + hour,
                        BitmapEncoder.BitmapFormat.PNG, 500);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        {
            for (int k = 0; k < NodeType.getCount(); k++) {
                ArrayList<Long> droppedPackets = new ArrayList<>();
                for (int i = 0; i < switchesDroppedPackets[0].size(); i++) {
                    long temp = 0;
                    for (int j = 0; j < switchCount; j++) {
                        temp += switchesDroppedPackets[j].get(i)[k];
                    }
                    droppedPackets.add(temp);
                }
                XYChart endToEndDelaysChart = QuickChart.getChart("total dropped packets in type" + k + "_" + hour + "H", "Time", "Count",
                        "all switches type " + k, xAxis, droppedPackets);
                try {
                    BitmapEncoder.saveBitmapWithDPI(endToEndDelaysChart, FIGURE_DIR + "total/dropped_packets_type_" + k + "_hour_" + hour,
                            BitmapEncoder.BitmapFormat.PNG, 500);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        {
            ArrayList<Float> queueSizePackets = new ArrayList<>();
            for (int i = 0; i < switchesQueuePackets[0].size(); i++) {
                float temp = 0;
                for (int j = 0; j < switchCount; j++) {
                    temp += switchesQueuePackets[j].get(i);
                }
                queueSizePackets.add(temp / switchCount);
            }
            XYChart endToEndDelaysChart = QuickChart.getChart("total queue size in " + hour + "H", "Time", "Count",
                    "all switches", xAxis, queueSizePackets);
            try {
                BitmapEncoder.saveBitmapWithDPI(endToEndDelaysChart, FIGURE_DIR + "total/queue_size_hour_" + hour,
                        BitmapEncoder.BitmapFormat.PNG, 500);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        {
            ArrayList<Float> totalServerUtilization = new ArrayList<>();
            for (int i = 0; i < serverUtilization[0].size(); i++) {
                float temp = 0;
                for (int j = 0; j < serverCount; j++) {
                    temp += serverUtilization[j].get(i);
                }
                totalServerUtilization.add(temp / serverCount);
            }
            XYChart serversUtilizationChart = QuickChart.getChart("server utilization in " + hour + "H", "Time", "utilization",
                    "all servers", xAxis, totalServerUtilization);
            try {
                BitmapEncoder.saveBitmapWithDPI(serversUtilizationChart, FIGURE_DIR + "total/server_utilization_hour_" + hour,
                        BitmapEncoder.BitmapFormat.PNG, 500);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        {
            ArrayList<Long> totalPackets = new ArrayList<>();
            for (int i = 0; i < serverInputPackets[0].size(); i++) {
                long temp = 0;
                for (int j = 0; j < serverCount; j++) {
                    temp += serverInputPackets[j].get(i);
                }
                for (int j = 0; j < switchCount; j++) {
                    temp += switchesInputPackets[j].get(i);
                }
                totalPackets.add(temp);
            }
            XYChart serversUtilizationChart = QuickChart.getChart("total packets in " + hour + "H", "Time", "packet count",
                    "all servers and switches", xAxis, totalPackets);
            try {
                BitmapEncoder.saveBitmapWithDPI(serversUtilizationChart, FIGURE_DIR + "total/packet_counts_hour_" + hour,
                        BitmapEncoder.BitmapFormat.PNG, 500);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        {
            ArrayList<Long> totalPackets = new ArrayList<>();
            for (int i = 0; i < serverInputPackets[0].size(); i++) {
                long temp = 0;
                for (int j = 0; j < serverCount; j++) {
                    temp += serverInputPackets[j].get(i);
                }
                totalPackets.add(temp);
            }
            XYChart serversUtilizationChart = QuickChart.getChart("total packets in " + hour + "H", "Time", "packet count",
                    "all servers", xAxis, totalPackets);
            try {
                BitmapEncoder.saveBitmapWithDPI(serversUtilizationChart, FIGURE_DIR + "total/output_packet_counts_hour_" + hour,
                        BitmapEncoder.BitmapFormat.PNG, 500);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
