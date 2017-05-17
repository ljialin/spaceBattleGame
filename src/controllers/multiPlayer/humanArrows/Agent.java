package controllers.multiPlayer.humanArrows;

import core.game.StateObservationMulti;
import core.player.AbstractMultiPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Jialin Liu on 05/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class Agent extends AbstractMultiPlayer implements KeyListener {
  Types.ACTIONS action;
  static int nControllers = 0;

  /**
   * Public constructor with state observation and time due.
   * @param so state observation of the current game.
   * @param elapsedTimer Timer for the controller creation.
   * @param playerID ID if this agent
   */
  public Agent(StateObservationMulti so, ElapsedCpuTimer elapsedTimer, int playerID)
  {
    setPlayerID(playerID);
    action = Types.ACTIONS.ACTION_NIL;
  }

  @Override
  public Types.ACTIONS act(StateObservationMulti stateObs, ElapsedCpuTimer elapsedTimer) {
    return action;
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    switch (key) {
      case KeyEvent.VK_UP :
        action = Types.ACTIONS.ACTION_THRUST;
        break;
      case KeyEvent.VK_LEFT :
        action = Types.ACTIONS.ACTION_LEFT;
        break;
      case KeyEvent.VK_RIGHT :
        action = Types.ACTIONS.ACTION_RIGHT;
        break;
      case KeyEvent.VK_SPACE :
        action = Types.ACTIONS.ACTION_FIRE;
        break;
      default:
        action = Types.ACTIONS.ACTION_NIL;
        break;
    }
  }

  public void keyReleased(KeyEvent e) {
    int key = e.getKeyCode();
    if (key == KeyEvent.VK_UP) {
      action = Types.ACTIONS.ACTION_NIL;
    }
    if (key == KeyEvent.VK_LEFT) {
      action = Types.ACTIONS.ACTION_NIL;
    }
    if (key == KeyEvent.VK_RIGHT) {
      action = Types.ACTIONS.ACTION_NIL;
    }
    if (key == KeyEvent.VK_SPACE) {
      action = Types.ACTIONS.ACTION_NIL;
    }
  }

  @Override
  public void draw(Graphics2D g) {}
}
