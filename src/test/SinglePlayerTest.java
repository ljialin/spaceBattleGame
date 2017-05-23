package test;

import core.game.StateObservationMulti;
import core.player.AbstractMultiPlayer;
import core.player.AbstractPlayer;
import ontology.Constants;

import java.util.Random;

/**
 * Created with IntelliJ IDEA. User: Diego Date: 04/10/13 Time: 16:29 This is a
 * Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 */
public class SinglePlayerTest {

	static String mctsController = "controllers.singlePlayer.advanced.nestedMC";


    public static void main(String[] args) {
		playOne(true, 1);
    }

	public static int playOne(boolean visuals, int run) {
		Random rdm = new Random();
		StateObservationMulti game = new StateObservationMulti(visuals);
		AbstractMultiPlayer[] players = new AbstractPlayer[2];
		players[0] = GameTest.createMultiPlayer(mctsController+".Agent", game, rdm.nextInt(), 0, false);
		players[1] = GameTest.createMultiPlayer("controllers.singlePlayer.rotateAndShoot.Agent", game, rdm.nextInt(), 1, false);

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
}
