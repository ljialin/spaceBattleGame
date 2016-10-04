package core.game;

import java.awt.*;

/**
 * Created by Jialin Liu on 04/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class StateObservation extends Game {
  protected ForwardModel model;

  public StateObservation() {
    super();
  }

  public StateObservation(ForwardModel _model) {
    this();
    this.model = _model;
  }

  public int getNoPlayers() {
    return this.no_players;
  }

  public StateObservation copy() {
    StateObservation copyObs = new StateObservation(model.copy());
    return copyObs;
  }

  public void draw(Graphics2D g) {
  }
}
