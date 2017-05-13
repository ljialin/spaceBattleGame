package test;

import core.game.StateObservationMulti;
import tools.ElapsedTimer;

/**
 * Created by sml on 12/05/2017.
 *
 *  Copying a game state is an important part of simulation-based search
 *
 *  Here we're just checking how long it takes
 *
 */

public class GameCopyTest {

    public static void main(String[] args) {

        ElapsedTimer timer = new ElapsedTimer();
        double gameTick = 40;

        int nCopies = (int) 1e6;

        StateObservationMulti game = new StateObservationMulti(false);

        for (int i=0; i<nCopies; i++) {

            game = game.copy();

        }

        System.out.format("Made %d copies\n", nCopies);

        System.out.format("Copies per milli-second: %.1f\n\n", nCopies / (double) timer.elapsed());
        System.out.format("Game tick time = %d ms\n",(int) gameTick);
        System.out.format("Copies per game tick: %d\n\n", (int) (gameTick * nCopies / (double) timer.elapsed()));

        System.out.println(timer);

    }
}
