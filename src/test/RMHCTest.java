package test;

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
  static int budget = 20;

  public static void main(String[] args) {

    Random rdm = new Random();

    int[] params = new int[searchSpace.nDims()];
    int ai1 = -1;
    int ai2 = -1;
    int resample = 1;
    int startPoint = -1;
    if(args.length==4) {
      ai1 = Integer.parseInt(args[0]);
      ai2 = Integer.parseInt(args[1]);
      resample = Integer.parseInt(args[2]);
      startPoint = Integer.parseInt(args[3]);
      params = GameDesign.initParams[startPoint];
    } else {
      for (int i=0; i<params.length; i++) {
        params[i] = searchSpace.getRandomValue(i);
      }
    }
    double[] res = GameDesign.playNWithParams(ai1, ai2, params, resample);
    double bestSoFar = res[0];
    double bestSoFarFit = GameDesign.fitness(bestSoFar);
    double bestSoFarPoints = res[1];

    double newWinRate;
    double newFitness;
    double newPoints;

    int buffer = 1;
    String str = "";
    int t = 0;
    int T = (int) Math.floor(budget/2/resample);

    str = resample + " " + t + " " + bestSoFarFit + " " + bestSoFarPoints;
    for (int i=0; i<params.length; i++) {
      str += " " + params[i];
    }
    System.out.println(str);

    while (t < T) {
      /** reproduction and mutation */
      // TODO: 14/05/17 Here to edit to implement a (1+1)-EA 
      int[] mutatedParams = params;
      int mutatedIdx = rdm.nextInt(params.length);
      int mutatedValue = searchSpace.getRandomValue(mutatedIdx);
      mutatedParams[mutatedIdx] = mutatedValue;
      /** evaluate offspring */
      res = GameDesign.playNWithParams(ai1, ai2, mutatedParams, resample);
      newWinRate = res[0];
      newPoints = res[1];
      newFitness = GameDesign.fitness(newWinRate);

      /** evaluate parent */
      res = GameDesign.playNWithParams(ai1, ai2, params, resample);
      bestSoFarPoints = (res[1] + bestSoFarPoints*buffer) / (buffer+1);
      bestSoFar = (res[0] + bestSoFar*buffer) / (buffer+1);
      bestSoFarFit = GameDesign.fitness(bestSoFar);
      /** compare and select */ 
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

      str = resample + " " + t + " " + bestSoFarFit + " " + bestSoFarPoints;
      for (int i=0; i<params.length; i++) {
        str += " " + params[i];
      }
      System.out.println(str);

    }
  }
}
