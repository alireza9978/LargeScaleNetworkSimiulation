package Models.StatsModels;

import constants.NodeType;

public class EndToEndDelay {

    private final float[] totalDelay = new float[NodeType.getCount()];
    private final int[] packetCount = new int[NodeType.getCount()];

    public void addPacket(float delay, int type) {
        totalDelay[type] += delay;
        packetCount[type]++;
    }

    public EndToEndDelay merge(EndToEndDelay endToEndDelay) {
        EndToEndDelay temp = new EndToEndDelay();
        for (int i = 0; i < NodeType.getCount(); i++) {
            temp.totalDelay[i] += endToEndDelay.totalDelay[i];
            temp.totalDelay[i] += this.totalDelay[i];
            temp.packetCount[i] += endToEndDelay.packetCount[i];
            temp.packetCount[i] += this.packetCount[i];
        }
        return temp;
    }

    public float getAverage(int type) {
        return totalDelay[type] / (float) packetCount[type];
    }

    public float getAverage() {
        long sumDelay = 0;
        long sumCount = 0;
        for (int i = 0; i < NodeType.getCount(); i++) {
            sumDelay += totalDelay[i];
            sumCount += packetCount[i];
        }
        return (float) sumDelay / (float) sumCount;
    }
}
