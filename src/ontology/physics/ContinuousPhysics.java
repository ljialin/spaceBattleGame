package ontology.physics;

import ontology.asteroids.GameObject;
import tools.Vector2d;

/**
 * Created by Jialin Liu on 04/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class ContinuousPhysics {
  public double gravity;
  public double friction;

  public ContinuousPhysics() {
    this.gravity = 0.0;
    this.friction = 0.01;
  }

  public void passiveMovement(GameObject object)
  {
    // TODO: 04/10/2016
  }

  public void activeMovement(GameObject object, Vector2d dir, double speed)
  {
    // TODO: 04/10/2016
  }

}
