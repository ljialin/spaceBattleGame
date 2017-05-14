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
// TODO: 14/05/17 Exercise Lab RHEA
// (1) Read the code to solve the OneMax
// (2) Change the mutation operator to implement (1+1)-EA
// (3) Solve the OneMax using (1+1)-EA
// (4) Solve the noisy OneMax with noiseStd=1, using either algorithm
public class TestOneMax {
    public static OneMax oneMax;

    /**
     * Initialise a d-dimensional binary string
     * @param d
     * @return
     */
    public static int[] init(int d) {
        /** Initialisation of all-0 string */
        int[] x = new int[d];
        // TODO: 14/05/17 Uncomment the following lines to use random initialisation
//        /** Initialisation of random string **/
//        Random rdm = new Random();
//        for (int i=1;i<d;i++) {
//            if (rdm.nextDouble()>0.5) {
//                x[i] = 1;
//            }
//        }
        return x;
    }

    /**
     * RMHC solver
     * @param d
     * @param budget
     * @return
     */
    // TODO: 14/05/17 Read the code to solve the OneMax
    public static int[] solveRMHC(int d, int budget) {
        int[] x = init(d);
        Random rdm = new Random();
        int t = 0;  // counter
        System.out.println("Optimisation starts with true fitness=" + oneMax.trueFitness(x)
            + ", budget=" + budget + " evaluations.");
        while (t<budget) {
            // reproduction and mutation
            int[] xp = x;
            int mut = rdm.nextInt(d);
            if (rdm.nextDouble()>0.5) {
                xp[mut] = 1 - xp[mut];
            }
            // evaluation
            double fitnessParent = oneMax.fitness(x);
            double fitnessChild = oneMax.fitness(xp);
            // comparison and selection
            if (fitnessChild >= fitnessParent) {
                x = xp;
            }
            t++;
        }
        return x;
    }

    // TODO: 14/05/17 Code the (1+1)-EA solver by changing the mutation operator of RMHC
    public static int[] solveOPOEA(int d, int budget) {
        int[] x = init(d);
        System.out.println("Optimisation starts with true fitness=" + oneMax.trueFitness(x)
            + ", budget=" + budget + " evaluations.");
        return x;
    }

    public static void main(String[] args) {
        int d = 10;             // dimension on OneMax problem, genome length
        int budget = 2000;      // optimisation budget, fitness evaluations
        /** Noise-free case */
        double noiseStd = 0;
        // TODO: 14/05/17  Uncomment the following lines to test with noisy OneMax
//        /** Noisy case with std = 1 */
//        double noiseStd = 1;
        oneMax = new OneMax(noiseStd);



        /** Solve the OneMax using RMHC */
        int[] solutionRMHC = solveRMHC(d, budget);
        System.out.println("The true fitness of optimised solution is "
            + oneMax.trueFitness(solutionRMHC));

        // TODO: 14/05/17 Solve the OneMax using (1+1)-EA
        int[] solutionOPOEA = solveOPOEA(d, budget);
        System.out.println("The true fitness of optimised solution is "
            + oneMax.trueFitness(solutionOPOEA));
    }
}
