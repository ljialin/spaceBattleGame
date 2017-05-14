package gamelog;

import core.game.StateObservationMulti;
import ontology.Types;

/**
 * Created by Simon Lucas on 14/05/2017.
 */
public interface FullGameLogger {

    public void log(StateObservationMulti gameState, Types.ACTIONS[] actions);
    public void printReport();

}
