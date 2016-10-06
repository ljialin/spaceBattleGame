package ontology.asteroids;

import ontology.Constants;
import ontology.Types;
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
    super(playerId, pos, velocity);
    this.destructivePower = Constants.MISSILE_DESTRUCTIVE_POWER;
    this.ttl = missileTTL;
    setRadius();
  }

  public Missile(int playerId, Vector2d pos, Vector2d velocity) {
    this(playerId, pos, velocity, Constants.MISSILE_MAX_TTL);
  }

  public void setVelocityByDir(Vector2d dir) {
    this.velocity = Vector2d.multiply(dir, Constants.MISSILE_MAX_SPEED);
  }

  @Override
  public void update() {
    if (!isDead()) {
      pos.add(velocity);
      ttl--;
    }
  }

  @Override
  public void setRadius() {
    this.radius = Constants.MISSILE_RADIUS;
  }

  @Override
  public void draw(Graphics2D g) {
    g.setColor(Types.RED);
    g.fillOval((int) (pos.x-radius), (int) (pos.y-radius), (int) radius * 2, (int) radius * 2);
  }

  @Override
  public GameObject copy() {
    Missile object = new Missile(playerId, pos.copy(), velocity.copy(), ttl);
    return object;
  }
}
