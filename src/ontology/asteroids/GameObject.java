package ontology.asteroids;

import ontology.Constants;
import ontology.Types;
import tools.Vector2d;

import java.awt.*;

/**
 * Created by Jialin Liu on 04/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public abstract class GameObject {
  protected Vector2d pos;
  protected double radius;
  protected boolean dead;
  protected boolean wrappable;
  protected int playerId;
  public Color color;
  public int destructivePower;

  public GameObject() {
    this.pos.set(Constants.WIDTH / 2, Constants.WIDTH / 2);
    this.radius = 0;
    this.playerId = -1;
    this.dead = false;
    this.wrappable = true;
    this.color = Types.WHITE;
  }

  public GameObject(Vector2d pos) {
    this();
  }

  public abstract GameObject copy();

  public abstract void update();

  public abstract void draw(Graphics2D g);

  public Vector2d getPosition() {
    return pos;
  }

  public void setPosition(double x, double y) {
    this.pos.x = x;
    this.pos.y = y;
  }

  public int getPlayerId() {
    return playerId;
  }

  public void setPlayerId(int _playerId) {
    this.playerId = _playerId;
  }

  public double getRadius() {
    return this.radius;
  }

  public boolean isDead() {
    return dead;
  }

  public void kill() {
    this.dead = true;
  }

  public boolean isWrappable() {
    return wrappable;
  }

  public void setWrappable(boolean wrappable) {
    this.wrappable = wrappable;
  }

  public void setColor(Color color) {
    this.color = color;
  }
}
