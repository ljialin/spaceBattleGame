package test;

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

    Random rdm = new Random();

    int[] params = new int[searchSpace.nDims()];
    int totalNb = 1;
    for (int i = 0; i < params.length; i++) {
      totalNb *= searchSpace.nValues(i);
    }
    for (int t = 0; t < totalNb; t++) {
      params = searchSpace.getParams(t);
      double[] res = GameDesign.playNWithParams(params);
      String str = "" + t + " " + res[0] + " " + res[1];
      for (int i = 0; i < params.length; i++) {
        str += " " + params[i];
      }
      System.out.println(str);
    }

  }
}