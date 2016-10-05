package core.game;

import competition.CompetitionParameters;
import core.player.Player;
import core.termination.Termination;
import ontology.Constants;
import ontology.Types;
import ontology.asteroids.GameObject;
import ontology.asteroids.Ship;
import ontology.asteroids.View;
import tools.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.ArrayList;
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

  /**
   * Key Handler for human play. The default is CompetitionParameters.KEY_INPUT
   */
  public String key_handler;

  public static KeyHandler ki;

  public boolean visible;

  public View view;

  /**
   * Avatars last actions.
   * Array for all avatars in the game.
   * Index in array corresponds to playerID.
   */
  protected Types.ACTIONS[] avatarLastAction;

  public int no_players = 2; //default to two player


  public StateObservationMulti() {
    reset();
  }

  public void reset() {
    setParams();
    createAvatars();
    resetLastAction();
  }

  public void setParams() {
    this.gameTick = -1;
    this.isEnded = false;
    this.visible = false;
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
   * Returns the game score.
   */
  public double getScore() { return getScore(0); }

  /**
   * Method overloaded for multi player games.
   * Returns the game score of the specified player.
   * @param playerID ID of the player.
   */
  public double getScore(int playerID)
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
    }
  }

  /**
   * Looks for the avatar of the game in the existing sprites. If the player
   * received as a parameter is not null, it is assigned to it.
   * @param players the players that will play the game (only 1 in single player games).
   */
  private void assignPlayer(Player[] players)
  {
    //iterate through all avatars and assign their players
    if (players.length == no_players) {
      for (int i = 0; i < no_players; i++) {
        if (players[i] != null) {
          this.avatars[i].player = players[i];
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
   * @param players Players that play this game.
   * @param randomSeed sampleRandom seed for the whole game.
   */
  private void prepareGame(Player[] players, int randomSeed)
  {
    //Start tick counter.
    gameTick = -1;

    //Create the sampleRandom generator.
    random = new Random(randomSeed);

    //Assigns the player to the avatar of the game.
    createAvatars();
    assignPlayer(players);
  }

  /**
   * Plays the game, graphics enabled.
   * @param players Players that play this game.
   * @param randomSeed sampleRandom seed for the whole game.
   * @return the score of the game played.
   */
  public double[] playGame(Player[] players, int randomSeed) {
    prepareGame(players, randomSeed);

    view = new View(this);

    for (int i=0; i<no_players; i++) {
      if (players[i] instanceof KeyListener) {
        view.addKeyListener((KeyListener) players[i]);
        view.setFocusable(true);
        view.requestFocus();
        this.visible = true;
      }
    }

    waitTillReady();

    while (!isEnded) {
      update();
    }

    for (int i=0; i<no_players; i++) {
      if (players[i] instanceof KeyListener) {
        view.removeKeyListener((KeyListener) players[i]);
      }
    }

    double[] res = new double[no_players];
// TODO: 04/10/16
    return res;
  }

  public void update() {
    // TODO: 04/10/16 checkMissile
    ElapsedCpuTimer elapsedTimer = new ElapsedCpuTimer();
    elapsedTimer.setMaxTimeMillis(CompetitionParameters.ACTION_TIME);

    Types.ACTIONS[] actions = new Types.ACTIONS[no_players];
    for (int i=0; i<no_players; i++) {
      actions[i] = this.avatars[i].player.act(this, elapsedTimer);
    }

    update(actions);

    for (GameObject ob : objects) {
      ob.update();
    }
    removeDead();

    int[] reducedHealthPoints = checkCollision();
    for (int i=0; i<no_players; i++) {
      avatars[i].healthPoints -= reducedHealthPoints[i];
      if (avatars[i].healthPoints <= 0) {
        this.avatars[i].kill();
        this.avatars[i].setWinState(Types.WINNER.PLAYER_LOSES);
      }
    }

    for (int i=0; i<this.objects.size(); i++) {
      if (reducedHealthPoints[i+no_players] <= 0) {
        this.objects.get(i).kill();
      }
    }
    removeDead();
  }

  public void update(Types.ACTIONS[] actions) {
    for (Types.ACTIONS action: actions) {
      if (action.equals(Types.ACTIONS.ACTION_FIRE)) {
        // TODO: 04/10/16
      } else {
        // TODO: 04/10/16
      }
    }

    // now apply them to the ships
    for (int i=0; i<no_players; i++) {
      avatars[i].update(actions[i]);
      wrap(avatars[i]);
    }

    gameTick++;

    Ship[] cloneAvatars = new Ship[no_players];
    for (int i=0; i<no_players; i++) {
      cloneAvatars[i] = avatars[i].copy();
    }

    if (visible) {
      view.repaint();
      sleep();
    }
  }

  protected void removeDead() {
    for(int i=objects.size()-1; i>1; i--) {
      GameObject ob = objects.get(i);
      if(ob.isDead())
        objects.remove(i);
    }
  }

  protected int[] checkCollision() {
    int[] reducedHealthPoints = new int[no_players+this.objects.size()];
    reducedHealthPoints = checkAvatarCollision(reducedHealthPoints);
    reducedHealthPoints = checkAvatarObjectCollision(reducedHealthPoints);
    reducedHealthPoints = checkObjectCollision(reducedHealthPoints);
    return reducedHealthPoints;
  }

  protected int[] checkAvatarCollision(int[] reducedHealthPoints) {
    for (int i=0; i<no_players; i++) {
      for (int j=i+1; j<no_players; j++) {
        if (overlap(avatars[i], avatars[j])) {
          reducedHealthPoints[i] = reducedHealthPoints[i] + avatars[i].destructivePower;
          reducedHealthPoints[j] = reducedHealthPoints[j] + 1;
        }
      }
    }
    return reducedHealthPoints;
  }

  // One won't be hit by its weapons
  protected int[] checkAvatarObjectCollision(int[] reducedHealthPoints) {
    for (int i=0; i<no_players; i++) {
      for (int j=0; j<this.objects.size(); j++) {
        if (this.objects.get(j).getPlayerId() != i && overlap(avatars[i], this.objects.get(j))) {
          reducedHealthPoints[i] = reducedHealthPoints[i] + this.objects.get(j).destructivePower;
          reducedHealthPoints[j+no_players] = reducedHealthPoints[j+no_players] + avatars[i].destructivePower;
        }
      }
    }
    return reducedHealthPoints;
  }

  protected int[] checkObjectCollision(int[] reducedHealthPoints) {

    for (int i=0; i<this.objects.size(); i++) {
      for (int j = i + 1; j < this.objects.size(); j++) {
        if (this.objects.get(j).getPlayerId() != this.objects.get(j).getPlayerId()
            && overlap(this.objects.get(i), this.objects.get(j))) {
          reducedHealthPoints[i + no_players] = reducedHealthPoints[i + no_players] + this.objects.get(j).destructivePower;
          reducedHealthPoints[j + no_players] = reducedHealthPoints[j + no_players] + this.objects.get(i).destructivePower;
        }
      }
    }
    return reducedHealthPoints;
  }

  public void sleep() {
    try {
      Thread.sleep(CompetitionParameters.DELAY);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private boolean overlap(GameObject ob1, GameObject ob2) {
    double dist = ob1.getPosition().sqDist(ob2.getPosition());
    boolean ret = dist < (ob1.getRadius() + ob2.getRadius());
    return ret;
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
    // only wrap objects which are wrappable
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
    }

    if(Constants.SHOW_ROLLOUTS)
    {
      waitStep(5000);
    }

  }

}