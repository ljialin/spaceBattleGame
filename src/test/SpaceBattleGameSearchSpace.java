package test;

import bandits.SearchSpace;

/**
 * Created by Jialin Liu on 14/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class SpaceBattleGameSearchSpace implements SearchSpace {
  @Override
  public int nDims() {
    return 6;
  }

  @Override
  public int nValues(int idxDim) {
    int dim = (GameDesign.bounds[idxDim][1] - GameDesign.bounds[idxDim][0])
        / GameDesign.bounds[idxDim][2]
        + 1;
    return dim;
  }

  public int getValue(int idxDim, int idx) {
    return GameDesign.bounds[idxDim][0] + idx * GameDesign.bounds[idxDim][2];
  }

}
