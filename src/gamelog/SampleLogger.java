package gamelog;

import core.game.StateObservationMulti;
import ontology.Types;
import tools.HeatMap;
import tools.JEasyFrame;
import tools.Vector2d;

/**
 * Created by Simon Lucas on 14/05/2017.
 *
 *  A sample logging class
 *
 */
public class SampleLogger implements FullGameLogger {

    EntropyLogger player1Actions = new EntropyLogger();

    EntropyLogger player1Positions = new EntropyLogger();

    // assume a width and height to help quantise the player positions

    // can ex

    static int width = 640;
    static int height = 480;

    // todo: experiment with varying the number of cells in the playing grid
    // does this affect the resolution of the entropy measurements?

    static int nCellsWide = 10;
    static int gridSize = width / nCellsWide;

    static int nGridCells = 1 + gridIndex(width-1, height-1);

    // this is available somewhere and should really be looked up in the Types.ACTIONS
    static int possibleActions = 5;

    @Override
    public void log(StateObservationMulti gameState, Types.ACTIONS[] actions) {

        player1Actions.addAction(actions[0].ordinal());
        Vector2d position = gameState.getAvatarPosition(0);
        player1Positions.addAction(gridIndex(position));

    }

    static int gridIndex(Vector2d v) {
        return gridIndex(v.x, v.y);
    }

    static int gridIndex(double x, double y) {
        int px = (int) (x / gridSize);
        int py = (int) (y / gridSize);

        return px + py * nCellsWide;
    }

    public void showPositionalHeatMap() {
        new JEasyFrame(new HeatMap(player1Positions.pVec(), nCellsWide), "Positional Heat Map");
    }

    public void printReport() {
        System.out.println();
        System.out.println("N grid cells: " + nGridCells);
        System.out.println("Player actions:");
        System.out.println(player1Actions.frequencyReport());
        System.out.format("Normalised entropy: %.5f\n ", player1Actions.normalisedEntropy(possibleActions));
        System.out.println();
        System.out.println("Player positions:");
        System.out.println(player1Positions.frequencyReport());
        System.out.format("Normalised entropy: %.5f\n ", player1Positions.normalisedEntropy(nGridCells));
        System.out.println();

        System.out.println("Just to demonstrate that all actions recorded are available: ");
        System.out.println(player1Actions.actions);
    }
}
