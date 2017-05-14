package controllers.labSimpleRHEA;

import core.game.StateObservationMulti;
import core.player.AbstractMultiPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jialin Liu on 14/05/17.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
// TODO: 14/05/17 Exercise Lab RHEA
// Complete the Agent with a RH-RMHC or RH-(1+1)-EA
// Test your agent
public class Agent extends AbstractMultiPlayer {

    int id; //this player's ID

    /**
     * initialize all variables for the agent
     * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
     * @param playerID ID if this agent
     */
    public Agent(StateObservationMulti stateObs, ElapsedCpuTimer elapsedTimer, int playerID){
        id = playerID;
    }

    /**
     * return ACTION_NIL on every call to simulate doNothing player
     * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
     * @return 	ACTION_NIL all the time
     */
    @Override
    public Types.ACTIONS act(StateObservationMulti stateObs, ElapsedCpuTimer elapsedTimer) {
        ArrayList<Types.ACTIONS> a = stateObs.getAvailableActions(id);
        return Types.ACTIONS.values()[new Random().nextInt(a.size())];
    }
}
