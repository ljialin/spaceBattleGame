package test;

import competition.CompetitionParameters;
import core.game.StateObservationMulti;
import core.player.AbstractMultiPlayer;
import gamelog.GameLogger;
import ontology.Constants;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tools.MutableDouble;
import tools.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

/**
 * Created by Jialin Liu on 05/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class GameTest {


    static String doNothingController = "doNothing";
    static String olmctsController = "sampleOLMCTS";
    static String humanArrows = "humanArrows";
    static String humanWSAD = "humanWSAD";
    static String sampleRandom = "sampleRandom";
    static String OneStepLookAheadController = "sampleOneStepLookAhead";
    static String GAController = "sampleGA";
    static String rotateAndShootController = "rotateAndShoot";

    static String[] testedControllers = {
            "rotateAndShoot", "doNothing", "sampleRandom",
            "sampleOneStepLookAhead", "sampleGA", "sampleOLMCTS",
            "sampleOLMCTStwoT", "sampleOLMCTShalfT", "humanArrows"
    };

    public static void main(String[] args) {

//    MutableDouble opt_value = new MutableDouble(0.0);
//    if(args.length>1) {
//      if(Utils.findArgValue(args, "runs", opt_value)) {
//        nbRuns = opt_value.intValue();
//      }
//      if(Utils.findArgValue(args, "p1", opt_value)) {
//        p1 = opt_value.intValue();
//      }
//      if(Utils.findArgValue(args, "p2", opt_value)) {
//        p2 = opt_value.intValue();
//      }
//
//      if(Utils.findArgValue(args, "2", opt_value)) {
//        Constants.SHIP_MAX_SPEED = opt_value.intValue();
//      }
//      if(Utils.findArgValue(args, "3", opt_value)) {
//        Constants.THRUST_SPEED = opt_value.intValue();
//      }
//      if(Utils.findArgValue(args, "4", opt_value)) {
//        Constants.MISSILE_COST = opt_value.intValue();
//      }
//      if(Utils.findArgValue(args, "7", opt_value)) {
//        Constants.MISSILE_MAX_SPEED = opt_value.intValue();
//      }
//      if(Utils.findArgValue(args, "8", opt_value)) {
//        Constants.MISSILE_COOLDOWN = opt_value.intValue();
//      }
//      if(Utils.findArgValue(args, "11", opt_value)) {
//        Constants.KILL_AWARD = opt_value.intValue();
//      }
//
//      if(Utils.findArgValue(args, "1", opt_value)) {
//        Constants.SHIP_RADIUS = opt_value.intValue();
//      }
//      if(Utils.findArgValue(args, "5", opt_value)) {
//        Constants.MISSILE_RADIUS = opt_value.intValue();
//      }
//      if(Utils.findArgValue(args, "6", opt_value)) {
//        Constants.MISSILE_MAX_TTL = opt_value.intValue();
//      }
//
//      if(Utils.findArgValue(args, "9", opt_value)) {
//        Constants.FRICTION = opt_value.intValue() / 100;
//      }
//      if(Utils.findArgValue(args, "10", opt_value)) {
//        Constants.RADIAN_UNIT = opt_value.intValue() * Math.PI / 180;
//      }
//
//    }
//    int[] params = { 4, 4, 1, 2, 5, 20}; //ValidParams.optimalParams50[33];
//    int[] params = { 10, 5, 1, 1, 4, 20};
        int[] params = {5, 4, 1, 2, 5, 20};
        Constants.SHIP_MAX_SPEED = params[0];
        Constants.THRUST_SPEED = params[1];
        Constants.MISSILE_COST = params[2];
        Constants.MISSILE_MAX_SPEED = params[3];
        Constants.MISSILE_COOLDOWN = params[4];
        Constants.SHIP_RADIUS = params[5];

        playOne(0, 2, false, 0);

        // now print out the entropy log

//    for(int j=0; j<1; j++) {
//      int wins = 0;
//      for (int i = 0; i < 100; i++) {
//        wins += playOne(2, 0, false);
//        System.out.println((i + 1) + "\t" + wins + "\t" + (double) 100 * wins / (i + 1));
//      }
//    }


//    double[] res = playNAndMean( nbRuns, p1, p2);
//    String str = "" + res[0];
//    for (int i=1; i<res.length; i++) {
//      str += " " + res[i];
//    }
//    System.out.println(str);

    }

//  public static void playOne()
//  {
//    boolean visuals = true;
//    StateObservationMulti game = new StateObservationMulti(visuals);
//    AbstractMultiPlayer[] players = new AbstractMultiPlayer[2];
//    String p1 = sampleRandom;
//    String p2 = sampleRandom;
//    players[0] = createMultiPlayer("controllers." + p1 + ".Agent", game, rdm.nextInt(), 0, false);
//    players[1] = createMultiPlayer("controllers." + p2 + ".Agent", game, rdm.nextInt(), 1, false);
//    double[][] res = game.playGame(players, rdm.nextInt());
//    dump(res, "./dat/" + p1 + "_vs_" + p2 + ".dat");
//  }

    public static int playOne(int id1, int id2, boolean visuals, int run) {
        Random rdm = new Random();
        StateObservationMulti game = new StateObservationMulti(visuals);
        AbstractMultiPlayer[] players = new AbstractMultiPlayer[2];
        String p1 = testedControllers[id1];
        String p2 = testedControllers[id2];
        players[0] = createMultiPlayer("controllers." + p1 + ".Agent", game, rdm.nextInt(), 0, false);
        players[1] = createMultiPlayer("controllers." + p2 + ".Agent", game, rdm.nextInt(), 1, false);

        if (id1 == 0) {
            game.cheating = 0;
        } else if (id2 == 0) {
            game.cheating = 1;
        } else {
            game.cheating = -1;
        }

        game.playGame(players, rdm.nextInt());

        double state0;
        double state1;
        if (game.getGameScore(0) > game.getGameScore(1)) {
            state0 = 1;
            state1 = 0;
        } else if (game.getGameScore(0) < game.getGameScore(1)) {
            state0 = 0;
            state1 = 1;
        } else {
            state0 = 0.5;
            state1 = 0.5;
        }
        System.out.println(run
                + " " + state0 + " " + game.getGameScore(0)
                + " " + state1 + " " + game.getGameScore(1)
                + " " + Constants.SHIP_MAX_SPEED
                + " " + Constants.THRUST_SPEED + " " + Constants.MISSILE_COST
                + " " + Constants.MISSILE_MAX_SPEED
                + " " + Constants.MISSILE_COOLDOWN);
        return (game.getGameScore(1) > game.getGameScore(0) ? 1 : 0);
    }

    public static void playMany(int nbRuns, int id1, int id2) {

        boolean visuals = false;
        for (int i = 0; i < nbRuns; i++) {
            Random rdm = new Random();
            StateObservationMulti game = new StateObservationMulti(false);
            AbstractMultiPlayer[] players = new AbstractMultiPlayer[2];
            String p1 = testedControllers[id1];
            String p2 = testedControllers[id2];
            players[0] = createMultiPlayer("controllers." + p1 + ".Agent", game, rdm.nextInt(), 0, false);
            players[1] = createMultiPlayer("controllers." + p2 + ".Agent", game, rdm.nextInt(), 1, false);
            if (id1 == 0) {
                game.cheating = 0;
            } else if (id2 == 0) {
                game.cheating = 1;
            } else {
                game.cheating = -1;
            }
            double[][] res = game.playGame(players, rdm.nextInt());

            dump(res, "./dat/" + p1 + "_vs_" + p2 + "_run" + (i + 1) + ".dat");
        }
    }

    public static double[] playNAndMean(int nbRuns, int id1, int id2) {

        boolean visuals = false;
        double[][] res = new double[nbRuns][4];
        for (int i = 0; i < nbRuns; i++) {
            Random rdm = new Random();

            StateObservationMulti game = new StateObservationMulti(false);
            AbstractMultiPlayer[] players = new AbstractMultiPlayer[2];
            String p1 = testedControllers[id1];
            String p2 = testedControllers[id2];
            players[0] = createMultiPlayer("controllers." + p1 + ".Agent", game, rdm.nextInt(), 0, false);
            players[1] = createMultiPlayer("controllers." + p2 + ".Agent", game, rdm.nextInt(), 1, false);
            if (id1 == 0) {
                game.cheating = 0;
            } else if (id2 == 0) {
                game.cheating = 1;
            } else {
                game.cheating = -1;
            }
            game.playGame(players, rdm.nextInt());
            double state0;
            double state1;
            if (game.getGameScore(0) > game.getGameScore(1)) {
                state0 = 1;
                state1 = 0;
            } else if (game.getGameScore(0) < game.getGameScore(1)) {
                state0 = 0;
                state1 = 1;
            } else {
                state0 = 0.5;
                state1 = 0.5;
            }
            res[i][0] = state0;
            res[i][1] = game.getGameScore(0);
            res[i][2] = state1;
            res[i][3] = game.getGameScore(1);
            System.out.println(i + " " + res[i][0] + " " + res[i][1] + " " + res[i][2] + " " + res[i][3]);

        }
        double[] meanRes = Utils.meanArray(res);
//    System.out.println(nbRuns + " " + meanRes[0] + " " + meanRes[1] + " " + meanRes[2] + " " + meanRes[3] );
        return meanRes;
    }

    private static void dump(double[][] results, String filename) {
        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename)));

            for (int i = 0; i < results.length; ++i) {
                for (int j = 0; j < results[i].length; ++j) {
                    writer.write(results[i][j] + ",");
                }
                writer.write("\n");
            }

            writer.close();

        } catch (Exception e) {
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
            int randomSeed, int id, boolean isHuman) {
        AbstractMultiPlayer player = null;

        try {
            //create the controller.
            player = (AbstractMultiPlayer) createController(playerName, id, so);
            if (player != null) {
                player.setup(randomSeed, isHuman);
            }

        } catch (Exception e) {
            //This probably happens because controller took too much time to be created.
            e.printStackTrace();
            System.exit(1);
        }

        return player;
    }


    protected static AbstractMultiPlayer createController(String playerName, int playerID,
                                                          StateObservationMulti so) throws RuntimeException {
        AbstractMultiPlayer abstractMultiPlayer = null;
        try {
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
                Object[] constructorArgs = new Object[]{(StateObservationMulti) so.copy(), ect.copy(), playerID};

                abstractMultiPlayer = (AbstractMultiPlayer) controllerArgsConstructor.newInstance(constructorArgs);
                abstractMultiPlayer.setPlayerID(playerID);
            }
            //Check if we returned on time, and act in consequence.
            long timeTaken = ect.elapsedMillis();
            if (ect.exceededMaxTime()) {
                long exceeded = -ect.remainingTimeMillis();
//        System.out.println("Controller initialization time out (" + exceeded + ").");

                return null;
            } else {
//        System.out.println("Controller initialization time: " + timeTaken + " ms.");
            }

            //This code can throw many exceptions (no time related):

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            System.err.println("Constructor " + playerName + "(StateObservation,long) not found in controller class:");
            System.exit(1);

        } catch (ClassNotFoundException e) {
            System.err.println("Class " + playerName + " not found for the controller:");
            e.printStackTrace();
            System.exit(1);

        } catch (InstantiationException e) {
            System.err.println("Exception instantiating " + playerName + ":");
            e.printStackTrace();
            System.exit(1);

        } catch (IllegalAccessException e) {
            System.err.println("Illegal access exception when instantiating " + playerName + ":");
            e.printStackTrace();
            System.exit(1);
        } catch (InvocationTargetException e) {
            System.err.println("Exception calling the constructor " + playerName + "(StateObservation,long):");
            e.printStackTrace();
            System.exit(1);
        }

        return abstractMultiPlayer;
    }
}
