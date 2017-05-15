package controllers.labMCTS;

import core.game.StateObservationMulti;
import tools.ElapsedCpuTimer;

import java.util.Random;

/**
 * A player that implements MCTS. It simply has a reference to the node at the root.
 * TODO: 16/05/17 Exercise Lab MCTS (2): Read the code. Do you understand what's happening in this class?
 */
public class SingleMCTSPlayer
{

  /**
   * Root of the tree.
   */
  public SingleTreeNode m_root;

  /**
   * Random generator.
   */
  public Random m_rnd;


  /**
   * Creates a player with a random generator.
   * @param a_rnd
   */
  public SingleMCTSPlayer(Random a_rnd)
  {
    m_rnd = a_rnd;
  }

  /**
   * Inits the tree with the new observation state in the root.
   * @param a_gameState current state of the game.
   */
  public void init(StateObservationMulti a_gameState)
  {
    //Set the game observation to a newly root node.
    m_root = new SingleTreeNode(m_rnd);
    m_root.rootState = a_gameState;
  }

  /**
   * Runs MCTS to decide the action to take. It does not reset the tree.
   * @param elapsedTimer Timer when the action returned is due.
   * @return the action to execute in the game.
   */
  public int run(ElapsedCpuTimer elapsedTimer)
  {
    //Do the search within the available time.
    m_root.mctsSearch(elapsedTimer);

    //Determine the best action to take and return it.
    int action = m_root.mostVisitedAction();
    //int action = m_root.bestAction();

    return action;
  }


}
