package labSimpleRHEA;

import java.util.Random;

/**
 * Created by Jialin Liu on 14/05/17.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class OneMax {
    private double noiseStd;
    public OneMax(double noiseStd) {
        this.noiseStd = noiseStd;
    }

    public double trueFitness(int[] x) {
        double sum = 0;
        for (int i: x) {
            sum += i;
        }
        return sum;
    }

    public double fitness(int[] x) {
        double sum = trueFitness(x);
        if (noiseStd == 0) {
            return sum;
        }
        Random rdm = new Random();
        return (sum + noiseStd * rdm.nextGaussian());
    }
}
