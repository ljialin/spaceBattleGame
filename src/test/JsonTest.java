package test;

import java.util.Random;
import com.google.gson.Gson;
import core.game.StateObservationMulti;
import ontology.Types;
import tools.ElapsedCpuTimer;

/**
 * Created by simonmarklucas on 04/10/2016.
 */
public class JsonTest {

  public static void main(String[] args) {
    int nSteps = (int) 10;
    int nPlayers = 2;

    Types.ACTIONS[] actions = new Types.ACTIONS[nPlayers];

    Gson gson = new Gson();

    Random rand = new Random();

    StateObservationMulti state = new StateObservationMulti();

    ElapsedCpuTimer t = new ElapsedCpuTimer();

    for (int i=0; i<nSteps; i++) {

      // for each step of the game choose a random action for each player

      for (int j=0; j<nPlayers; j++) {
        actions[j] = Types.AVAILABLE_ACTIONS.get(rand.nextInt(5));
      }

      // now advance the game to the next step

      state.advance(actions);
      String out = gson.toJson(state);

      System.out.println(out);



    }
    System.out.println(t);
    System.out.println();
  }
}
