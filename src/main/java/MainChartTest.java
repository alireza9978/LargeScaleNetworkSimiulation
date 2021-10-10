import constants.Constants;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.io.IOException;

import static constants.Constants.FIGURE_DIR;

public class MainChartTest {

    public static void main(String[] args) {

        double[] xData = new double[]{0.0, 1.0, 2.0};
        double[] yData = new double[]{2.0, 1.0, 0.0};

        // Create Chart
        XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);

        try {
            // Save it
            BitmapEncoder.saveBitmap(chart, FIGURE_DIR + "Sample_Chart", BitmapEncoder.BitmapFormat.PNG);

            // or save it in high-res
            BitmapEncoder.saveBitmapWithDPI(chart, FIGURE_DIR + "Sample_Chart_300_DPI",
                    BitmapEncoder.BitmapFormat.PNG, 300);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
