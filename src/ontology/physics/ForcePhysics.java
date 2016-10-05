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
public class ForcePhysics {

  public static void repulse(Vector2d pos, Vector2d dir, boolean isRandom) {
    double strength = Constants.MAX_REPULSE_FORCE;
    if(isRandom) {
      strength *= Math.random();
    }
    pos.subtract(strength*dir.x, strength*dir.y);
  }

  public static void thrust(Vector2d velocity, Vector2d dir) {
    velocity.add(Constants.THRUST_SPEED*dir.x, Constants.THRUST_SPEED*dir.y);
  }
}
