package Models.StatsModels;

public class EndToEndDelay {

    private long totalDelay = 0;
    private int packetCount = 0;

    public void addPacket(long delay){
        totalDelay += delay;
        packetCount++;
    }

    public EndToEndDelay merge(EndToEndDelay endToEndDelay){
        EndToEndDelay temp = new EndToEndDelay();
        temp.totalDelay += endToEndDelay.totalDelay;
        temp.totalDelay += this.totalDelay;
        temp.packetCount += endToEndDelay.packetCount;
        temp.packetCount += this.packetCount;
        return temp;
    }

    public float getAverage(){
        return (float) totalDelay / (float) packetCount;
    }
}
