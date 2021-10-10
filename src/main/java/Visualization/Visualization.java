package Visualization;

import Models.Network;
import Models.Server;
import Models.Switch;
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
    private final ArrayList<Integer>[] switchesInputPackets = new ArrayList[SWITCH_COUNT];
    private final ArrayList<Float>[] switchesQueuePackets = new ArrayList[SWITCH_COUNT];
    private final ArrayList<Float>[] serverUtilization = new ArrayList[SERVER_COUNT];

    public Visualization(int hour) {
        this.hour = hour;
        for (int i = 0; i < SERVER_COUNT; i++) {
            serverUtilization[i] = new ArrayList<>();
        }
        for (int i = 0; i < SWITCH_COUNT; i++) {
            switchesInputPackets[i] = new ArrayList<>();
            switchesQueuePackets[i] = new ArrayList<>();
        }
    }

    public void getData(Network network) {
        xAxis.add(clock);
        clock++;
        for (int i = 0; i < SWITCH_COUNT; i++) {
            Switch s = network.getSwitch(i);
            switchesInputPackets[i].add(s.getInputPacketsCount());
            switchesQueuePackets[i].add(s.getQueuePacketsCount());
        }
        for (int i = 0; i < SERVER_COUNT; i++) {
            Server s = network.getServer(i);
            serverUtilization[i].add(s.getUtilization());
        }
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
    }

}
