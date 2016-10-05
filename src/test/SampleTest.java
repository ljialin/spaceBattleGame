package test;

import competition.CompetitionParameters;
import controllers.doNothing.Agent;
import core.game.Game;
import core.game.StateObservation;
import core.game.StateObservationMulti;
import core.player.Player;
import ontology.asteroids.View;
import tools.ElapsedCpuTimer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Jialin Liu on 04/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 *
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class SampleTest {
  View view;
  String doNothingController = "controllers.multiPlayer.doNothing.Agent";

  public static void main(String[] args) {
  }

  public static void playOne(int ply1, int ply2)
  {
    boolean visuals = true;
    StateObservationMulti game = new StateObservationMulti();
    Player[] players = new Player[2];
    ElapsedCpuTimer elapsedTimer = new ElapsedCpuTimer();
    elapsedTimer.setMaxTimeMillis(CompetitionParameters.ACTION_TIME);
    Player[0] = createPlayer("", game, 3, false);
    Player[1] = createPlayer(new controllers.doNothing.Agent(game, elapsedTimer, 1), game,);

    double[] res = game.playGame(players, 1);
  }

  private static Player createPlayer(String playerName, StateObservationMulti so,
                                     int randomSeed, boolean isHuman)
  {
    Player player = null;
    try{
      //create the controller.
      player = (Player) createController(playerName, 0, so);
      if(player != null)
        player.setup(randomSeed, isHuman);
      //else System.out.println("No controller created.");

    }catch (Exception e)
    {
      //This probably happens because controller took too much time to be created.
      e.printStackTrace();
      System.exit(1);
    }

    //System.out.println("Created player.");

    return player;
  }

  protected static Player createController(String playerName, int playerID,
                                           StateObservationMulti so) throws RuntimeException
  {
    Player player = null;
    try
    {
      //Determine the time due for the controller creation.
      ElapsedCpuTimer ect = new ElapsedCpuTimer(CompetitionParameters.TIMER_TYPE);
      ect.setMaxTimeMillis(CompetitionParameters.INITIALIZATION_TIME);
      if (so.getNoPlayers() < 2) { //single player
        //Get the class and the constructor with arguments (StateObservation, long).
        Class<? extends Player> controllerClass = Class.forName(playerName).asSubclass(Player.class);
        Class[] gameArgClass = new Class[]{StateObservationMulti.class, ElapsedCpuTimer.class};
        Constructor controllerArgsConstructor = controllerClass.getConstructor(gameArgClass);

        //Call the constructor with the appropriate parameters.
        Object[] constructorArgs = new Object[]{so, ect.copy()};

        player = (Player) controllerArgsConstructor.newInstance(constructorArgs);
        player.setPlayerID(playerID);

      } else { //multi player
        //Get the class and the constructor with arguments (StateObservation, long, int).
        Class<? extends Player> controllerClass = Class.forName(playerName).asSubclass(Player.class);
        Class[] gameArgClass = new Class[]{StateObservationMulti.class, ElapsedCpuTimer.class, int.class};
        Constructor controllerArgsConstructor = controllerClass.getConstructor(gameArgClass);

        //Call the constructor with the appropriate parameters.
        Object[] constructorArgs = new Object[]{(StateObservationMulti)so.copy(), ect.copy(), playerID};

        player = (Player) controllerArgsConstructor.newInstance(constructorArgs);
        player.setPlayerID(playerID);
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

    return player;
  }
}
