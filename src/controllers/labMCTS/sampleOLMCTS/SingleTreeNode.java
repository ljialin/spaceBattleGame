package controllers.labMCTS.sampleOLMCTS;

import controllers.multiPlayer.heuristics.Heuristics;
import core.game.StateObservationMulti;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tools.Utils;

import java.util.Random;

/**
 * Class for a tree node.
 * TODO: 16/05/17 Exercise Lab MCTS (3): Read the code. Do you understand what's happening in this class?
 */
public class SingleTreeNode
{
  //A few important constants.
  private static final double HUGE_NEGATIVE = -10000000.0;
  private static final double HUGE_POSITIVE =  10000000.0;
  public static double epsilon = 1e-6;
  public static double egreedyEpsilon = 0.05;

  //Reference to the parent and children nodes.
  public SingleTreeNode parent;
  public SingleTreeNode[] children;

  //Stats of this tree.
  public double totValue;
  public int nVisits;
  public int m_depth;

  //Random generator
  public static Random m_rnd;

  //Bounds with the lowest and highest reward ever seen.
  protected static double[] bounds = new double[]{Double.MAX_VALUE, -Double.MAX_VALUE};

  //Index of this node in the parent's children array.
  public int childIdx;

  //Reference to the root.
  public static StateObservationMulti rootState;

  //Creates a single tree node.
  public SingleTreeNode(Random rnd) {
    this(null, -1, rnd);
  }
  public SingleTreeNode(SingleTreeNode parent, int childIdx, Random rnd) {
    this.parent = parent;
    m_rnd = rnd;
    totValue = 0.0;
    this.childIdx = childIdx;
    if(parent != null)
      m_depth = parent.m_depth+1;
    else
      m_depth = 0;
    children = new SingleTreeNode[Agent.NUM_ACTIONS[Agent.id]];
  }

  /**
   * Start of the MCTS algorithm. Runs for Agent.MCTS_ITERATIONS iterations.
   * @param elapsedTimer
   */
  public void mctsSearch(ElapsedCpuTimer elapsedTimer) {

    int numIters = 0;

    //TODO: 16/05/17 Exercise Lab MCTS (4): Read the code. Do you understand what's happening on each iteration?
    while(numIters < Agent.MCTS_ITERATIONS){
      //On each iteration, we make a copy of the state.
      StateObservationMulti state = rootState.copy();

      //Tree policy, going down the tree.
      SingleTreeNode selected = treePolicy(state);

      //Roll out
      double delta = selected.rollOut(state);

      //Updates statistics of the visited nodes.
      backUp(selected, delta);

      numIters++;
    }
//    System.out.println(numIters);
  }

  /**
   * Executes the tree policy from a state received as parameter.
   * @param state state to start running from
   * @return A new node added to the tree, in the Expand phase of MCTS,
   * or a node already in the tree if a terminal state was reached.
   */
  public SingleTreeNode treePolicy(StateObservationMulti state) {

    SingleTreeNode cur = this;

    //Keep going down the tree until depth reached or game is over
    while (!state.isGameOver() && cur.m_depth < Agent.ROLLOUT_DEPTH)
    {

      if (cur.notFullyExpanded()) {
        //We reached a node where all children have not been explored. Need to Expand.
        return cur.expand(state);

      } else {
        //Keep going down using UCT to select the next node to pick
        SingleTreeNode next = cur.uct(state);
        cur = next;
      }
    }

    return cur;
  }

  /**
   * Expansion phase of the MCTS algorithm.
   * @param state State to roll forward to create a new state.
   * @return
   */
  public SingleTreeNode expand(StateObservationMulti state) {

    int bestAction = 0;
    double bestValue = -1;

    //Pick a non-expanded child at random.
    for (int i = 0; i < children.length; i++) {
      double x = m_rnd.nextDouble();
      if (x > bestValue && children[i] == null) {
        bestAction = i;
        bestValue = x;
      }
    }

    //need to provide actions for all players to advance the forward model
    Types.ACTIONS[] acts = new Types.ACTIONS[Agent.no_players];

    //set this agent's action
    acts[Agent.id] = Agent.actions[Agent.id][bestAction];

    //get actions available to the opponent and assume they will do a random action
    Types.ACTIONS[] oppActions = Agent.actions[Agent.oppID];
    acts[Agent.oppID] = oppActions[new Random().nextInt(oppActions.length)];

    //Roll the state forward with the actions selected.
    state.advance(acts);

    //Create a new node with the state reaced and add it to the tree.
    SingleTreeNode tn = new SingleTreeNode(this,bestAction,m_rnd);
    children[bestAction] = tn;
    return tn;
  }

