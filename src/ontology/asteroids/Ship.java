package ontology.asteroids;

import core.player.Player;
import ontology.Constants;
import ontology.Types;
import ontology.physics.RotationPhysics;
import tools.KeyHandler;
import tools.KeyInput;
import tools.Vector2d;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by Jialin Liu on 04/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class Ship extends GameObject {
  public Vector2d dir;
  public Vector2d velocity;
  public boolean thrusting;
  public int healthPoints;
  public ArrayList<Types.ACTIONS> actions;
  public ArrayList<Types.ACTIONS> actionsNIL;
  public TreeMap<Integer, Integer> resources;
  public Player player;
  public int playerId;

  private double score = 0.0;
  private Types.WINNER winState = Types.WINNER.NO_WINNER;
  private KeyHandler ki;

  /** define the shape of the ship */
  static int[] xp = {-Constants.SHIP_SIZE, 0, Constants.SHIP_SIZE, 0};
  static int[] yp = {Constants.SHIP_SIZE, -Constants.SHIP_SIZE, Constants.SHIP_SIZE, 0};
  /** the thrust poly that will be drawn when the ship is thrusting */
  static int[] xpThrust =  {-Constants.SHIP_SIZE, 0, Constants.SHIP_SIZE, 0};
  static int[] ypThrust = {Constants.SHIP_SIZE, Constants.SHIP_SIZE+1, Constants.SHIP_SIZE, 0};

  public Ship() {
    reset();
  }

  public Ship(Vector2d _pos, Vector2d _dir, Vector2d _velocity) {
    super(_pos);
    this.pos = _pos;
    this.velocity = _velocity;
    this.thrusting = false;
  }

  public void reset() {
    this.pos.set(Constants.WIDTH / 2, Constants.WIDTH / 2);
    this.velocity.zero();
    this.dir.set(0, -1);
    this.dead = false;
    this.thrusting = false;
  }

  public Rectangle2D getBound() {
    return new Rectangle2D.Double(pos.x,pos.y,Double.valueOf(xp[2]-xp[0]),Double.valueOf(yp[0]-yp[1]));
  }

  public Vector2d getDirection() {
    return dir;
  }

  public Vector2d getVelocity() {
    return velocity;
  }

  public boolean isThrusting() {
    return thrusting;
  }

  public Types.WINNER getWinState() {
    return winState;
  }

  public double getScore() {
    return score;
  }

  public int getPlayerId() {
    return playerId;
  }

  public void setPlayerId(int _playerId) {
    this.playerId = _playerId;
  }

  public void setPlayer(Player _player) {
    this.player = _player;
  }
  /**
   * Gets the key handler of this avatar.
   * @return - KeyHandler object.
   */
  public KeyHandler getKeyHandler() { return ki; }

  /**
   * Sets the key handler of this avatar.
   * @param k - new KeyHandler object.
   */
  public void setKeyHandler(KeyHandler k) {
    if (k instanceof KeyInput)
      ki = new KeyInput();
    else ki = k;
  }

  @Override
  public Ship copy() {
    Ship cloneShip = new Ship(pos, dir, velocity);
    return cloneShip;
  }

  @Override
  public void update() {

  }

  @Override
  public void draw(Graphics2D g) {
    AffineTransform at = g.getTransform();
    g.translate(pos.x, pos.y);
    double rot = RotationPhysics.rotate(dir);
    g.rotate(rot);
    g.scale(Constants.SHIP_SCALE, Constants.SHIP_SCALE);
    g.setColor(color);
    g.fillPolygon(xp, yp, xp.length);
    if (this.thrusting) {
      g.setColor(Color.red);
      g.fillPolygon(xpThrust, ypThrust, xpThrust.length);
    }
    g.setTransform(at);
  }
}
