package gamelog;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created by Simon Lucas on 13/05/2017.
 * <p>
 * The purpose of this class is simple: it is to log various aspects of the game
 */
public class GameLogger {
    // keep them all just in case
    // but also keep a TreeList of actions

    public static void main(String[] args) {
        Random random = new Random();
        int nSamples = 100;
        int nValues = 10;
        GameLogger logger = new GameLogger();
        for (int i = 0; i < nSamples; i++) {
            logger.addAction(random.nextInt(nValues));
        }
        System.out.println(logger.frequencyReport());
        double entropy = entropy(logger.pVec());

        System.out.format("Entropy:   \t %.5f Shannons\n", entropy);
        System.out.format("Normalised:\t %.5f \n", entropy / log2(nValues));
    }

    public double[] pVec() {
        double[] p = new double[countMap.size()];
        int ix = 0;
        double tot = 0;
        for (Comparable key : countMap.keySet()) {
            p[ix] = countMap.get(key).count;
            tot += p[ix];
            ix++;
        }
        // now normalise
        for (int i = 0; i < p.length; i++)
            p[i] /= tot;
        return p;
    }

    public static double entropy(double[] p) {
        //assumes that p is a valid probability vector
        // where all entries sum to 1 and are non-negative
        double tot = 0;
        for (int i = 0; i < p.length; i++) {
            // only consider non-zero entries
            // to avoid NaN (Not a Number) errors
            if (p[i] != 0) {
                tot += p[i] * log2(p[i]);
            }
        }
        // return the negative of the total
        return -tot;
    }

    public static double log2(double x) {
        return Math.log(x) / Math.log(2);
    }



    ArrayList<Integer> actions;

    // use a TreeMap in order to naturally get a well ordered printout
    TreeMap<Comparable, Counter> countMap;


    public GameLogger() {
        actions = new ArrayList<>();
        countMap = new TreeMap<>();
    }

    public String frequencyReport() {
        StringBuffer sb = new StringBuffer();
        sb.append("Frequency Report\n");
        for (Comparable key : countMap.keySet()) {
            sb.append(String.format("%s \t %5d\n", key, countMap.get(key).count));
        }
        return sb.toString();
    }


    public void addAction(int action) {
        actions.add(action);

        // also add to the TreeMap

        Counter counter = countMap.get(action);
        if (counter == null) {
            counter = new Counter();
            countMap.put(action, counter);
        } else {
            counter.inc();
        }
    }

    static class Counter {
        int count = 1;

        void inc() {
            count++;
        }

        public String toString() {
            return "" + count;
        }
    }
}
