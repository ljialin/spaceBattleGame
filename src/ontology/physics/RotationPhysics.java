package ontology.physics;

import ontology.Constants;
import tools.Vector2d;

/**
 * Created by Jialin Liu on 04/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class RotationPhysics {

  public static double rotate(double x, double y) {
    double rot = Math.atan2(y, x) + Math.PI / 2;
    return rot;
  }

  public static double rotate(Vector2d v) {
    return rotate(v.x, v.y);
  }

  public static void steer(Vector2d dir, double angle) {
    dir.rotate(angle* Constants.RADIAN_UNIT);
  }
}
