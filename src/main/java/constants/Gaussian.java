package constants;

public class Gaussian {

    protected double stdDeviation, variance, mean;

    public Gaussian(double stdDeviation, double mean) {

        this.stdDeviation = stdDeviation;
        variance = stdDeviation * stdDeviation;
        this.mean = mean;

    }

    public double getY(double x) {
        return Math.pow(Math.exp(-(((x - mean) * (x - mean)) / ((2 * variance)))), 1 / (stdDeviation * Math.sqrt(2 * Math.PI)));
    }

    public String getName() {
        return "Gaussian Curve";
    }


}