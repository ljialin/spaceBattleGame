package test;

import tools.ElapsedCpuTimer;
import tools.Utils;

import java.util.Random;

/**
 * Created by Jialin Liu on 13/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class RMHCTest {
  static SpaceBattleGameSearchSpace searchSpace = new SpaceBattleGameSearchSpace();

  public static void main(String[] args) {

    Random rdm = new Random();

    int[] params = new int[searchSpace.nDims()];
    if(args.length>0) {
      params = GameDesign.initParams[Integer.parseInt(args[0])];
    } else {
      for (int i=0; i<params.length; i++) {
        params[i] = searchSpace.getValue(i);
//        params[i] = Utils.randomInRange(rdm,
//            GameDesign.bounds[i][0],
//            GameDesign.bounds[i][1], GameDesign.bounds[i][2]);
      }
    }
    double[] res = GameDesign.playNWithParams(params);
    double bestSoFar = res[0];
    double bestSoFarFit = GameDesign.fitness(bestSoFar);
    double bestSoFarPoints = res[1];

    double newWinRate;
    double newFitness;
    double newPoints;

    int buffer = 1;
    String str = "";
    int t = 0;
    while (t < GameDesign.nbIter) {
//      ElapsedCpuTimer timer = new ElapsedCpuTimer();

      int[] mutatedParams = params;
      int mutatedIdx = rdm.nextInt(params.length);
//      System.out.println(params.length + " " + mutatedIdx);
      int mutatedValue = searchSpace.getValue(mutatedIdx);
//      int mutatedValue = Utils.randomInRange(rdm, GameDesign.bounds[mutatedIdx][0],
//          GameDesign.bounds[mutatedIdx][1], GameDesign.bounds[mutatedIdx][2]);
      mutatedParams[mutatedIdx] = mutatedValue;
      /** evaluate offspring */
      res = GameDesign.playNWithParams(mutatedParams);
      newWinRate = res[0];
      newPoints = res[1];
      newFitness = GameDesign.fitness(newWinRate);

      /** evaluate parent */
      res = GameDesign.playNWithParams(params);
      bestSoFarPoints = (res[1] + bestSoFarPoints*buffer) / (buffer+1);
      bestSoFar = (res[0] + bestSoFar*buffer) / (buffer+1);
      bestSoFarFit = GameDesign.fitness(bestSoFar);
      if(newFitness >= bestSoFarFit) {
        params = mutatedParams;
        bestSoFar = newWinRate;
        bestSoFarFit = newFitness;
        bestSoFarPoints = newPoints;
        buffer = 1;
      } else {
        buffer++;
      }

      t++;

      str = "" + t + " " + bestSoFarFit + " " + bestSoFar + " " + bestSoFarPoints;
      for (int i=0; i<params.length; i++) {
        str += " " + params[i];
      }
      System.out.println(str);

    }
  }
}
