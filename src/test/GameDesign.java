package test;

import ontology.Constants;

/**
 * Created by Jialin Liu on 12/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/stylBeguide/javaguide.html
 */
public class GameDesign {
  public static int nbIter = 1000;
  public static int nbRuns = 100;
  public static int p1 = 4;  //
  public static int p2 = 2;  // random
  public static double target = 1.0;
  public static double maxDist = Math.max(target, 1-target);
  public static double range = Math.max( target*target, (1-target)*(1-target) );
  public static int[] indices = {2, 3, 4, 7, 8, 11, 1, 5, 6, 9, 10};

  static int[][] bounds = {
      {4, 10, 2},  // SHIP_MAX_SPEED
      {1, 5, 1},  // THRUST_SPEED
      {5, 95, 10},  // MISSILE_COST
      {1, 10, 1},  // MISSILE_MAX_SPEED
      {0, 5, 1},  // MISSILE_COOLDOWN

      {10, 100, 10},  // KILL_AWARD

      {10, 50},  // SHIP_RADIUS
      {1, 5},  // MISSILE_RADIUS
      {10, 50},  // MISSILE_MAX_TTL
      {50, 100},  // FRICTION
      {1, 60}  // RADIAN_UNIT
  };

  static int[][] initParams = {
      {6, 4, 5, 9, 2, 80},
      {6, 5, 5, 9, 2, 50},
      {4, 4, 5, 10, 3, 60},
      {4, 5, 3, 3, 5, 80},
      {10, 2, 4, 7, 0, 50},
      {6, 4, 2, 9, 4, 40},
      {6, 4, 5, 7, 3, 40},
      {6, 4, 5, 8, 0, 70},
      {6, 3, 1, 9, 1, 30},
      {6, 2, 3, 6, 2, 40},
      {8, 4, 5, 8, 2, 90},
      {4, 1, 1, 2, 1, 40},
      {4, 3, 1, 2, 3, 90},
      {10, 3, 5, 6, 3, 40},
      {6, 3, 1, 9, 0, 50},
      {10, 2, 4, 9, 5, 100},
      {4, 2, 5, 6, 3, 70},
      {10, 3, 2, 5, 2, 100},
      {8, 4, 4, 4, 3, 60},
      {4, 3, 4, 5, 5, 80},
      {6, 3, 2, 8, 4, 50},
      {8, 5, 4, 8, 0, 40},
      {8, 3, 1, 3, 5, 10},
      {10, 1, 4, 6, 1, 60},
      {4, 4, 3, 1, 0, 20},
      {4, 3, 5, 7, 3, 90},
      {4, 5, 1, 6, 0, 60},
      {4, 4, 5, 10, 5, 60},
      {6, 1, 3, 6, 4, 10},
      {8, 3, 1, 10, 3, 50},
      {10, 4, 3, 9, 3, 60},
      {8, 2, 2, 6, 5, 50},
      {4, 3, 2, 5, 5, 50},
      {6, 2, 1, 3, 0, 50},
      {6, 2, 3, 2, 2, 80},
      {4, 4, 1, 4, 0, 30},
      {4, 1, 1, 3, 1, 20},
      {8, 5, 5, 3, 2, 40},
      {8, 2, 1, 5, 1, 10},
      {10, 3, 5, 8, 0, 70},
      {8, 4, 3, 3, 4, 30},
      {6, 5, 5, 5, 1, 70},
      {10, 5, 3, 4, 5, 50},
      {10, 1, 3, 8, 1, 40},
      {4, 2, 3, 6, 0, 60},
      {6, 2, 2, 2, 5, 100},
      {10, 4, 1, 4, 1, 10},
      {6, 4, 4, 6, 2, 30},
      {8, 4, 4, 6, 4, 70},
      {4, 3, 2, 1, 0, 20},
      {8, 3, 4, 3, 0, 60},
      {6, 5, 5, 4, 0, 70},
      {10, 5, 1, 5, 3, 70},
      {8, 4, 4, 4, 3, 20},
      {4, 5, 2, 6, 5, 20},
      {4, 2, 5, 4, 1, 20},
      {6, 2, 1, 5, 0, 70},
      {4, 3, 4, 3, 2, 60},
      {4, 3, 4, 3, 5, 100},
      {10, 3, 2, 8, 1, 100},
      {8, 4, 1, 1, 1, 90},
      {10, 4, 4, 3, 3, 90},
      {6, 5, 1, 4, 3, 10},
      {8, 3, 3, 9, 5, 80},
      {6, 3, 4, 2, 0, 30},
      {8, 5, 4, 4, 1, 90},
      {10, 4, 2, 1, 5, 60},
      {10, 1, 3, 3, 4, 20},
      {6, 2, 5, 7, 1, 40},
      {4, 4, 3, 2, 0, 50},
      {10, 3, 1, 1, 4, 50},
      {6, 4, 2, 6, 4, 90},
      {6, 4, 5, 1, 3, 50},
      {6, 2, 4, 10, 1, 80},
      {4, 5, 5, 5, 4, 50},
      {10, 2, 1, 6, 5, 20},
      {6, 4, 1, 10, 4, 60},
      {4, 3, 3, 10, 5, 100},
      {8, 3, 5, 5, 1, 40},
      {8, 3, 4, 10, 5, 60},
      {10, 1, 1, 4, 3, 60},
      {10, 3, 3, 6, 4, 10},
      {10, 1, 3, 3, 2, 70},
      {4, 2, 1, 2, 1, 90},
      {6, 3, 1, 10, 2, 90},
      {8, 2, 5, 8, 2, 90},
      {10, 3, 2, 6, 5, 80},
      {6, 4, 5, 6, 3, 40},
      {4, 1, 1, 5, 0, 100},
      {8, 1, 1, 3, 2, 20},
      {4, 4, 2, 8, 2, 50},
      {4, 1, 1, 6, 1, 90},
      {10, 5, 3, 3, 1, 60},
      {10, 2, 3, 7, 5, 20},
      {8, 3, 3, 9, 2, 20},
      {8, 4, 2, 9, 5, 100},
      {4, 2, 1, 7, 0, 50},
      {10, 3, 2, 3, 2, 100},
      {4, 3, 5, 1, 4, 100},
      {6, 1, 4, 10, 3, 90}
  };

  public static double[] playNWithParams(int[] params) {
//    ElapsedCpuTimer t = new ElapsedCpuTimer();

    Constants.SHIP_MAX_SPEED = params[0];
    Constants.THRUST_SPEED = params[1];
    Constants.MISSILE_COST = params[2];
    Constants.MISSILE_MAX_SPEED = params[3];
    Constants.MISSILE_COOLDOWN = params[4];
//    Constants.KILL_AWARD = params[5];

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