package Test;

import constants.Constants;
import constants.Gaussian;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.io.IOException;
import java.util.ArrayList;

import static constants.Constants.*;

public class MainChartTest {

    public static void main(String[] args) {

        ArrayList<Integer> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        Gaussian small = new Gaussian(12, 110);
        Gaussian big = new Gaussian(9, 270);
        int minActiveCount = MINIMUM_ACTIVE_NODE_COUNT;
        int smallPeak = Constants.MAX_NODE_COUNT / 2 - minActiveCount;
        int bigPeak = Constants.MAX_NODE_COUNT - minActiveCount;
        for (int i = 0; i < 360; i++) {
            x.add(i);
            y.add(small.getY(i) * smallPeak + big.getY(i) * bigPeak + minActiveCount);
//            y.add(small.getY(i) * smallPeak);
        }

        // Create Chart
        XYChart chart = QuickChart.getChart("active node count", "time", "count", "count(time)", x, y);

        try {
            // or save it in high-res
            BitmapEncoder.saveBitmapWithDPI(chart, TEST_DIR + "active_node_count",
                    BitmapEncoder.BitmapFormat.PNG, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
