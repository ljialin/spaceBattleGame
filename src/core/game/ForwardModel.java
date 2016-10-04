package core.game;

import ontology.Constants;
import ontology.Types;
import ontology.asteroids.GameObject;
import ontology.asteroids.Ship;
import tools.KeyHandler;
import tools.Vector2d;

import java.awt.*;
import java.util.*;

/**
 * Created by Jialin Liu on 04/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class ForwardModel extends Game {
  /**
   * Private sampleRandom generator. Rolling the state forward from this state
   * observation will use this sampleRandom generator, different from the one
   * that is used in the real game.
   */
  private Random randomObs;

  /**
   * Constructor for StateObservation. Initializes everything
   * @param a_gameState
   */
  public ForwardModel(Game a_gameState)
  {
    //All static elements of the game are assigned from the game we create the copy from.
    initNonVolatile(a_gameState);

    //Init those variables that take a determined value at the beginning of a game.
    init();
  }


  /**
   * Dumps the game state into 'this' object. Effectively, creates a state observation
   * from a game state (of class Game).
   * @param a_gameState game to take the state from.
   */
  final public void update(Game a_gameState)
  {
    avatars = new Ship[this.no_players];
    for(int i = 0; i < avatars.length; ++i)
    {
      avatars[i] = a_gameState.getAvatars()[i].copy();
    }

    objects = new ArrayList<GameObject>();
    for(GameObject ob : a_gameState.getObjects())
    {
      objects.add(ob.copy());
    }

    this.gameTick = a_gameState.gameTick;
    this.isEnded = a_gameState.isEnded;
    this.avatarLastAction = new Types.ACTIONS[no_players];
    System.arraycopy(a_gameState.avatarLastAction, 0, avatarLastAction, 0, no_players);
  }

  /**
   * Initializes the variables of this game that have always a determined value at the beginning
   * of any game.
   */
  private void init()
  {
    this.randomObs = new Random();
    this.gameTick = 0;
    this.isEnded = false;
  }

  /**
   * Initializes the non volatile elements of a game (constructors, termination conditions,
   * effects, etc). 'this' takes these from a_gameState,
   * @param a_gameState Reference to the original game
   */
  private void initNonVolatile(Game a_gameState)
  {
    this.terminations = a_gameState.terminations;
    this.resources_limits = a_gameState.resources_limits;
    this.screenSize = a_gameState.screenSize;
    this.size = a_gameState.size;
    this.no_players = a_gameState.no_players;
    this.avatarLastAction = new Types.ACTIONS[no_players];
    System.arraycopy(a_gameState.avatarLastAction, 0, avatarLastAction, 0, no_players);
    for (int i = 0; i < no_players; i++) {
      avatars[i] = (Ship) a_gameState.avatars[i].copy();
      avatars[i].setKeyHandler(a_gameState.avatars[i].getKeyHandler());
    }
  }


  /**
   * Returns the sampleRandom generator of this forward model. It is not the same as the
   * sampleRandom number generator of the main game copy.
   * @return the sampleRandom generator of this forward model.
   */
  final public Random getRandomGenerator()
  {
    return randomObs;
  }

  /**
   * Sets a new seed for the forward model's random generator (creates a new object)
   *
   * @param seed the new seed.
   */
  public void setNewSeed(int seed)
  {
    randomObs = new Random(seed);
  }

  /************** Useful functions for the agent *******************/

  /**
   * Method used to access the number of players in a game.
   * @return number of players.
   */
  public int getNoPlayers() {
    return no_players;
  }

  /**
   * Calls update(this) in avatar sprites. It uses the action received as the action of the avatar.
   * Doesn't update disabled avatars.
   * @param action Action to be performed by the avatar for this game tick.
   */
  protected void updateAvatars(Types.ACTIONS action, int playerID)
  {
    Ship a = avatars[playerID];
    KeyHandler ki = a.getKeyHandler();
    ki.reset(playerID);
    ki.setAction(action, a.getPlayerID());

    //apply action to correct avatar
    //// TODO: 04/10/2016
  }

  /**
   * Performs one tick for the game, calling update(this) in all sprites.
   * It follows the same order of update calls as in the real game (inverse spriteOrder[]).
   * Doesn't update disabled sprites.
   */
  protected void tick() {
    // TODO: 04/10/2016
  }


  /**
   * Advances the forward model using the action supplied.
   * @param action
   */
  final public void advance(Types.ACTIONS action) {
    if(!isEnded) {
      //apply player action
      updateAvatars(action, 0);
      //update all the other sprites
      tick();
      //update game state
      advance_aux();
    }
  }

  /**
   * Advances the forward model using the actions supplied.
   * @param actions array of actions of all players (index in array corresponds
   *                to playerID).
   */
  final public void advance(Types.ACTIONS[] actions) {

    if(!isEnded) {
      //apply actions of all players
      for (int i = 0; i < actions.length; i++) {
        Types.ACTIONS a = actions[i]; // action
        updateAvatars(a, i); // index in array actions is the playerID
      }
      //update all other sprites in the game
      tick();
      //update game state
      advance_aux();
    }
    //System.out.println(isMultiGameOver());
  }

  /**
   * Auxiliary method for advance methods, to avoid code duplication.
   */
  private void advance_aux() {
    // TODO: 04/10/2016
    gameTick++;
  }

  /**
   * Creates a copy of this forward model.
   * @return the copy of this forward model.
   */
  final public ForwardModel copy() {
    ForwardModel copyObs = new ForwardModel(this);
    copyObs.update(this);
    return copyObs;
  }

  /**
   * Gets the game score of this state.
   * @return the game score.
   */
  public double getGameScore() { return this.avatars[0].getScore(); }

  /**
   * Method overloaded for multi player games.
   * Gets the game score of a particular player (identified by playerID).
   * @param playerID ID of the player to query.
   * @return the game score.
   */
  public double getGameScore(int playerID) { return this.avatars[playerID].getScore(); }

  /**
   * Gets the current game tick of this particular state.
   * @return the game tick of the current game state.
   */
  public int getGameTick() { return this.gameTick; }

  /**
   * Indicates if there is a game winner in the current observation.
   * Possible values are Types.WINNER.PLAYER_WINS, Types.WINNER.PLAYER_LOSES and
   * Types.WINNER.NO_WINNER.
   * @return the winner of the game.
   */
  public Types.WINNER getGameWinner() { return this.avatars[0].getWinState(); }

  /**
   * Method overloaded for multi player games.
   * Indicates if there is a game winner in the current observation.
   * Possible values are Types.WINNER.PLAYER_WINS, Types.WINNER.PLAYER_LOSES and
   * Types.WINNER.NO_WINNER.
   * @return the winner of the game.
   */
  public Types.WINNER[] getMultiGameWinner() {
    Types.WINNER[] winners = new Types.WINNER[no_players];
    for (int i = 0; i < no_players; i++) {
      winners[i] = avatars[i].getWinState();
    }
    return winners; }

  /**
   * Indicates if the game is over or if it hasn't finished yet.
   * @return true if the game is over.
   */
  public boolean isGameOver() { return getGameWinner() != Types.WINNER.NO_WINNER; }

  /**
   * Indicates if the game is over or if it hasn't finished yet.
   * @return true if the game is over.
   */
  public boolean isMultiGameOver() {
    for (int i = 0; i < no_players; i++) {
      if (getMultiGameWinner()[i] == Types.WINNER.NO_WINNER) return false;
    }
    return true;
  }

  /**
   * Returns the world dimensions, in pixels.
   * @return the world dimensions, in pixels.
   */
  public Dimension getWorldDimension()
  {
    return screenSize;
  }


  /** avatar-dependent functions **/

  /**
   * Returns the position of the avatar. If the game is finished, we cannot guarantee that
   * this position reflects the real position of the avatar (the avatar itself could be
   * destroyed). If game finished, this returns Types.NIL.
   * @return position of the avatar, or Types.NIL if game is over.
   */
  public Vector2d getAvatarPosition() { return getAvatarPosition(0); }

  /**
   * Method overloaded for multi player games.
   * @param playerID ID of the player to query.
   */
  public Vector2d getAvatarPosition(int playerID) {
    if(isEnded)
      return Types.NIL;
    return avatars[playerID].getPosition();
  }

  /**
   * Returns the speed of the avatar. If the game is finished, we cannot guarantee that
   * this speed reflects the real speed of the avatar (the avatar itself could be
   * destroyed). If game finished, this returns 0.
   * @return orientation of the avatar, or 0 if game is over.
   */
  public Vector2d getAvatarVelocity() { return avatars[0].velocity; }

  /**
   * Method overloaded for multi player games.
   * @param playerID ID of the player to query.
   */
  public Vector2d getAvatarVelocity(int playerID) {
    if(isEnded)
      return new Vector2d(true);
    return avatars[playerID].velocity;
  }

  /**
   * Returns the orientation of the avatar. If the game is finished, we cannot guarantee that
   * this orientation reflects the real orientation of the avatar (the avatar itself could be
   * destroyed). If game finished, this returns Types.NIL.
   * @return orientation of the avatar, or Types.NIL if game is over.
   */
  public Vector2d getAvatarOrientation() { return getAvatarOrientation(0); }

  /**
   * Method overloaded for multi player games.
   * @param playerID ID of the player to query.
   */
  public Vector2d getAvatarOrientation(int playerID) {
    if(isEnded)
      return Types.NIL;
    return new Vector2d(avatars[playerID].dir.x, avatars[playerID].dir.y);
  }

  /**
   * Returns the actions that are available in this game for
   * the avatar. If the parameter 'includeNIL' is true, the array contains the (always available)
   * NIL action. If it is false, this is equivalent to calling getAvailableActions().
   * @param includeNIL true to include Types.ACTIONS.ACTION_NIL in the array of actions.
   * @return the available actions.
   */
  public ArrayList<Types.ACTIONS> getAvatarActions(boolean includeNIL) { return getAvatarActions(0, includeNIL); }

  /**
   * Method overloaded for multi player games.
   * @param playerID ID of the player to query.
   */
  public ArrayList<Types.ACTIONS> getAvatarActions(int playerID, boolean includeNIL) {
    if(isEnded)
      return new ArrayList<Types.ACTIONS>();
    if(includeNIL)
      return avatars[playerID].actionsNIL;
    return avatars[playerID].actions;
  }

  /**
   * Returns the resources in the avatar's possession. As there can be resources of different
   * nature, each entry is a key-value pair where the key is the resource ID, and the value is
   * the amount of that resource type owned. It should be assumed that there might be other resources
   * available in the game, but the avatar could have none of them.
   * If the avatar has no resources, an empty HashMap is returned.
   * @return resources owned by the avatar.
   */
  public HashMap<Integer, Integer> getAvatarResources() { return getAvatarResources(0); }

  /**
   * Method overloaded for multi player games.
   * @param playerID ID of the player to query.
   */
  public HashMap<Integer, Integer> getAvatarResources(int playerID) {
    //Determine how many different resources does the avatar have.
    HashMap<Integer, Integer> owned = new HashMap<Integer, Integer>();

    if(avatars[playerID] == null)
      return owned;

    //And for each type, add their amount.
    Set<Map.Entry<Integer, Integer>> entries = avatars[playerID].resources.entrySet();
    for(Map.Entry<Integer, Integer> entry : entries)
    {
      owned.put(entry.getKey(), entry.getValue());
    }

    return owned;
  }

  /**
   * Returns the avatar's last move. At the first game cycle, it returns ACTION_NIL.
   * Note that this may NOT be the same as the last action given by the agent, as it may
   * have overspent in the last game cycle.
   * @return the action that was executed in the real game in the last cycle. ACTION_NIL
   * is returned in the very first game step.
   */
  public Types.ACTIONS getAvatarLastAction() { return getAvatarLastAction(0); }

  /**
   * Method overloaded for multi player games.
   * @param playerID ID of the player to query.
   */
  public Types.ACTIONS getAvatarLastAction(int playerID) {
    if(avatarLastAction[playerID] != null)
      return avatarLastAction[0];
    else return Types.ACTIONS.ACTION_NIL;
  }

  /**
   * Returns the health points of the avatar. A value of 0 doesn't necessarily
   * mean that the avatar is dead (could be that no health points are in use in that game).
   * @return a numeric value, the amount of remaining health points.
   */
  public int getAvatarHealthPoints() { return getAvatarHealthPoints(0); }

  /**
   * Method overloaded for multi player games.
   * @param playerID ID of the player to query.
   */
  public int getAvatarHealthPoints(int playerID) { return avatars[playerID].healthPoints; }


  /**
   * Returns the maximum amount of health points.
   * @return the maximum amount of health points the avatar can have.
   */
  public int getAvatarMaxHealthPoints() { return getAvatarMaxHealthPoints(0); }

  /**
   * Method overloaded for multi player games.
   * @param playerID ID of the player to query.
   */
  public int getAvatarMaxHealthPoints(int playerID) { return Constants.MAX_HEALTH_POINTS; }
  /**
   * Returns true if the avatar is alive
   * @return true if the avatar is alive
   */
  public boolean isAvatarAlive()
  {
    return isAvatarAlive(0);
  }

  /**
   * Method overloaded for multi player games, returns true if the avatar is still alive.
   *
   * @param playerID ID of the player to query.
   */
  public boolean isAvatarAlive(int playerID) {
    return !avatars[playerID].isDead();
  }
}