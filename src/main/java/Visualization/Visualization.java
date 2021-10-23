package Visualization;

import Models.Network;
import Models.Server;
import Models.Switch;
import constants.NodeType;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XYChart;

import java.io.IOException;
import java.util.ArrayList;

import static constants.Constants.*;

public class Visualization {

    private final ArrayList<Integer> xAxis = new ArrayList<>();
    private int clock = 0;
    private final int hour;
    private final int serverCount;
    private final int switchCount;
    private final ArrayList<Integer>[] switchesInputPackets = new ArrayList[MAX_SWITCH_COUNT];
    private final ArrayList<Float>[] switchesQueuePackets = new ArrayList[MAX_SWITCH_COUNT];
    private final ArrayList<Float>[] serverUtilization = new ArrayList[MAX_SERVER_COUNT];
    private final ArrayList<Long> activeNodeCount = new ArrayList<>();

    public Visualization(int hour, int serverCount, int switchCount) {
        this.hour = hour;
        this.serverCount = serverCount;
        this.switchCount = switchCount;
        for (int i = 0; i < serverCount; i++) {
            serverUtilization[i] = new ArrayList<>();
        }
        for (int i = 0; i < switchCount; i++) {
            switchesInputPackets[i] = new ArrayList<>();
            switchesQueuePackets[i] = new ArrayList<>();
        }
    }

    public void getData(Network network) {
        xAxis.add(clock);
        clock++;
        for (int i = 0; i < switchCount; i++) {
            Switch s = network.getSwitch(i);
            switchesInputPackets[i].add(s.getInputPacketsCount());
            switchesQueuePackets[i].add(s.getQueuePacketsCount());
        }
        for (int i = 0; i < serverCount; i++) {
            Server s = network.getServer(i);
            serverUtilization[i].add(s.getUtilization());
        }
        long total = 0;
        for (int i = 0; i < NodeType.getCount(); i++) {
            total += network.getActiveNodeCount(i);
        }
        activeNodeCount.add(total);
    }

    public void plot() {
        for (int i = 0, switchesDataLength = switchesInputPackets.length; i < switchesDataLength; i++) {
            ArrayList<Integer> inputPacketData = switchesInputPackets[i];
            ArrayList<Float> queuePacketData = switchesQueuePackets[i];
            // Create Chart
            XYChart inputPacketChart = QuickChart.getChart("switch input packet in " + hour + "H", "Time", "input packet count",
                    "switch id = " + i, xAxis, inputPacketData);
            XYChart queuePacketChart = QuickChart.getChart("switch queue packet in " + hour + "H", "Time", "average queue packet count",
                    "switch id = " + i, xAxis, queuePacketData);


            try {
                // or save it in high-res
                BitmapEncoder.saveBitmapWithDPI(inputPacketChart, FIGURE_DIR + "switches/switch_" + i + "_hour_" + hour + "_input",
                        BitmapEncoder.BitmapFormat.PNG, 300);
                BitmapEncoder.saveBitmapWithDPI(queuePacketChart, FIGURE_DIR + "switches/switch_" + i + "_hour_" + hour + "_queue",
                        BitmapEncoder.BitmapFormat.PNG, 300);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0, serversDataLength = serverUtilization.length; i < serversDataLength; i++) {
            ArrayList<Float> serverUtilizationData = serverUtilization[i];

            XYChart queuePacketChart = QuickChart.getChart("Server utilization in " + hour + "H", "Time", "Server utilization",
                    "server id = " + i, xAxis, serverUtilizationData);

            try {
                BitmapEncoder.saveBitmapWithDPI(queuePacketChart, FIGURE_DIR + "servers/server_" + i + "_hour_" + hour,
                        BitmapEncoder.BitmapFormat.PNG, 300);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        XYChart queuePacketChart = QuickChart.getChart("Active Node Count in " + hour + "H", "Time", "Node Count",
                "all node type", xAxis, activeNodeCount);

        try {
            BitmapEncoder.saveBitmapWithDPI(queuePacketChart, FIGURE_DIR + "nodes/activeNodeCount_hour_" + hour,
                    BitmapEncoder.BitmapFormat.PNG, 300);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
