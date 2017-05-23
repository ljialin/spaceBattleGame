package controllers.singlePlayer.advanced.nestedMC;

import core.game.StateObservationMulti;
import core.player.AbstractPlayer;
import ontology.Constants;
import ontology.Types;
import ontology.asteroids.Ship;
import tools.ElapsedCpuTimer;
import tools.Vector2d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Tristan Cazenave's nestedMC
 *
 *
 */
public class Agent extends AbstractPlayer {
    static Random random = new Random();
//    public Types.ACTIONS[] allActions;
    ArrayList<Types.ACTIONS> allActionsList;
    public Types.ACTIONS[] actions; // available actions at current game tick
    public int num_actions; // length of actions

    public static int nestDepth = 2;
    public static int maxNestingDepth = 5;
    public static int maxRolloutLength = 20;

    int[] lengthBestRollout;
    double[] scoreBestRollout;
    Types.ACTIONS[][] bestRollout;
//    static int maxLegalMoves = Types.ACTIONS.values().length; // TODO: 17/05/17 should not use this

    double bestScoreNested = 1000000;

    public int minRemainingTime = 5;
    public double epsilon = 1e-6;
    Types.ACTIONS[] bestPrediction;
    public double bestScoreSoFar;
    
//    public int maxModelCalls = 10000;
    public int nbModelCalls;



    // Position learning
    public double maxMag = Math.sqrt(Constants.HEIGHT*Constants.HEIGHT+Constants.WIDTH*Constants.WIDTH)/2;
    public int nbGrid = 10;
    public int nbAngleUnit = 8;
    public double angleUnit = 2*Math.PI/nbAngleUnit;
    public double cellSize;


    /**
     * Public constructor with state observation and time due.
     *
     * @param so           state observation of the current game.
     * @param elapsedTimer Timer for the controller creation.
     */
    public Agent(StateObservationMulti so, ElapsedCpuTimer elapsedTimer, int playerId) {
        allActionsList = Types.AVAILABLE_ACTIONS;
//        allActions = new Types.ACTIONS[allActionsList.size()];
//        for (int i=0; i<allActions.length; i++) {
//            allActions[i] = allActionsList.get(i);
//        }
        //Get the actions in a static array.
        ArrayList<Types.ACTIONS> act = so.getAvailableActions();
        actions = new Types.ACTIONS[act.size()];
        for (int i = 0; i < actions.length; ++i) {
            actions[i] = act.get(i);
        }
        num_actions = actions.length;
        bestScoreSoFar = -bestScoreNested;
        bestPrediction = new Types.ACTIONS[maxRolloutLength];
        for (int i=0;i<maxRolloutLength;i++) {
            bestPrediction[i] = Types.ACTIONS.ACTION_NIL;
        }
        bestRollout = new Types.ACTIONS[maxNestingDepth][maxRolloutLength];
        for (int i=0; i<maxNestingDepth; i++) {
            for (int j=0; j<maxRolloutLength; j++) {
                bestRollout[i][j] = Types.ACTIONS.ACTION_NIL;
            }
        }
        lengthBestRollout = new int[maxNestingDepth];
        scoreBestRollout = new double[maxNestingDepth];
        System.out.println(Arrays.toString(actions));

        cellSize = (int) Math.ceil(this.maxMag/nbGrid);
    }

    public void shiftBestPlayout() {
        int i=0;
        for (;i<maxRolloutLength-1;i++){
            bestPrediction[i] = bestPrediction[i+1];
        }
        bestPrediction[i] = Types.ACTIONS.ACTION_NIL;
    }

