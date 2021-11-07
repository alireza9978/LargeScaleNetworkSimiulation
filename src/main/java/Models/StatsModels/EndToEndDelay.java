package Models.StatsModels;

public class EndToEndDelay {

    private float totalDelay = 0;
    private int packetCount = 0;

    public void addPacket(float delay) {
        totalDelay += delay;
        packetCount++;
    }

    public EndToEndDelay merge(EndToEndDelay endToEndDelay) {
        EndToEndDelay temp = new EndToEndDelay();
        temp.totalDelay += endToEndDelay.totalDelay;
        temp.totalDelay += this.totalDelay;
        temp.packetCount += endToEndDelay.packetCount;
        temp.packetCount += this.packetCount;
        return temp;
    }

    public float getAverage() {
        return totalDelay / (float) packetCount;
    }
}
