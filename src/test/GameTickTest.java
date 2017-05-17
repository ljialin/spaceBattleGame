package test;

import core.game.StateObservationMulti;
import ontology.Types;
import tools.ElapsedTimer;

/**
 * Created by sml on 12/05/2017.
 */
public class GameTickTest {

    public static void main(String[] args) {

        ElapsedTimer timer = new ElapsedTimer();

        int gameTickMillis = 40;
        int nTicks = 500;
        int nTrials = 10000;

        for (int i = 0; i < nTrials; i++) {

            StateObservationMulti game = new StateObservationMulti(false);

            controllers.multiPlayer.sampleRandom.Agent randomAgent = new controllers.multiPlayer.sampleRandom.Agent(game, null, 0);

            // do not need separate agents for player one and plater 2
            // since actions are simply random

            for (int j=0; j<nTicks; j++) {
                Types.ACTIONS a1 = randomAgent.act(game, null);
                Types.ACTIONS a2 = randomAgent.act(game, null);

                Types.ACTIONS[] actions = new Types.ACTIONS[]{a1, a2};
                game.advance(actions);

                // record actions made

                // record FEATURES of game state

            }
            // System.out.println(i + "\t " + game.getGameTick());

        }

        int totalTicks = nTicks * nTrials;
        System.out.format("Made %d ticks\n", totalTicks);
        System.out.format("Ticks per milli-second: %.1f\n\n", totalTicks / (double) timer.elapsed());
//        System.out.format("Game tick time = %d ms\n", (int) gameTick);
        System.out.format("Copies per game tick:   %d\n\n", (int) (gameTickMillis * totalTicks / (double) timer.elapsed()));
        System.out.println(timer);

    }
}
