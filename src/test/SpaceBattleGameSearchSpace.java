package test;

import bandits.SearchSpace;

import java.util.Random;

/**
 * Created by Jialin Liu on 14/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class SpaceBattleGameSearchSpace implements SearchSpace {

  static int[][] values = {
      { 4, 6, 8, 10}, // SHIP_MAX_SPEED
      { 1, 2, 3, 4, 5},  // THRUST_SPEED
      { 1, 5, 10, 20, 50, 75, 100}, // MISSILE_COST
      { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, // MISSILE_MAX_SPEED
      { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9} // MISSILE_COOLDOWN
  };

  @Override
  public int nDims() {
    return 5;
  }

  @Override
  public int nValues(int idxDim) {
    int dim = values[idxDim].length;
//    int dim = (GameDesign.bounds[idxDim][1] - GameDesign.bounds[idxDim][0])
//        / GameDesign.bounds[idxDim][2]
//        + 1;
    return dim;
  }

  public int getValue(int idxDim, int idx) {
    return values[idxDim][idx];
//    return GameDesign.bounds[idxDim][0] + idx * GameDesign.bounds[idxDim][2];
  }

  public int getValue(int idxDim) {
    int idx = new Random().nextInt(values[idxDim].length);
    return values[idxDim][idx];
  }


  public static int[] getParams(int idx) {
    int[] params = new int[5];
    double deno = 1;
    for (int i=0; i<params.length; i++) {
      deno *= values[i].length;
      int k = (int) Math.floor( (double) idx / deno);
      params[i] = values[i][k];
      idx -= k*deno;
    }
    return params;
  }
}
