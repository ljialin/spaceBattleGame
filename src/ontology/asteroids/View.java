package ontology.asteroids;

import core.game.StateObservationMulti;
import ontology.Constants;
import ontology.Types;

import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Dimension;
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
  public static final Font FONT = new Font("Courier", Font.PLAIN, 20);
  public View(StateObservationMulti game) {
    this.game = game;
  }

  public void paintComponent(Graphics gx) {
//    System.out.println("View : paintComponent begin !");

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

//    System.out.println("View : paintComponent ready !");
  }

  public void paintState(Graphics2D g) {
    /** draw avatars */
    if (game.getAvatars().length != 0) {
      GameObject[] objectsCopy = game.getAvatars();
      for (GameObject object : objectsCopy) {
        object.draw(g);
      }
    }


    if(!game.getObjects().isEmpty()) {
      GameObject[] objectsCopy = game.getObjects().toArray(new GameObject[game.getObjects().size()]);
      for (GameObject object : objectsCopy) {
        object.draw(g);
      }
    }

    g.setColor(Types.WHITE);
    g.setFont(FONT);
//    System.out.println("View : paintState finished !");

    double sc0 = ((int)(game.getGameScore(0) * 1000) * 0.001);
    double sc1 = ((int)(game.getGameScore(1) * 1000) * 0.001);
    String strScores    = "Score:    " + sc0 + " | " + sc1;


    String strPoints = "Points:    " + game.getPlayerPoints(0) + " | " + game.getPlayerPoints(1);
    String strTicks = "Ticks:    " + game.getGameTick();
    String strLives = "Life: " + game.getAvatarLives(0) +  " | " + game.getAvatarLives(1);
    String p1 = "P1 BLUE";
    String p2 = "P2 GREEN";
    g.drawString(strPoints, 10, 20);
    g.drawString(strScores, 10, 40);
    g.drawString(strLives, 10, 80);
    g.drawString(strTicks, 10, 110);
    g.drawString(p1, 10, 140);
    g.drawString(p2, 10, 170);
  }

  public Dimension getPreferredSize() {
    return new Dimension(Constants.WIDTH, Constants.HEIGHT);
  }
}
