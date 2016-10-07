package ontology;

/**
 * Created by Jialin Liu on 04/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class Constants {

  public static final int SHIP_RADIUS = 10;
  public static final double SHIP_SCALE = 1; //
  public static final double SHIP_MAX_SPEED = 5; //
  public static final double MAX_REPULSE_FORCE = 1.0;
  public static final double THRUST_SPEED = 1.0;

  public static final int WEAPON_ID_MISSILE = 1; //
  public static final int MISSILE_MAX_RESOURCE = 30;
  public static final int MISSILE_COST = 1;
  public static final double MISSILE_RADIUS = 4;
  public static final int MISSILE_MAX_TTL = 20;
  public static final int MISSILE_MAX_SPEED = 10;
  public static final int MISSILE_DESTRUCTIVE_POWER = 1;
  public static final int MISSILE_COOLDOWN = 3;

  public static final double GRAVITY = 0; //
  public static final double FRICTION = 0.99;

  public static final double RADIAN_UNIT = 10 * Math.PI / 180;

  public static final int WIDTH = 640; //
  public static final int HEIGHT = 480; //
  public static final double VIEW_SCALE = 1.0; //

  public static final int MAX_HEALTH_POINTS = 3;
  public static final int KILL_AWARD = 10;
  public static final int LIVE_AWARD = 10;

  public static final boolean SHOW_ROLLOUTS = true; //
}
