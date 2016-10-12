package controllers.rotateAndShoot;

import core.game.StateObservationMulti;
import core.player.AbstractMultiPlayer;
import ontology.Constants;
import ontology.Types;
import tools.ElapsedCpuTimer;

/**
 * Created by Jialin Liu on 11/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class Agent extends AbstractMultiPlayer {

  int id; //this player's ID
  int cooldown;

  /**
   * initialize all variables for the agent
   * @param stateObs Observation of the current state.
   * @param elapsedTimer Timer when the action returned is due.
   * @param playerID ID if this agent
   */
  public Agent(StateObservationMulti stateObs, ElapsedCpuTimer elapsedTimer, int playerID){
    id = playerID;
    cooldown = Constants.MISSILE_COOLDOWN;
    // now cheat
  }

  /**
   * return ACTION_NIL on every call to simulate doNothing player
   * @param stateObs Observation of the current state.
   * @param elapsedTimer Timer when the action returned is due.
   * @return 	ACTION_NIL all the time
   */
  @Override
  public Types.ACTIONS act(StateObservationMulti stateObs, ElapsedCpuTimer elapsedTimer) {
    if (cooldown==0) {
      cooldown = Constants.MISSILE_COOLDOWN;
      return Types.ACTIONS.ACTION_FIRE;
    } else {
      cooldown--;
      return Types.ACTIONS.ACTION_LEFT;
    }
  }
}