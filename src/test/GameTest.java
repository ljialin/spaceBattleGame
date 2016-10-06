package test;

import competition.CompetitionParameters;
import core.game.StateObservationMulti;
import core.player.AbstractMultiPlayer;
import tools.ElapsedCpuTimer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Jialin Liu on 05/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class GameTest {

  static String doNothingController = "controllers.doNothing.Agent";
  static String olmctsController = "controllers.sampleOLMCTS.Agent";
  static String humanArrows = "controllers.humanArrows.Agent";
  static String humanWSAD = "controllers.humanWSAD.Agent";
  static String randomController = "controllers.sampleRandom.Agent";
  static String OneStepLookAheadController = "controllers.sampleOneStepLookAhead.Agent";
  static String GAController = "controllers.sampleGA.Agent";

  public static void main(String[] args) {
    playOne();
  }

  public static void playOne()
  {
    boolean visuals = true;
    StateObservationMulti game = new StateObservationMulti(visuals);
    AbstractMultiPlayer[] players = new AbstractMultiPlayer[2];
    players[0] = createMultiPlayer(olmctsController, game, 3, 0, false);
    players[1] = createMultiPlayer(GAController, game, 3, 1, false);

    double[][] res = game.playGame(players, 3);
  }

  private static void dump(double[][] results, String filename)
  {
    try {

      BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename)));

      for (int i = 0; i < results.length; ++i) {
        for (int j = 0; j < results[i].length; ++j) {
          writer.write(results[i][j] + ",");
        }
        writer.write("\n");
      }

      writer.close();

    }catch(Exception e)
    {
      System.out.println("MEH: " + e.toString());
      e.printStackTrace();
    }
  }

  protected static AbstractMultiPlayer createOLMCTSController(
     int playerID, StateObservationMulti so) {
    ElapsedCpuTimer ect = new ElapsedCpuTimer(CompetitionParameters.TIMER_TYPE);
    ect.setMaxTimeMillis(CompetitionParameters.INITIALIZATION_TIME);
    AbstractMultiPlayer abstractMultiPlayer = new controllers.sampleOLMCTS.Agent(so.copy(), ect.copy(), playerID);
    return abstractMultiPlayer;
  }

  private static AbstractMultiPlayer createMultiPlayer(
      String playerName, StateObservationMulti so,
      int randomSeed, int id, boolean isHuman)
  {
    AbstractMultiPlayer player = null;

    try{
      //create the controller.
      player = (AbstractMultiPlayer) createController(playerName, id, so);
      if(player != null) {
        player.setup(randomSeed, isHuman);
      }

    }catch (Exception e)
    {
      //This probably happens because controller took too much time to be created.
      e.printStackTrace();
      System.exit(1);
    }

    return player;
  }


  protected static AbstractMultiPlayer createController(String playerName, int playerID,
                                                        StateObservationMulti so) throws RuntimeException
  {
    AbstractMultiPlayer abstractMultiPlayer = null;
    try
    {
      //Determine the time due for the controller creation.
      ElapsedCpuTimer ect = new ElapsedCpuTimer(CompetitionParameters.TIMER_TYPE);
      ect.setMaxTimeMillis(CompetitionParameters.INITIALIZATION_TIME);
      if (so.no_players < 2) { //single player
        //Get the class and the constructor with arguments (StateObservation, long).
        Class<? extends AbstractMultiPlayer> controllerClass = Class.forName(playerName).asSubclass(AbstractMultiPlayer.class);
        Class[] gameArgClass = new Class[]{StateObservationMulti.class, ElapsedCpuTimer.class};
        Constructor controllerArgsConstructor = controllerClass.getConstructor(gameArgClass);

        //Call the constructor with the appropriate parameters.
        Object[] constructorArgs = new Object[]{so, ect.copy()};

        abstractMultiPlayer = (AbstractMultiPlayer) controllerArgsConstructor.newInstance(constructorArgs);
        abstractMultiPlayer.setPlayerID(playerID);

      } else { //multi player
        //Get the class and the constructor with arguments (StateObservation, long, int).
        Class<? extends AbstractMultiPlayer> controllerClass = Class.forName(playerName).asSubclass(AbstractMultiPlayer.class);
        Class[] gameArgClass = new Class[]{StateObservationMulti.class, ElapsedCpuTimer.class, int.class};
        Constructor controllerArgsConstructor = controllerClass.getConstructor(gameArgClass);

        //Call the constructor with the appropriate parameters.
        Object[] constructorArgs = new Object[]{(StateObservationMulti)so.copy(), ect.copy(), playerID};

        abstractMultiPlayer = (AbstractMultiPlayer) controllerArgsConstructor.newInstance(constructorArgs);
        abstractMultiPlayer.setPlayerID(playerID);
      }
      //Check if we returned on time, and act in consequence.
      long timeTaken = ect.elapsedMillis();
      if (ect.exceededMaxTime()) {
        long exceeded = -ect.remainingTimeMillis();
        System.out.println("Controller initialization time out (" + exceeded + ").");

        return null;
      }
      else
      {
        System.out.println("Controller initialization time: " + timeTaken + " ms.");
      }

      //This code can throw many exceptions (no time related):

    }catch(NoSuchMethodException e)
    {
      e.printStackTrace();
      System.err.println("Constructor " + playerName + "(StateObservation,long) not found in controller class:");
      System.exit(1);

    }catch(ClassNotFoundException e)
    {
      System.err.println("Class " + playerName + " not found for the controller:");
      e.printStackTrace();
      System.exit(1);

    }catch(InstantiationException e)
    {
      System.err.println("Exception instantiating " + playerName + ":");
      e.printStackTrace();
      System.exit(1);

    }catch(IllegalAccessException e)
    {
      System.err.println("Illegal access exception when instantiating " + playerName + ":");
      e.printStackTrace();
      System.exit(1);
    }catch(InvocationTargetException e)
    {
      System.err.println("Exception calling the constructor " + playerName + "(StateObservation,long):");
      e.printStackTrace();
      System.exit(1);
    }

    return abstractMultiPlayer;
  }
}
