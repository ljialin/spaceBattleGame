package test;

import competition.CompetitionParameters;
import core.game.StateObservationMulti;
import core.player.AbstractMultiPlayer;
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
public class BattleTest {
  View view;


  public static void main(String[] args) {
    playOne(0 ,1);
  }

  public static void playOne(int ply1, int ply2)
  {
    boolean visuals = true;
    String doNothingController = "controllers.doNothing.Agent";
    String human = "controllers.human.Agent";
    StateObservationMulti game = new StateObservationMulti(true);
    AbstractMultiPlayer[] abstractMultiPlayers = new AbstractMultiPlayer[2];
    ElapsedCpuTimer elapsedTimer = new ElapsedCpuTimer();
    elapsedTimer.setMaxTimeMillis(CompetitionParameters.ACTION_TIME);
    abstractMultiPlayers[0] = createPlayer(human, game, 3, false);
    abstractMultiPlayers[1] = createPlayer(doNothingController, game, 3 , true);
    double[][] res = game.playGame(abstractMultiPlayers, 1);
  }

  private static AbstractMultiPlayer createPlayer(String playerName, StateObservationMulti so,
                                                  int randomSeed, boolean isHuman)
  {
    AbstractMultiPlayer abstractMultiPlayer = null;
    try{
      //create the controller.
      abstractMultiPlayer = (AbstractMultiPlayer) createController(playerName, 0, so);
      if(abstractMultiPlayer != null)
        abstractMultiPlayer.setup(randomSeed, isHuman);
      //else System.out.println("No controller created.");

    }catch (Exception e)
    {
      //This probably happens because controller took too much time to be created.
      e.printStackTrace();
      System.exit(1);
    }

    //System.out.println("Created player.");

    return abstractMultiPlayer;
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

      if (playerName.equals("controllers.human.Agent")) {
        abstractMultiPlayer.setHuman();
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
