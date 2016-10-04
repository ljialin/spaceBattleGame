package core.game;

import competition.CompetitionParameters;
import core.player.Player;
import core.termination.Termination;
import ontology.Types;
import ontology.asteroids.GameObject;
import ontology.asteroids.Ship;
import ontology.asteroids.View;
import tools.JEasyFrame;
import tools.KeyHandler;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
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
public class Game {
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
   * Screen size.
   */
  protected Dimension screenSize;

  /**
   * Dimensions of the game.
   */
  protected Dimension size;

  /**
   * Game tick
   */
  protected int gameTick;

  /**
   * Indicates if the game is ended.
   */
  protected boolean isEnded;

  /**
   * State observation for this game.
   */
  protected ForwardModel fwdModel;

  /**
   * Random number generator for this game. It can only be received when the game is started.
   */
  private Random random;

  /**
   * Key Handler for human play. The default is CompetitionParameters.KEY_INPUT
   */
  public String key_handler;

  public static KeyHandler ki;

  /**
   * Avatars last actions.
   * Array for all avatars in the game.
   * Index in array corresponds to playerID.
   */
  protected Types.ACTIONS[] avatarLastAction;

  public int no_players = 2; //default to two player


  public Game() {
    for (int i=0; i<this.no_players; i++) {
      this.avatars[i] = new Ship();
    }
  }