    /**
     * Picks an action. This function is called every game step to request an
     * action from the player.
     *
     * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
     * @return An action for the current state
     */
    public Types.ACTIONS act(StateObservationMulti stateObs, ElapsedCpuTimer elapsedTimer) {
        getRelatedPosIdx(stateObs);
//        shiftBestPlayout();
        nbModelCalls=0;
        bestScoreSoFar=-bestScoreNested;
        int[] code = new int[maxRolloutLength];
        String str2 = "After shifted: actions are ";
        for (int j = 0; j < 10; j++) {
            str2 += bestPrediction[j] + ",";
        }
//        System.out.println(str2);

        //Set the state observation object as the new root of the tree.
        // we'll set up a game adapter and run the algorithm independently each
        // time at least to being with
        StateObservationMulti obs = stateObs.copy();
        // move the below to constructor
//        bestRollout = new Types.ACTIONS[maxNestingDepth][maxRolloutLength];
//        for (int i=0; i<maxNestingDepth; i++) {
//            for (int j=0; j<maxRolloutLength; j++) {
//                bestRollout[i][j] = Types.ACTIONS.ACTION_NIL;
//            }
//        }
//        lengthBestRollout = new int[maxNestingDepth];
//        scoreBestRollout = new double[maxNestingDepth];


        //nested(obs, nestDepth, moveSeq, 0);
        double bestScore = -bestScoreNested;
        Types.ACTIONS bestAction = actions[0];
        bestAction = actions[0];
        int nestedIdx=0;
        boolean finished = false;
        while (elapsedTimer.remainingTimeMillis()>minRemainingTime && nestedIdx<maxNestingDepth){
//            && nbModelCalls < maxModelCalls) {
            Types.ACTIONS tmpBestAction = actions[0];
//            double bestScore = -bestScoreNested;
            for (int i=0; i<num_actions; i++) {

                StateObservationMulti state = obs.copy();
                Types.ACTIONS[] moveSeqCopy = new Types.ACTIONS[maxRolloutLength];
                int nActionsPlayed = 0;
                state.advance(actions[i]);
                nbModelCalls++;
                moveSeqCopy[nActionsPlayed] = actions[i];
                nActionsPlayed++;
                if (nestedIdx > 0) {
                    finished = nested(state, nestedIdx, moveSeqCopy, nActionsPlayed, code, elapsedTimer);
                } else {
                    finished = playoutNRPA(state, moveSeqCopy, 0, code, elapsedTimer);
                }
                if (finished) {
                    double score = score(state);
                    if (score > bestScore){// || (score==bestScore && random.nextDouble()>0.5)) {
                        bestScore = score;
                        tmpBestAction = actions[i];
//                        if (bestScore>bestScoreSoFar) {
//                            bestPrediction[nActionsPlayed] = tmpBestAction;
//                        }
                        if (score>bestScoreSoFar){// || (score==bestScoreSoFar && random.nextDouble()>0.5)) {
//                        if (bestScore>bestScoreSoFar ) {
                            for (int j = 0; j < maxRolloutLength; j++) {
                                bestPrediction[j] = moveSeqCopy[j];
                            }
                            String str = "score=" +bestScore+ ", actions are ";
                            for (int j = 0; j < 10; j++) {
                                str += bestPrediction[j] + ",";
                            }
//                            System.out.println(str);
//                          bestPrediction = bestRollout[nestingLevel].clone();
                            bestScoreSoFar = bestScore;
                        }
                    }
                }
            }
            if (finished || nestedIdx==0) {
//                System.out.println("previous bestAction is " + bestAction + " and new one is "
//                    + tmpBestAction + " with nestedIdx="+nestedIdx);
                bestAction = tmpBestAction;
            }
            nestedIdx++;
        }
//        System.out.println("BestAction is " + bestAction);
//        return bestAction;
//        System.out.println(bestPrediction.toString() + " bestScore=" + bestScoreSoFar);
        System.out.println("Move:"+bestPrediction[0]);
        System.out.println("nbModelCalls used =" + nbModelCalls);
        return bestPrediction[0];
    }


    boolean playout(StateObservationMulti StateObservationMulti, Types.ACTIONS[] moveSeq, int nActionsPlayed,
        ElapsedCpuTimer elapsedTimer) {
        // Types.ACTIONS[] moveSeq = new Types.ACTIONS[maxRolloutLength];

        while (!StateObservationMulti.isGameOver() && nActionsPlayed < maxRolloutLength) {
            if (elapsedTimer.remainingTimeMillis() < minRemainingTime)
//            || nbModelCalls>=maxModelCalls)
                return false;
            ArrayList<Types.ACTIONS> availableActions = StateObservationMulti.getAvailableActions();
            int move = random.nextInt(availableActions.size());
            moveSeq[nActionsPlayed] = availableActions.get(move);
            StateObservationMulti.advance(moveSeq[nActionsPlayed]);
            nbModelCalls++;
            nActionsPlayed++;
        }
        return true;
    }

