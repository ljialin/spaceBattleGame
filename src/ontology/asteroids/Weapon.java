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
public class Weapon extends GameObject {
  public Vector2d velocity;
  public int ttl;

  public Weapon(int playerId, Vector2d pos, Vector2d velocity, int ttl) {
    super(pos);
    this.playerId = playerId;
    this.velocity = velocity;
    this.ttl = ttl;
    setParams();
  }

  public Weapon(int playerId, Vector2d pos, Vector2d velocity) {
    this(playerId, pos, velocity, Constants.MISSILE_MAX_TTL);
  }

  public Weapon(Vector2d pos, Vector2d velocity) {
    this(-1, pos, velocity, Constants.MISSILE_MAX_TTL);
  }

  public void setParams() {
    this.radius = Constants.MISSILE_RADIUS;
  }

  @Override
  public void update() {
    if (!dead()) {
      pos.add(velocity);
      ttl--;
    }
  }

  @Override
  public GameObject copy() {
    Weapon object = new Weapon(playerId, pos, velocity, ttl);
    return object;
  }

  @Override
  public void draw(Graphics2D g) {
  }

  public boolean dead() {
    return ttl <= 0;
  }

  public void hit() {
    // kill it by setting ttl to zero
    ttl = 0;
  }

  public String toString() {
    return ttl + " :> " + pos;
  }
}
