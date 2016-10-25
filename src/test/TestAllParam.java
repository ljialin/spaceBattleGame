package test;

import ontology.Constants;
import tools.Utils;

import java.util.Random;

/**
 * Created by Jialin Liu on 24/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class TestAllParam {
  static SpaceBattleGameSearchSpace searchSpace = new SpaceBattleGameSearchSpace();

  public static void main(String[] args) {

    int[] params = new int[searchSpace.nDims()];
    int totalNb = 1;
    for (int i = 0; i < params.length; i++) {
      totalNb *= searchSpace.nValues(i);
//      System.out.println(searchSpace.values[i].length);
    }

    for (int run=0; run<1000; run++) {
      Random rdm = new Random();


      for (int t = 0; t < totalNb; t++) {
        params = searchSpace.getParams(t);

        Constants.SHIP_MAX_SPEED = params[0];
        Constants.THRUST_SPEED = params[1];
        Constants.MISSILE_COST = params[2];
        Constants.MISSILE_MAX_SPEED = params[3];
        Constants.MISSILE_COOLDOWN = params[4];

        GameTest.playOne(GameDesign.p1, GameDesign.p2, false, run);
      }
    }

  }
}