    boolean nested(StateObservationMulti StateObservationMulti, int nestingLevel, Types.ACTIONS[] moveSeq,
                int nActionsPlayed, int[] code, ElapsedCpuTimer elapsedTimer) {
        ArrayList<Types.ACTIONS> availableActions = StateObservationMulti.getAvailableActions();
        int nbMoves = availableActions.size();

        lengthBestRollout[nestingLevel] = -1;
        scoreBestRollout[nestingLevel] = -bestScoreNested;
        float res;

//        double avgTimeTaken = 0;
//        double acumTimeTaken = 0;
//        long remaining = elapsedTimer.remainingTimeMillis();
//        int numIters = 0;
//        int remainingLimit = 5;
        while (true) {
//            ElapsedCpuTimer elapsedTimerIteration = new ElapsedCpuTimer();
            if (StateObservationMulti.isGameOver())
                return true;
            if (nActionsPlayed >= maxRolloutLength)
                return true;
            if (elapsedTimer.remainingTimeMillis() < minRemainingTime) {
//            } nbModelCalls+nbMoves*maxNestingDepth>=maxModelCalls)
                return false;
            }
            //return board.score ();
            for (int i = 0; i < nbMoves; i++) {
                StateObservationMulti state = StateObservationMulti.copy();
                Types.ACTIONS[] moveSeqCopy = new Types.ACTIONS[maxRolloutLength];
                int nActionsCopy = nActionsPlayed;
                for (int j = 0; j < nActionsPlayed; j++)
                    moveSeqCopy[j] = moveSeq[j];
                if (nestingLevel == 1) {
                    moveSeqCopy[nActionsCopy] = availableActions.get(i);
                    state.advance(moveSeqCopy[nActionsCopy]);
                    nbModelCalls++;
                    nActionsCopy++;
                    playoutNRPA(state, moveSeqCopy, nActionsCopy, code, elapsedTimer);
                } else {
                    moveSeqCopy[nActionsCopy] = availableActions.get(i);
                    state.advance(moveSeqCopy[nActionsCopy]);
                    nbModelCalls++;
                    nActionsCopy++;
                    nested(state, nestingLevel - 1, moveSeqCopy, nActionsCopy, code, elapsedTimer);
                }
                if (elapsedTimer.remainingTimeMillis() < minRemainingTime)
                    return false;
                double score = score(state);
                if (score > scoreBestRollout[nestingLevel]) {
                    //System.out.println ("level " + nestingLevel + "score " + score);
                    scoreBestRollout[nestingLevel] = score;
                    lengthBestRollout[nestingLevel] = maxRolloutLength;
                    for (int j = 0; j < maxRolloutLength; j++) {
                        bestRollout[nestingLevel][j] = moveSeqCopy[j];

                    }
//                    System.out.println("score=" + score + ", bestScoreSoFar="+bestScoreSoFar);
                }
            }
//            System.out.println("nActionsPlayed="+nActionsPlayed);
//            if (bestRollout[nestingLevel][nActionsPlayed] == null) {
//                StateObservationMulti.advance(Types.ACTIONS.ACTION_NIL);
//                moveSeq[nActionsPlayed] = Types.ACTIONS.ACTION_NIL;
//            } else {
                StateObservationMulti.advance(bestRollout[nestingLevel][nActionsPlayed]);
                nbModelCalls++;
                moveSeq[nActionsPlayed] = bestRollout[nestingLevel][nActionsPlayed];
//            }
            nActionsPlayed++;
        }
    }

    public double score(StateObservationMulti stateObs) {
        if (stateObs.isGameOver()) {
            Types.WINNER winner = stateObs.getGameWinner();
            if (winner == Types.WINNER.PLAYER_WINS) {
                return bestScoreNested;
            } else if (winner == Types.WINNER.PLAYER_LOSES) {
                return -bestScoreNested;
            }
        }
//        return stateObs.getGameScore();
        return (random.nextDouble()*epsilon + (stateObs.getGameScore(0)-stateObs.getGameScore(1)));
    }

    public int getRelatedPosIdx(StateObservationMulti stateObs) {
        Ship[] ships = stateObs.getAvatars();
        Vector2d relativePos = Vector2d.subtract(ships[0].getPosition(), ships[1].getPosition());
        Vector2d dir = ships[0].getDirection();
        Vector2d dirOpp = ships[1].getDirection();
        int idx1 = (int) Math.floor(relativePos.mag()/cellSize);
        if (idx1>=nbGrid) {
            idx1 = 0;
        }
        double angle = relativePos.theta() - ships[1].getDirection().theta();
        int idx2;
        if (angle<0) {
            idx2=(int) Math.floor((2*Math.PI+angle)/angleUnit);
        } else {
            idx2 = (int) Math.floor(angle / angleUnit);
        }
        if (idx2>=nbAngleUnit) {
            idx2 = 0;
        }
        int idx = idx1*nbAngleUnit + idx2;
        System.out.println("ship 1 at "+ships[0].getPosition().toString() + "; ship 2 at " +ships[1].getPosition().toString());

        System.out.println("Area:"+idx1+",Angle:"+idx2 + ",idPos="+idx);
        return idx;
    }


    double ALPHA = 0.32;
    int maxMoveNumber = nbAngleUnit*nbGrid*Types.AVAILABLE_ACTIONS.size();
    double[][] statistics = new double[maxMoveNumber][2]; //include do nothing. nbVisited and sumScore
    double[] policy = new double[maxMoveNumber];


    public void resetStatistic() {
        if (statistics == null) {
            statistics = new double[maxMoveNumber][2];
        } else {
            for (int i=0;i<statistics.length;i++) {
                for (int j=0;j<statistics[0].length;j++) {
                    statistics[i][j] = 0;
                }
            }
        }
    }

