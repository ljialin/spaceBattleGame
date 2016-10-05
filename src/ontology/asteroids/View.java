package ontology.asteroids;

import core.game.StateObservationMulti;
import ontology.Constants;
import ontology.Types;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;


/**
 * Created by Jialin Liu on 04/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class View extends JComponent {
  public StateObservationMulti game;
  public boolean ready = false;
  public static Color bg = Types.BLACK;
  public static final Font FONT = new Font("Courier", Font.PLAIN, 20);
  Font font = new Font("Courier", Font.PLAIN, 20);
  public View(StateObservationMulti game) {
    this.game = game;
  }

  public void paintComponent(Graphics gx) {
    System.out.println("View : paintComponent begin !");

    for (Ship ship : this.game.getAvatars()) {
      if (ship == null) {
        return;
      }
    }

    Graphics2D g = (Graphics2D) gx;
    AffineTransform at = g.getTransform();
    g.translate((1 - Constants.VIEW_SCALE) * Constants.WIDTH / 2, (1- Constants.VIEW_SCALE) * Constants.HEIGHT / 2);
    g.scale(Constants.VIEW_SCALE, Constants.VIEW_SCALE);

    game.draw(g);
    g.setTransform(at);
    paintState(g);

    this.ready = true;

    System.out.println("View : paintComponent ready !");
  }

  public void paintState(Graphics2D g) {
    /** draw avatars */
    if (game.getAvatars().length != 0) {
      GameObject[] objectsCopy = game.getAvatars();
      for (GameObject object : objectsCopy) {
        object.draw(g);
      }
    }

    g.setColor(Types.WHITE);
    g.setFont(FONT);

    if(!game.getObjects().isEmpty()) {
      GameObject[] objectsCopy = game.getObjects().toArray(new GameObject[game.getObjects().size()]);
      for (GameObject object : objectsCopy) {
        object.draw(g);
      }
    }
    System.out.println("View : paintState finished !");


  }

  public Dimension getPreferredSize() {
    return new Dimension(Constants.WIDTH, Constants.HEIGHT);
  }
}