  /**
   * UCT policy of the MCTS algorithm. It requires a state to choose an action from.
   * @param state state to apply UCT in
   * @return The selected node.
   */
  public SingleTreeNode uct(StateObservationMulti state) {

    SingleTreeNode selected = null;
    double bestValue = -Double.MAX_VALUE;
    for (SingleTreeNode child : this.children)
    {
      //EXPLOITATION term: average of rewards, normalized within the bounds so it's in [0,1]
      double hvVal = child.totValue;
      double exploitation =  hvVal / (child.nVisits + epsilon);
      exploitation = Utils.normalise(exploitation, bounds[0], bounds[1]);

      //EXPLORATION term: to value highly states rarely visited
      double exploration = Math.sqrt(Math.log(this.nVisits + 1) / (child.nVisits + epsilon));

      //UCB1 Equation build up.
      double uctValue = exploitation + Agent.K * exploration;

      //This allows breaking ties randomly
      uctValue = Utils.noise(uctValue, epsilon, m_rnd.nextDouble());

      //We need to keep the highest UCB1 value.
      if (uctValue > bestValue) {
        selected = child;
        bestValue = uctValue;
      }
    }
    if (selected == null)
    {
      //Uh oh... this shouldn't happen.
      throw new RuntimeException("Warning! returning null: " + bestValue + " : " + this.children.length + " " +
          + bounds[0] + " " + bounds[1]);
    }


    //need to provide actions for all players to advance the forward model
    Types.ACTIONS[] acts = new Types.ACTIONS[Agent.no_players];

    //set this agent's action
    acts[Agent.id] = Agent.actions[Agent.id][selected.childIdx];

    //get actions available to the opponent and assume they will do a random action
    Types.ACTIONS[] oppActions = Agent.actions[Agent.oppID];
    acts[Agent.oppID] = oppActions[new Random().nextInt(oppActions.length)];

    //Roll the state forward with the actions selected
    state.advance(acts);

    //Return the selected node.
    return selected;
  }

  /**
   * Rollout phase of MCTS
   * @param state state to start the rollout from.
   * @return the Value (aka reward, fitness) of the state reached at the end of the rollout.
   */
  public double rollOut(StateObservationMulti state)
  {
    int thisDepth = this.m_depth;

    //Check that we don't have to finish the rollout.
    while (!finishRollout(state,thisDepth)) {

      //Random move for all players
      Types.ACTIONS[] acts = new Types.ACTIONS[Agent.no_players];
      for (int i = 0; i < Agent.no_players; i++) {
        acts[i] = Agent.actions[i][m_rnd.nextInt(Agent.NUM_ACTIONS[i])];
      }

      //Roll the state forward
      state.advance(acts);
      thisDepth++;
    }

    //Reached the end of the rollout, we need to retrieve the value of the state.
    double delta = value(state);

    //Update the bounds with the newly seen reward
    if(delta < bounds[0])
      bounds[0] = delta;
    if(delta > bounds[1])
      bounds[1] = delta;

    return delta;
  }

  /**
   * Evaluates the state reached
   * @param a_gameState state to evaluate
   * @return Value of the state passed as parameter, according to some heuristic.
   */
  public double value(StateObservationMulti a_gameState) {

    //Is the game over?
    boolean gameOver = a_gameState.isGameOver();

    //Let's collect victory state and current score
    Types.WINNER win = a_gameState.getMultiGameWinner()[Agent.id];
    double rawScore = myHeuristicScore(a_gameState);

    //Simple: high positive number if we won, high negative if we lost.
    if(gameOver && win == Types.WINNER.PLAYER_LOSES)
      rawScore += HUGE_NEGATIVE;

    if(gameOver && win == Types.WINNER.PLAYER_WINS)
      rawScore += HUGE_POSITIVE;

    //If the game hasn't finished, our value is the current score which we try to maximize.
    return rawScore;
  }

