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
  protected boolean dead;
  protected boolean wrappable;
  public Color color;

  public GameObject() {
    this.pos.set(Constants.WIDTH / 2, Constants.WIDTH / 2);
    this.dead = false;
    this.wrappable = true;
    this.color = Types.WHITE;
  }

  public GameObject(Vector2d _pos) {
    this.pos = _pos;
    this.dead = false;
    this.wrappable = true;
    this.color = Types.WHITE;
  }

  public abstract GameObject copy();

  public abstract void update();

  public abstract void draw(Graphics2D g);

  public Vector2d getPosition() {
    return pos;
  }

  public boolean isDead() {
    return dead;
  }

  public boolean isWrappable() {
    return wrappable;
  }

  public void setWrappable(boolean _wrappable) {
    this.wrappable = _wrappable;
  }

  public void setColor(Color _color) {
    this.color = _color;
  }
}