  public void reset() {
    this.gameTick = -1;
    this.isEnded = false;

    this.avatarLastAction = new Types.ACTIONS[this.no_players];
    for (int i = 0; i < no_players; i++) {
      avatarLastAction[i] = Types.ACTIONS.ACTION_NIL;
    }

    this.avatars = new Ship[this.no_players];
    for (int i=0; i<no_players; i++) {
      this.avatars[i] = new Ship();
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

  public void _updateCollisionDict(GameObject object) {
    ////// TODO: 04/10/2016
  }

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
    for (int i=0; i<no_players; i++) {
      this.avatars[i] = new Ship();
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
          avatars[i].player = players[i];
          avatars[i].playerId = i;
        } else {
          System.out.println("Null player.");
        }
      }
    } else {
      System.out.println("Not enough players.");
    }
  }

  /**
   * Starts the forward model for the game.
   */
  public void initForwardModel()
  {
    fwdModel = new ForwardModel(this);
    fwdModel.update(this);
  }

  /**
   * Initializes some variables for the game to be played, such as
   * the game tick, sampleRandom number generator, forward model and assigns
   * the player to the avatar.
   * @param players Players that play this game.
   * @param randomSeed sampleRandom seed for the whole game.
   */
  private void prepareGame(Player[] players, int randomSeed, int humanID)
  {
    //Start tick counter.
    gameTick = -1;

    //Create the sampleRandom generator.
    random = new Random(randomSeed);

    //Assigns the player to the avatar of the game.
    createAvatars();
    assignPlayer(players);
    if(humanID != -1) {
      this.avatars[humanID] = new H
    }

    //Initialize state observation (sets all non-volatile references).
    initForwardModel();
  }

  /**
   * Plays the game, graphics enabled.
   * @param players Players that play this game.
   * @param randomSeed sampleRandom seed for the whole game.
   * @param humanID ID of the human player
   * @return the score of the game played.
   */
  public double[] playGame(Player[] players, int randomSeed, int humanID) {
    prepareGame(players, randomSeed, humanID);
    for (int i=0; i<this.no_players; i++) {
      this.avatars[i].setPlayerId(i);
      this.avatars[i].setPlayer();
    }
    this.p1 = p1;
    this.p2 = p2;
    reset(true);

    if (p1 instanceof KeyListener) {
      view.addKeyListener((KeyListener)p1);
      view.setFocusable(true);
      view.requestFocus();
    }

    if (p2 instanceof KeyListener) {
      view.addKeyListener((KeyListener)p2);
      view.setFocusable(true);
      view.requestFocus();
    }

    waitTillReady();
    while (!isGameOver()) {

      update();
      //System.out.println("current time: " + currentTick);
      //System.out.println("player 1: " + this.stats.get(0).life);
      //System.out.println("player 2: " + this.stats.get(1).life);
    }

    //update();
    if(this.winner == -1) {
      if (stats.get(0).nPoints > stats.get(1).nPoints)
        this.winner = 0;
      else if (stats.get(0).nPoints < stats.get(1).nPoints)
        this.winner = 1;
    }
    if(this.winner!=-1) {
      System.out.println("Player " + (this.winner+1) + " wins at " + currentTick + " with life " + stats.get(this.winner).life + " fired " + stats.get(this.winner).getMissilesFired() + " points " + stats.get(this.winner).nPoints);
      System.out.println("Player " + (2-this.winner) + " loss at " + currentTick + " with life " + stats.get(1-this.winner).life  + " fired " + stats.get(1-this.winner).getMissilesFired() + " points " + stats.get(1-this.winner).nPoints);
    } else {
      System.out.println("Player 1 draws at " + currentTick + " with life " + stats.get(0).life + " with life " + stats.get(0).life + " fired " + stats.get(0).getMissilesFired() + " points " + stats.get(0).nPoints);
      System.out.println("Player 2 draws at " + currentTick + " with life " + stats.get(1).life + " with life " + stats.get(1).life + " fired " + stats.get(1).getMissilesFired() + " points " + stats.get(1).nPoints);
    }

    if (p1 instanceof KeyListener) {
      view.removeKeyListener((KeyListener)p1);
    }
    if (p2 instanceof KeyListener) {
      view.removeKeyListener((KeyListener)p2);
    }

    double[] tmp = Util.combineArray(scoreRecord,score1Record);
    double[] allRecord = Util.combineArray(tmp,score2Record);
    return allRecord;
  }

  /**
   * Sets the title of the game screen, depending on the game ending state.
   * @param frame The frame whose title needs to be set.
   */
  private void setTitle (JEasyFrame frame)
  {
    String sb = "";
    sb += "Java-VGDL: ";
    for (int i = 0; i < no_players; i++) {
      sb += "Player" + i + "-Score:" + avatars[i].getScore() + ". ";
    }
    sb += "Tick:" + this.getGameTick();

//        sb += " --Counter:";
//        for (int i = 0; i < no_counters; i++) {
//            sb += counter[i] + ", ";
//        }

    if(!isEnded)
      frame.setTitle(sb);
    else {
      for (int i = 0; i < no_players; i++) {
        if (avatars[i].getWinState() == Types.WINNER.PLAYER_WINS)
          sb += " [Player " + i + " WINS!]";
        else
          sb += " [Player " + i + " LOSES!]";
      }
    }

    frame.setTitle(sb);

  }

  public void draw(Graphics2D g) {
    // for (Object ob : objects)
    if (s1 == null || s2 == null) {
      return;
    }

    // System.out.println("In draw(): " + n);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON);
    g.setColor(bg);
    g.fillRect(0, 0, size.width, size.height);

    if(!objects.isEmpty()) {
      GameObject[] objectsCopy = objects.toArray(new GameObject[objects.size()]);
      for (GameObject go : objectsCopy) {
        go.draw(g);
      }
    }

    s1.draw(g);
    if (p1 instanceof RenderableBattleController) {
      RenderableBattleController rbc = (RenderableBattleController)p1;
      rbc.render(g, s1.copy());
    }

    s2.draw(g);
    if (p2 instanceof RenderableBattleController) {
      RenderableBattleController rbc = (RenderableBattleController)p2;
      rbc.render(g, s2.copy());
    }

    p1.draw(g);
    p2.draw(g);

    if(BattleTest.SHOW_ROLLOUTS)
    {
      //waitStep(5000);
    }


  }

}