  /**
   * Heuristic for a score on steroids. Do we want something else apart from the score?
   * @param a_gameState state to evaluate.
   * @return Value for this state
   */
  public double myHeuristicScore(StateObservationMulti a_gameState) {
    //This is the normal game score
    double rawScore = a_gameState.getGameScore(Agent.id);

    //What about some extra score based on the distance between the players?
    double distScore = Heuristics.calcDistScore(
        a_gameState.getAvatars()[Agent.id], a_gameState.getAvatars()[1- Agent.id]);

    return rawScore;
  }


  /**
   * Indicates if the rollout should finish.
   * @param rollerState current state of the rollout.
   * @param depth Depth in the tree at this moment.
   * @return true if the rollout should finish.
   */
  public boolean finishRollout(StateObservationMulti rollerState, int depth)
  {
    //Did we reach the rollout's maximum depth?
    if(depth >= Agent.ROLLOUT_DEPTH)
      return true;

    //Is the game over?
    if(rollerState.isGameOver())               //end of game
      return true;

    return false;
  }

  /**
   * Backs up the result to update the statistics of all ndoes.
   * @param node node to update the statistics of.
   * @param result result (Value of the state reached at the end of the rollout).
   */
  public void backUp(SingleTreeNode node, double result)
  {
    SingleTreeNode n = node;
    while(n != null)
    {
      //Update stats (number of visit and accumulated value)
      n.nVisits++;
      n.totValue += result;

      //Go to this node's parent.
      n = n.parent;
    }
  }


  /**
   * A possible recommendation policy: returns the action from this node with the highest number of visits.
   * @return the index of the action chosen.
   */
  public int mostVisitedAction() {
    int selected = -1;
    double bestValue = -Double.MAX_VALUE;
    boolean allEqual = true;
    double first = -1;

    //Find that agent with most visits.
    for (int i=0; i<children.length; i++) {

      if(children[i] != null)
      {
        if(first == -1)
          first = children[i].nVisits;
        else if(first != children[i].nVisits)
        {
          allEqual = false;
        }

        double childValue = children[i].nVisits;
        //break ties randomly
        childValue = Utils.noise(childValue, epsilon, m_rnd.nextDouble());
        if (childValue > bestValue) {
          bestValue = childValue;
          selected = i;
        }
      }
    }

    if (selected == -1)
    {
      //Mmm, no selection. Just pick the first, but this shouldn't happen.
      selected = 0;
    }else if(allEqual)
    {
      //If all are equal, we opt to choose for the one with the best Q.
      selected = bestAction();
    }
    return selected;
  }

  /**
   * A possible recommendation policy: returns the action from this node with the highest average of rewards
   * @return the index of the action chosen.
   */
  public int bestAction()
  {
    int selected = -1;
    double bestValue = -Double.MAX_VALUE;

    for (int i=0; i<children.length; i++) {

      if(children[i] != null) {
        //double tieBreaker = m_rnd.nextDouble() * epsilon;
        double childValue = children[i].totValue / (children[i].nVisits + epsilon);
        //to break ties randomly
        childValue = Utils.noise(childValue, epsilon, m_rnd.nextDouble());
        if (childValue > bestValue) {
          bestValue = childValue;
          selected = i;
        }
      }
    }

    if (selected == -1)
    {
      System.out.println("Unexpected selection!");
      selected = 0;
    }

    return selected;
  }

  /**
   * Checks if this node is fully expanded or not.
   * @return true if NOT fully expanded.
   */
  public boolean notFullyExpanded() {
    for (SingleTreeNode tn : children) {
      if (tn == null) {
        //It wont' be fully expanded if one of the children is still NULL.
        return true;
      }
    }

    return false;
  }
}