package test;

import core.game.StateObservationMulti;
import gamelog.EntropyLogger;
import ontology.Constants;

import java.util.Arrays;

import static test.GameTest.playNAndMean;

/**
 * Created by Simon Lucas on 14/05/2017.
 */

public class GameEntropyLogTest {
    public static void main(String[] args) {
        // todo: Play with different parameter values to observe effects on entropy
        // todo: How does entropy also vary depending on the players?

        // note: could set these up directly
        int[] params = {5, 4, 1, 2, 5, 20};
        Constants.SHIP_MAX_SPEED = params[0];
        Constants.THRUST_SPEED = params[1];
        Constants.MISSILE_COST = params[2];
        Constants.MISSILE_MAX_SPEED = params[3];
        Constants.MISSILE_COOLDOWN = params[4];
        Constants.SHIP_RADIUS = params[5];

        int p1 = 2; // player list in GameTest.testedControllers
        int p2 = 2;
        System.out.format("Playing %s versus %s\n\n",
                GameTest.testedControllers[p1], GameTest.testedControllers[p2]);
        double[] x = playNAndMean(3, p1, p2);
        System.out.println(Arrays.toString(x));
        StateObservationMulti.gameLogger.printReport();
    }
}