    public boolean[] isActionAvailable(StateObservationMulti stateObservationMulti) {
        boolean[] isActionAvailable = new boolean[allActionsList.size()];
        ArrayList<Types.ACTIONS> availableAction = stateObservationMulti.getAvailableActions();
        for (int i=0;i<allActionsList.size();i++) {
            if (availableAction.contains(allActionsList.get(i))) {
                isActionAvailable[i] = true;
            } else {
                isActionAvailable[i] = false;
            }
        }
        return isActionAvailable;
    }

    boolean playoutNRPA(StateObservationMulti stateObservationMulti, Types.ACTIONS[] moveSeq,
                        int nActionsPlayed, int[] code,
                    ElapsedCpuTimer elapsedTimer) {
        StateObservationMulti soCopy = stateObservationMulti.copy();
        double[] probaMove = new double[allActionsList.size()];
        int nActionsPlayedSave = nActionsPlayed;
        while (!soCopy.isGameOver() && nActionsPlayed < maxRolloutLength) {
            if (elapsedTimer.remainingTimeMillis() < minRemainingTime)
                return false;
            int idx = getRelatedPosIdx(soCopy);
            code[nActionsPlayed] = idx;
            System.out.println("area id="+idx);
            for (int i = 0; i < allActionsList.size(); i++) {
                probaMove[i] = Math.exp(policy[idx*allActionsList.size()+i]);
            }
            boolean[] isActionAvailable = isActionAvailable(soCopy);
            double sum = 0;
            for (int i = 0; i < allActionsList.size(); i++) {
                if (isActionAvailable[i]) {
                    sum += probaMove[i];
                }
            }
            double r = random.nextDouble()* sum;
            int j = 0;
            while(!isActionAvailable[j] && j<isActionAvailable.length-1) {
                j++;
                assert(j<isActionAvailable.length);
            }
            double s = probaMove[j];
            while (s < r && j<isActionAvailable.length-1) {
                j++;
                assert(j<isActionAvailable.length);
                if (isActionAvailable[j]) {
                    s += probaMove[j];
                }
            }
            soCopy.advance(allActionsList.get(j));
            moveSeq[nActionsPlayed] = allActionsList.get(j);
            nbModelCalls++;
            nActionsPlayed++;
        }
        double score = score(soCopy);
        for (int i=nActionsPlayedSave; i<nActionsPlayed; i++) {
            statistics[code[i]][1] += score;
            statistics[code[i]][0] += 1;
        }
        return true;
    }

    public void updatePolicy() {
        double sum = 0;
        for (int i=0;i<policy.length; i++)
            sum += Math.exp(policy[i]);
        for (int i=0;i<policy.length; i++)
            policy[i] -= ALPHA * Math.exp(policy[i])/sum;
   }

//    double playoutNRPA (StateObservationMulti stateObs, double pol [maxMoveNumber]) {
//        int nbMoves = 0;
//        Move moves [MaxLegalMoves];
//        double probaMove [MaxLegalMoves];
//
//        while (true) {
//            if (board->terminal ()) {
//                return board->score () > 0;
//            }
//            nbMoves = board->legalMoves (joueur, moves);
//
//            for (int i = 0; i < nbMoves; i++)
//                probaMove [i] = exp (pol [moves [i].number ()]);
//
//            double sum = probaMove [0];
//            for (int i = 1; i < nbMoves; i++)
//                sum += probaMove [i];
//            double r = (rand () / (RAND_MAX + 1.0)) * sum;
//            int j = 0;
//            double s = probaMove [0];
//            while (s < r) {
//                j++;
//                s += probaMove [j];
//            }
//            board->play (moves [j]);
//            if (board->length >= MaxPlayoutLength - 20) {
//                return 0;
//            }
//            joueur = board->opponent (joueur);
//        }
//        return 0.0;
//    }

//    void adapt (int res, Board* board, int joueur, int start, int length, Move rollout [MaxPlayoutLength], double pol [maxMoveNumber]) {
//        double polp [ maxMoveNumber];
//        for (int i = 0; i < maxMoveNumber; i++)
//            polp [i] = pol [i];
//        int nbMoves;
//        Move moves [MaxLegalMoves];
//        for (int i = start; i < length; i++) {
//            if (((res > 0) && (joueur == White)) ||
//                ((res <= 0) && (joueur == Black))) {
//                nbMoves = board->legalMoves (joueur, moves);
//                polp [rollout [i].number ()] += ALPHA;
//                double z = 0.0;
//                for (int j = 0; j < nbMoves; j++)
//                    z += exp (pol [moves [j].number()]);
//                for  (int j = 0; j < nbMoves; j++)
//                    polp [moves [j].number()] -= ALPHA * exp (pol [moves [j].number ()]) / z;
//            }
//            board->play (rollout [i]);
//            joueur = board->opponent (joueur);
//        }
//        for (int i = 0; i < maxMoveNumber; i++)
//            pol [i] = polp [i];
//    }

}
