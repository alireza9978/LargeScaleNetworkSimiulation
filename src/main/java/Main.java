import FileGenerator.NodeStateCSVGenerator;
import FileGenerator.NetworkConnectionCSVGenerator;
import Models.Network;
import Models.controllers.ShortestPathController;
import Models.controllers.SimpleFileBasedController;

public class Main {

    public static void main(String[] args) {

        System.out.println("generating network structure files");
        NodeStateCSVGenerator nodeStateCSVGenerator = new NodeStateCSVGenerator();
        nodeStateCSVGenerator.create();
        NetworkConnectionCSVGenerator networkConnectionCSVGenerator = new NetworkConnectionCSVGenerator();
        networkConnectionCSVGenerator.create();
        System.out.println("starting the simulation");
        Network test = new Network();
//        test.simulate(new SimpleFileBasedController());
//        test.simulate(new ShortestPathController());

    }

}
