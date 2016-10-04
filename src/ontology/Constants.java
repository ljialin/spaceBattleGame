package ontology;

import java.awt.*;

/**
 * Created by Jialin Liu on 04/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class Constants {
  public static final int SHIP_SIZE = 2;
  public static final double SHIP_SCALE = 5;
  public static final double SHIP_MAX_SPEED = 3; // define how quickly the ship will rotate

  public static final double RADIAN_UNIT = 10 * Math.PI / 180; // steerStep

  public static final int WIDTH = 640;
  public static final int HEIGHT = 480;
  public static final double VIEW_SCALE = 1.0;

  public static final int MAX_HEALTH_POINTS = 3;
}
