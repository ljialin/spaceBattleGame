package ontology.asteroids;

import ontology.Constants;
import tools.Vector2d;

import java.awt.*;


/**
 * Created by Jialin Liu on 04/10/16.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class Missile extends Weapon {
  public Missile(int playerId, Vector2d pos, Vector2d velocity, int missileTTL) {
    super(playerId, pos, velocity, missileTTL);
    this.destructivePower = Constants.MISSILE_DESTRUCTIVE_POWER;
  }

    @Override
  public void draw(Graphics2D g) {
    g.setColor(Color.red);
    g.fillOval((int) (pos.x-radius), (int) (pos.y-radius), (int) radius * 2, (int) radius * 2);
  }
}
