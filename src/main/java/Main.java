import FileGenerator.NetworkConnectionCSVGenerator;
import FileGenerator.NodeStateCSVGeneratorGaussian;
import Models.Network;
import Models.controllers.ShortestPathController_hop;
import Models.controllers.ShortestPathController_queue;
import Models.controllers.SimpleShortestPathController;

public class Main {

    public static void main(String[] args) {

        System.out.println("generating network structure files");
        NodeStateCSVGeneratorGaussian nodeStateCSVGenerator = new NodeStateCSVGeneratorGaussian();
        nodeStateCSVGenerator.create();
        NetworkConnectionCSVGenerator networkConnectionCSVGenerator = new NetworkConnectionCSVGenerator();
        networkConnectionCSVGenerator.create();
        System.out.println("starting the simulation");
        Network test = new Network();
//        test.simulate(new SimpleFileBasedController());
//        test.simulate(new SimpleShortestPathController());
//        test.simulate(new ShortestPathController_hop());
        test.simulate(new ShortestPathController_queue());

    }

}
