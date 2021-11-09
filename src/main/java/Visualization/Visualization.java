package Visualization;

import Models.Network;
import Models.Server;
import Models.StatsModels.EndToEndDelay;
import Models.Switch;
import Models.VirtualMachine;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XYChart;

import java.io.IOException;
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
    private final ArrayList<Integer>[] switchesDroppedPackets = new ArrayList[MAX_SWITCH_COUNT];
    private final ArrayList<Float>[] switchesQueuePackets = new ArrayList[MAX_SWITCH_COUNT];
    private final ArrayList<Float>[] serverUtilization = new ArrayList[MAX_SERVER_COUNT];
    private final ArrayList<Long>[] serverInputPackets = new ArrayList[MAX_SERVER_COUNT];
    private final ArrayList<Float>[][] vmUtilization = new ArrayList[MAX_SERVER_COUNT][MAX_VM_IN_SINGLE_SERVER_COUNT];
    private final ArrayList<Long> activeNodeCount = new ArrayList<>();
    private final ArrayList<Float> endToEndDelays = new ArrayList<>();

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
            Switch s = network.getSwitch(i);
            switchesInputPackets[i].add(s.getInputPacketsCount());
            switchesQueuePackets[i].add(s.getQueuePacketsCount());
            switchesDroppedPackets[i].add(s.getDroppedPacketsCount());
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
        endToEndDelays.add(endToEndDelay.getAverage());
        activeNodeCount.add(network.getActiveNodeCount());
    }

    synchronized void plotSwitch(int i) {
        ArrayList<Integer> inputPacketData = switchesInputPackets[i];
        ArrayList<Integer> droppedPacketData = switchesDroppedPackets[i];
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
            XYChart endToEndDelaysChart = QuickChart.getChart("End-to-End Delay in " + hour + "H", "Time", "End-To-End Delay",
                    "all node type", xAxis, endToEndDelays);
            try {
                BitmapEncoder.saveBitmapWithDPI(endToEndDelaysChart, FIGURE_DIR + "nodes/endToEndDelay_hour_" + hour,
                        BitmapEncoder.BitmapFormat.PNG, 500);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        {
            ArrayList<Long> droppedPackets = new ArrayList<>();
            for (int i = 0; i < switchesDroppedPackets[0].size(); i++) {
                long temp = 0;
                for (int j = 0; j < switchCount; j++) {
                    temp += switchesDroppedPackets[j].get(i);
                }
                droppedPackets.add(temp);
            }
            XYChart endToEndDelaysChart = QuickChart.getChart("total dropped packets in " + hour + "H", "Time", "Count",
                    "all switches", xAxis, droppedPackets);
            try {
                BitmapEncoder.saveBitmapWithDPI(endToEndDelaysChart, FIGURE_DIR + "nodes/dropped_packets_hour_" + hour,
                        BitmapEncoder.BitmapFormat.PNG, 500);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
