import Models.Network;
import constants.Constants;

public class Main {

    public static void main(String[] args) {

        System.out.println("start");
        Network test = new Network(Constants.SERVER_COUNT, Constants.SWITCH_COUNT, Constants.NODE_COUNT);
        test.simulate();

    }

}
