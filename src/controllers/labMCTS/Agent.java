package controllers.labMCTS;

import competition.CompetitionParameters;
import core.game.StateObservationMulti;
import core.player.AbstractMultiPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;

import java.util.ArrayList;
import java.util.Random;

/**
 * Main class to hold a playing agent.
 * TODO: 16/05/17 Exercise Lab MCTS (1): Read the code. Do you understand what's happening in this class?
 */
public class Agent extends AbstractMultiPlayer {


  // A few needed variables.
  public static int[] NUM_ACTIONS;
  public static int MCTS_ITERATIONS = CompetitionParameters.MCTS_ITER;
  public static int ROLLOUT_DEPTH = 10;
  public static double K = Math.sqrt(2);
  public static double REWARD_DISCOUNT = 1.00;
  public static Types.ACTIONS[][] actions;
  public static int id, oppID, no_players;

  protected SingleMCTSPlayer mctsPlayer;

  /**
   * Public constructor with state observation and time due.
   * @param so state observation of the current game.
   * @param elapsedTimer Timer for the controller creation.
   */
  public Agent(StateObservationMulti so, ElapsedCpuTimer elapsedTimer, int playerID)
  {
    //get game information about the game.
    no_players = so.getNoPlayers();
    id = playerID;
    oppID = (id + 1) % so.getNoPlayers();

    //Get the actions for all players in a static array.
    NUM_ACTIONS = new int[no_players];
    actions = new Types.ACTIONS[no_players][];
    for (int i = 0; i < no_players; i++) {

      ArrayList<Types.ACTIONS> act = so.getAvailableActions(i);

      actions[i] = new Types.ACTIONS[act.size()];
      for (int j = 0; j < act.size(); ++j) {
        actions[i][j] = act.get(j);
      }
      NUM_ACTIONS[i] = actions[i].length;
    }

    //Create the player.
    mctsPlayer = getPlayer(so, elapsedTimer);
  }

  public SingleMCTSPlayer getPlayer(StateObservationMulti so, ElapsedCpuTimer elapsedTimer) {
    return new SingleMCTSPlayer(new Random());
  }


  /**
   * Picks an action. This function is called every game step to request an
   * action from the player.
   * @param stateObs Observation of the current state.
   * @param elapsedTimer Timer when the action returned is due.
   * @return An action for the current state
   */
  public Types.ACTIONS act(StateObservationMulti stateObs, ElapsedCpuTimer elapsedTimer) {

    //Set the state observation object as the new root of the tree.
    mctsPlayer.init(stateObs);

    //Determine the action using MCTS...
    int action = mctsPlayer.run(elapsedTimer);

    //... and return it.
    return actions[id][action];
  }

}