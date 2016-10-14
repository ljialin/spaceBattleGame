package test;

import ontology.Constants;
import tools.ElapsedCpuTimer;

/**
 * Created by Jialin Liu on 12/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/stylBeguide/javaguide.html
 */
public class GameDesign {
  public static int nbIter = 100;
  public static int nbRuns = 100;
  public static int p1 = 5;  //
  public static int p2 = 2;  // random
  public static double target = 1.0;
  public static double maxDist = Math.max(target, 1-target);
  public static double range = Math.max( target*target, (1-target)*(1-target) );
  public static int[] indices = {2, 3, 4, 7, 8, 11, 1, 5, 6, 9, 10};

  static int[][] bounds = {
      {2, 10, 2},  // SHIP_MAX_SPEED
      {1, 5, 1},  // THRUST_SPEED
      {1, 5, 1},  // MISSILE_COST
      {1, 10, 1},  // MISSILE_MAX_SPEED
      {0, 5, 1},  // MISSILE_COOLDOWN
      {10, 50, 10},  // KILL_AWARD

      {10, 50},  // SHIP_RADIUS
      {1, 5},  // MISSILE_RADIUS
      {10, 50},  // MISSILE_MAX_TTL
      {50, 100},  // FRICTION
      {1, 60}  // RADIAN_UNIT
  };

  public static double[] playNWithParams(int[] params) {
//    ElapsedCpuTimer t = new ElapsedCpuTimer();

    Constants.SHIP_MAX_SPEED = params[0];
    Constants.THRUST_SPEED = params[1];
    Constants.MISSILE_COST = params[2];
    Constants.MISSILE_MAX_SPEED = params[3];
    Constants.MISSILE_COOLDOWN = params[4];
    Constants.KILL_AWARD = params[5];

    double[] res = GameTest.playNAndMean( nbRuns, p1, p2);

//    System.out.println(t);

    return res;
  }


  public static double fitness(double var) {
//    double sqDist = (var-target)*(var-target);
//    return (1 - sqDist/range);
//    double dist = Math.abs(var - target);
//    return (1 - dist/maxDist);
    return var;
  }
}