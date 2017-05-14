package core.game;

import competition.CompetitionParameters;
import core.player.AbstractMultiPlayer;
import core.termination.Termination;
import ontology.Constants;
import ontology.Types;
import ontology.asteroids.Ship;
import ontology.asteroids.GameObject;
import ontology.asteroids.View;

import ontology.asteroids.Missile;

import tools.ElapsedCpuTimer;
import tools.JEasyFrame;
import tools.Vector2d;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;


/**
 * Created by Jialin Liu on 04/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 *
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class StateObservationMulti {
  /**
   * Termination set conditions to finish the game.
   */
  protected ArrayList<Termination> terminations;

  /**
   * Avatars in the game
   */
  protected Ship[] avatars;

  /**
   * Other objects, except avatars
   */
  protected ArrayList<GameObject> objects;
  /**
   * Limit number of each resource type
   */
  protected int[] resources_limits;

  /**
   * Color for each resource
   */
  protected Color[] resources_colors;

  /**
   * Game tick
   */
  protected int gameTick;

  /**
   * Indicates if the game is ended.
   */
  protected boolean isEnded;

  /**
   * Random number generator for this game. It can only be received when the game is started.
   */
  private Random random;

  public boolean visible;

  public View view;

  double scoreRecord[][];
  /**
   * Avatars last actions.
   * Array for all avatars in the game.
   * Index in array corresponds to playerID.
   */
  protected Types.ACTIONS[] avatarLastAction;

  public int no_players = 2; //default to two player

  static public int cheating = -1;


  public StateObservationMulti() {
    reset();
    this.visible = false;
  }

  public StateObservationMulti(boolean visible) {
    reset();
    this.visible = visible;
  }

  public void reset() {
    setParams();
    createAvatars();
    resetLastAction();
  }

  public void setParams() {
    scoreRecord = new double[no_players*2][CompetitionParameters.MAX_TIMESTEPS+1];
    this.gameTick = -1;
    this.isEnded = false;
    this.visible = true;
    this.objects = new ArrayList<GameObject>();
    this.terminations = new ArrayList<Termination>();
  }

  public void resetLastAction() {
    this.avatarLastAction = new Types.ACTIONS[this.no_players];
    for (int i = 0; i < no_players; i++) {
      avatarLastAction[i] = Types.ACTIONS.ACTION_NIL;
    }
  }

  public Ship[] getAvatars() {
    return avatars;
  }

  public ArrayList<GameObject> getObjects() {
    return this.objects;
  }

  public int getGameTick() {
    return gameTick;
  }

  /**
   * Returns the winner of this game. A value from Types.WINNER.
   * @return the winner of this game.
   */
  public Types.WINNER getWinner() {return getWinner(0);}

  /**
   * Overloaded method for multi player games.
   * Returns the win state of the specified player.
   * @param playerID ID of the player.
   * @return the win state of the specified player.
   */
  public Types.WINNER getWinner(int playerID) {return avatars[playerID].getWinState();}

  /**
   * Method overloaded for multi player games.
   * Returns the game score of the specified player.
   * @param playerID ID of the player.
   */
  public double getGameScore(int playerID)
  {
    return avatars[playerID].getScore();
  }

  /**
   * Method to create the array of avatars from the sprites.
   */
  public void createAvatars() {
    avatars = new Ship[no_players];
    double unit_x = Constants.WIDTH / (no_players + 1);
    double unit_y = Constants.HEIGHT / (no_players + 1);
    for (int i=0; i<no_players; i++) {
      Vector2d pos = new Vector2d(unit_x * (i + 1), unit_y * (i + 1));
      Vector2d dir = new Vector2d(0, (i%2)==0? 1 : -1);
      this.avatars[i] = new Ship(pos, dir, i);
//      System.out.println("ship id=" + i + " x="+ unit_x * (i + 1) + " y=" + unit_y * (i + 1));
//      System.out.println("Ship id " + this.avatars[i].getPlayerId() + " is created at ("
//          + this.avatars[i].getPosition().x + "," + this.avatars[i].getPosition().y + ")");
    }
  }

  /**
   * Looks for the avatar of the game in the existing sprites. If the player
   * received as a parameter is not null, it is assigned to it.
   * @param abstractMultiPlayers the players that will play the game (only 1 in single player games).
   */
  private void assignPlayer(AbstractMultiPlayer[] abstractMultiPlayers)
  {
    //iterate through all avatars and assign their players
    if (abstractMultiPlayers.length == no_players) {
      for (int i = 0; i < no_players; i++) {
        if (abstractMultiPlayers[i] != null) {
          this.avatars[i].player = abstractMultiPlayers[i];
          this.avatars[i].setPlayerId(i);
        } else {
          System.out.println("Null player.");
        }
      }
    } else {
      System.out.println("Not enough players.");
    }
  }

  /**
   * Initializes some variables for the game to be played, such as
   * the game tick, sampleRandom number generator, forward model and assigns
   * the player to the avatar.
   * @param abstractMultiPlayers Players that play this game.
   * @param randomSeed sampleRandom seed for the whole game.
   */
  private void prepareGame(AbstractMultiPlayer[] abstractMultiPlayers, int randomSeed)
  {
    //Start tick counter.
    gameTick = -1;

    //Create the sampleRandom generator.
    random = new Random(randomSeed);

    //Assigns the player to the avatar of the game.
    this.no_players = abstractMultiPlayers.length;
    //reset();
    assignPlayer(abstractMultiPlayers);
  }

  /**
   * Plays the game, graphics enabled.
   * @param abstractMultiPlayers Players that play this game.
   * @param randomSeed sampleRandom seed for the whole game.
   * @return the score of the game played.
   */
  public double[][] playGame(AbstractMultiPlayer[] abstractMultiPlayers, int randomSeed) {
    prepareGame(abstractMultiPlayers, randomSeed);
//    System.out.println("StateObservationMulti : game prepared !");

    if(visible) {
    view = new View(this);
    new JEasyFrame(view, "battle");

      view.repaint();
    }
//    System.out.println("StateObservationMulti : view created !");

    for (int i=0; i<no_players; i++) {
      if (avatars[i].player instanceof KeyListener) {
        view.addKeyListener((KeyListener) avatars[i].player);
        view.setFocusable(true);
        view.requestFocus();
        this.visible = true;
//        System.out.println("StateObservationMulti : Player i key added !");
      }
    }
//    System.out.println("StateObservationMulti : all key added !");

    waitTillReady();

//    System.out.println("GAME: " + this.avatars[0].getCooldown());

    while (!isEnded) {
//      System.out.println("StateObservationMulti : gameTick=" + gameTick);
      update();
    }
//    System.out.println("StateObservationMulti : end at gameTick=" + gameTick);
//    System.out.println("Final results:");

    if (scoreRecord != null) {
      for (int i=0; i<no_players; i++) {
        scoreRecord[2*i][CompetitionParameters.MAX_TIMESTEPS] = gameTick;
        scoreRecord[2*i+1][CompetitionParameters.MAX_TIMESTEPS] = gameTick;
      }
    }
    for (int i=0; i<no_players; i++) {
      if (this.avatars[i].getWinState() == Types.WINNER.PLAYER_LOSES) {
//        System.out.println("player " + i + " loses with score " + avatars[i].getScore()
//            + " points " + avatars[i].getPoints() + " at gameTick " + gameTick);
//        System.out.println("-1 " + avatars[i].getScore() + " " + gameTick);
      } else if (this.avatars[i].getWinState() == Types.WINNER.PLAYER_WINS) {
//        System.out.println("player " + i + " wins with score " + avatars[i].getScore()
//            + " points " +avatars[i].getPoints() + " at gameTick " + gameTick);
//        System.out.println("1 " + avatars[i].getScore() + " " + gameTick);
      } else {
//        System.out.println("player " + i + " draws with score " + avatars[i].getScore()
//            + " points " +avatars[i].getPoints() + " at gameTick " + gameTick);
//        System.out.println("0 " + avatars[i].getScore() + " " + gameTick);

      }
    }

    for (int i=0; i<no_players; i++) {
      if (avatars[i].player instanceof KeyListener) {
        view.removeKeyListener((KeyListener) avatars[i].player);
      }
    }
    return scoreRecord;
  }

  public void update() {
    ElapsedCpuTimer elapsedTimer = new ElapsedCpuTimer();
    elapsedTimer.setMaxTimeMillis(CompetitionParameters.ACTION_TIME);

    Types.ACTIONS[] actions = new Types.ACTIONS[no_players];
    for (int i=0; i<no_players; i++) {
      actions[i] = this.avatars[i].player.act(this.copy(), elapsedTimer);
    }

    advance(actions);

    if (scoreRecord != null) {
      for (int i=0; i<no_players; i++) {
        scoreRecord[2*i][gameTick] = getGameScore(i);
        scoreRecord[2*i+1][gameTick] = getPlayerPoints(i);
      }
    }

    if (gameTick + 1 >= CompetitionParameters.MAX_TIMESTEPS) {
      isEnded = true;
    }

  }

  public void advance(Types.ACTIONS[] actions) {
    for (int i=0; i<actions.length; i++) {
      if(actions[i] == null) {
        avatars[i].update(Types.ACTIONS.ACTION_NIL);
      } else {
        if (actions[i].equals(Types.ACTIONS.ACTION_FIRE)) {
          fireMissile(i, Constants.WEAPON_ID_MISSILE);
        } else {
          avatars[i].update(actions[i]);
        }
      }
      wrap(avatars[i]);
    }

    for (GameObject ob : objects) {
      ob.update();
      wrap(ob);
    }

    checkCollision();

    removeDead();

    for (int i=0; i<no_players; i++) {
      this.avatars[i].updatePoints();
    }

    gameTick++;

//    System.out.println("StateObservationMulti : gameTick=" + gameTick);
    if (visible) {
      view.repaint();
      sleep();
    }
  }

  protected void removeDead() {
    for(int i=objects.size()-1; i>=0; i--) {
      GameObject ob = objects.get(i);
      if(ob.isDead())
        objects.remove(i);
    }
  }

  protected void checkCollision() {
//    checkAvatarCollision();
    checkAvatarObjectCollision();
    checkObjectCollision();
  }

  /**
   * Priority collision check
   */
  protected void checkAvatarCollision() {
    int[] crash = new int[no_players];
    for (int i=0; i<no_players; i++) {
      for (int j=i+1; j<no_players; j++) {
        if (overlap(avatars[i], avatars[j])) {
          crash[i] = 1;
          crash[j] = 1;
        }
      }
    }
    int deadShips = 0;
    for (int i=0; i<crash.length; i++) {
      if (crash[i] == 1) {
        this.avatars[i].injured(Constants.MAX_HEALTH_POINTS);
        this.avatars[i].setWinState(Types.WINNER.PLAYER_LOSES);
        deadShips++;
      }
    }
    if (no_players == deadShips) {
      this.isEnded = true;
    }
  }

  // One won't be hit by its weapons
  protected void checkAvatarObjectCollision() {
    int[] usedWeapon = new int[this.objects.size()];
    for (int i=0; i<no_players; i++) {
      if (this.avatars[i].getWinState() != Types.WINNER.PLAYER_LOSES) {
        for (int j = 0; j < this.objects.size(); j++) {
          if (this.objects.get(j).getPlayerId() != i && overlap(avatars[i], this.objects.get(j))) {
            this.avatars[i].injured(objects.get(j).destructivePower);
            this.avatars[objects.get(j).getPlayerId()].kill();
            usedWeapon[j] = 1;
//            System.out.println(" ship " + i + " hitted by missile launched by ship " + objects.get(j).getPlayerId());
//            System.out.println(" ship " + i + " points " + this.avatars[i].getPoints());

          }
        }
      }
    }
    for (int i=usedWeapon.length-1; i>=0; i--) {
      if (usedWeapon[i] == 1) {
        objects.remove(i);
      }
    }
    checkShips();
  }

  protected void checkShips() {
    int deadShips = 0;
    for (int i=0; i<no_players; i++) {
      if (this.avatars[i].isDead()) {
        this.avatars[i].setWinState(Types.WINNER.PLAYER_LOSES);
        deadShips++;
      }
    }
    if (no_players - deadShips <= 1) {
      this.isEnded = true;
      checkWinner();
    }
  }

  protected void checkWinner() {
    if(this.isEnded) {
      for (int i = 0; i < no_players; i++) {
        if (!this.avatars[i].getWinState().equals(Types.WINNER.PLAYER_LOSES)) {
          this.avatars[i].setWinState(Types.WINNER.PLAYER_WINS);
        }
      }
    }
  }

  protected void checkObjectCollision() {
    int[] usedWeapon = new int[this.objects.size()];
    for (int i=0; i<this.objects.size(); i++) {
      for (int j = i + 1; j < this.objects.size(); j++) {
        if (this.objects.get(j).getPlayerId() != this.objects.get(j).getPlayerId()
            && overlap(this.objects.get(i), this.objects.get(j))) {
          usedWeapon[i] = 1;
          usedWeapon[j] = 1;
        }
      }
    }
    for (int i=usedWeapon.length-1; i>=0; i--) {
      if (usedWeapon[i] == 1) {
        objects.remove(i);
      }
    }
  }

  private boolean overlap(GameObject ob1, GameObject ob2) {
    double dist = Vector2d.dist(ob1.getPosition(), ob2.getPosition());
    boolean ret = dist < (ob1.getRadius() + ob2.getRadius());
    return ret;
  }

  protected void fireMissile(int playerId, int weaponId) {
    // need all the usual missile firing code here
    Ship currentShip = this.avatars[playerId];
    boolean fired = this.avatars[playerId].fireWeapon(weaponId);
//    System.out.println("Ship " + playerId + " fires ? " + fired);
    if(fired) {
      Missile m = new Missile(playerId, this.avatars[playerId].getPosition(), new Vector2d(0, 0));
      m.setVelocityByDir(this.avatars[playerId].getDirection());
      m.velocity.add(currentShip.velocity);
      m.setPlayerId(playerId);
      m.getPosition().add(m.velocity, (currentShip.getRadius() + m.getRadius()) * 1.5 / m.velocity.mag());
      this.objects.add(m);
      if(visible) {
//        System.out.println("Ship " + playerId + " fires missile id " + m.getPlayerId());
      }
    }
  }

  public boolean canFireWeapon(int playerId, int weaponId) {
    return this.avatars[playerId].canFireWeapon(weaponId);
  }

  public void sleep() {
    try {
      Thread.sleep(CompetitionParameters.DELAY);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public ArrayList<Types.ACTIONS> getAvailableActions(int playerId) {
    return Types.AVAILABLE_ACTIONS;
  }

  protected void waitTillReady()
  {
    if(visible)
    {
      while(!view.ready) {
        view.repaint();
        waitStep(1000);
      }
    }

    waitStep(1000);
  }

  /**
   * Waits until the next step.
   * @param duration Amount of time to wait for.
   */
  protected static void waitStep(int duration) {

    try
    {
      Thread.sleep(duration);
    }
    catch(InterruptedException e)
    {
      e.printStackTrace();
    }
  }

  private void wrap(GameObject ob) {
    /** only wrap objects which are wrappable */
    if (ob.isWrappable()) {
      double x = (ob.getPosition().x + Constants.WIDTH) % Constants.WIDTH;
      double y = (ob.getPosition().y + Constants.HEIGHT) % Constants.HEIGHT;

      ob.setPosition(x, y);
    }
  }

  public void draw(Graphics2D g) {
    for (Ship ship : avatars) {
      if (ship == null) {
        return;
      }
    }
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g.setColor(Types.BLACK);
    g.fillRect(0, 0, Constants.WIDTH, Constants.HEIGHT);

    if(!objects.isEmpty()) {
      GameObject[] objectsCopy = objects.toArray(new GameObject[objects.size()]);
      for (GameObject go : objectsCopy) {
        go.draw(g);
      }
    }

    for (Ship ship : avatars) {
      ship.draw(g);
//      System.out.println("Ship id " + ship.getPlayerId() + " is drawn at ("
//          + ship.getPosition().x + "," + ship.getPosition().y + ")");
    }

    if(Constants.SHOW_ROLLOUTS)
    {
      waitStep(10);
    }

  }

  public StateObservationMulti copy() {
    StateObservationMulti state = new StateObservationMulti(false);
    state.objects = copyObjects();
    state.avatars = copyAvatars();
    state.gameTick = gameTick;
    state.visible = false;
    return state;
  }

  protected ArrayList<GameObject> copyObjects() {
    ArrayList<GameObject> objectClone = new ArrayList<>();
    for (GameObject object : objects) {
      objectClone.add(object.copy());
    }

    return objectClone;
  }

  protected Ship[] copyAvatars() {
    Ship[] avatarClone = new Ship[no_players];
    for (int i=0; i<no_players; i++) {
      avatarClone[i] = avatars[i].copy();
    }

    return avatarClone;
  }

  public int getNoPlayers() {
    return this.no_players;
  }

  public boolean isGameOver() {
    return isEnded;
  }

  /**
   * Method overloaded for multi player games.
   * Indicates if there is a game winner in the current observation.
   * Possible values are Types.WINNER.PLAYER_WINS, Types.WINNER.PLAYER_LOSES and
   * Types.WINNER.NO_WINNER.
   * @return the winner of the game.
   */
  public Types.WINNER[] getMultiGameWinner() {
    checkWinner();
    Types.WINNER[] winners = new Types.WINNER[no_players];
    for (int i = 0; i < no_players; i++) {
      winners[i] = avatars[i].getWinState();
    }
    return winners;
  }

  public Vector2d getAvatarPosition(int playerId) {
    Vector2d avatarPosition = this.avatars[playerId].getPosition();
    return avatarPosition;
  }

  public ArrayList<StateObservationMulti>[] getPortalsPositions(Vector2d avatarPosition) {
    return null;
  }

  public ArrayList<StateObservationMulti>[] getNPCPositions() {
    return null;
  }

  public ArrayList<StateObservationMulti>[] getNPCPositions(Vector2d avatarPosition) {
    return null;
  }

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

  public double getGameScore() { return this.avatars[0].getScore(); }

  public double getPlayerPoints(int playerId) {
    return this.avatars[playerId].getPoints();
  }

  public int getAvatarLives(int playerId) {
    return this.avatars[playerId].getHealthPoints();
  }

  public double[][] getScoreRecord() {
    return this.scoreRecord;
  }
}