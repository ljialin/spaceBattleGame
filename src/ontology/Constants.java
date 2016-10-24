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
  public static  double SHIP_MAX_SPEED = 10; // to optimise, id=2
  public static  double THRUST_SPEED = 4; // to optimise, id=3
  public static  int MISSILE_COST = 50; // to optimise, id=4
  public static  int MISSILE_MAX_SPEED = 3; // to optimise, id=7
  public static  int MISSILE_COOLDOWN = 5; // to optimise, id=8
  public static  int KILL_AWARD = 70; // to optimise, id=11



  public static int SHIP_RADIUS = 20; // to optimise, id=1
  public static final double SHIP_SCALE = 1;
  public static final double MAX_REPULSE_FORCE = 1.0;

  public static final int WEAPON_ID_MISSILE = 1;
  public static final int MISSILE_MAX_RESOURCE = 1000000;
  public static  double MISSILE_RADIUS = 4; // to optimise, id=5
  public static  int MISSILE_MAX_TTL = 30; // to optimise, id=6
  public static final int MISSILE_DESTRUCTIVE_POWER = 1;

  public static final double GRAVITY = 0;
  public static  double FRICTION = 0.99; // to optimise, id=9

  public static  double RADIAN_UNIT = 10 * Math.PI / 180; // to optimise, id=10

  public static final int WIDTH = 640;
  public static final int HEIGHT = 480;
  public static final double VIEW_SCALE = 1.0;

  public static final int MAX_HEALTH_POINTS = 1000;
  public static final int LIVE_AWARD = 10;

  public static final boolean SHOW_ROLLOUTS = true;
}
