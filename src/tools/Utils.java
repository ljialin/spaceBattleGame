package tools;

import ontology.Types;

/**
 * Created by Jialin Liu on 04/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class Utils {



  private static double clamp(double v, double min, double max) {
    if (v > max) {
      return max;
    }

    if (v < min) {
      return min;
    }

    return v;
  }

  public static Direction processMovementActionKeys(boolean[] key_pressed, int idx) {

    int vertical = 0;
    int horizontal = 0;

    if (key_pressed[Types.ACTIONS.ACTION_THRUST.getKey()[idx]]) {
      vertical = 1;
    }

    if (key_pressed[Types.ACTIONS.ACTION_LEFT.getKey()[idx]]) {
      horizontal = -1;
    }
    if (key_pressed[Types.ACTIONS.ACTION_RIGHT.getKey()[idx]]) {
      horizontal = 1;
    }

    if (horizontal == 0) {
      if (vertical == 1)
        return Types.DTHRUST;
    } else if (vertical == 0) {
      if (horizontal == 1) {
        return Types.DRIGHT;
      }
      else if (horizontal == -1)
        return Types.DLEFT;
    }
    return Types.DNIL;
  }

}
