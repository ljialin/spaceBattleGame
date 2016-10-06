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

  public Weapon(int playerId, Vector2d pos, Vector2d velocity) {
    super(pos);
    this.playerId = playerId;
    this.velocity = velocity;
    setRadius();
  }

  public void setRadius() {
    this.radius = 1;
  }

  @Override
  public void update() {
    if (!isDead()) {
      pos.add(velocity);
      ttl--;
    }
  }

  @Override
  public GameObject copy() {
    Weapon object = new Weapon(playerId, pos.copy(), velocity.copy());
    return object;
  }

  @Override
  public void draw(Graphics2D g) {
  }

  @Override
  public void injured(int harm) {
    this.ttl -= harm;
  }

  @Override
  public boolean isDead() {
    dead = ( ttl <= 0);
    return dead;
  }

  public void hit() {
    // kill it by setting ttl to zero
    ttl = 0;
  }

  public String toString() {
    return ttl + " :> " + pos;
  }
}
