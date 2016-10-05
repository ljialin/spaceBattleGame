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
public class GravityPhysics {

  public static void gravity(Vector2d pos, Vector2d velocity) {
    pos.subtract(0, Constants.GRAVITY);
    velocity.mul(Constants.FRICTION);
  }
}
