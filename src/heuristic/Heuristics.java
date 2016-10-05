package heuristic;

import ontology.asteroids.Ship;

/**
 * Created by Jialin Liu on 05/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class Heuristics {

  public static double calcDistScore(Ship ship1, Ship ship2) {
    double dist = Math.abs(ship1.distTo(ship2));
    double dot = ship1.dotTo(ship2);
    double distPoints = 1.0/(1.0+dist/100.0);
    return dot*distPoints;
  }
}
