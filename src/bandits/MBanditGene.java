package bandits;

import tools.Picker;
import tools.StatSummary;

import java.util.Random;

/**
 * Created by simonmarklucas on 13/08/2016.
 *
 * Edited by Jialin Liu  on 14/10/2016
 */
public class MBanditGene {
  public static final double HUGE_NEGATIVE = -1000000.0;
  public static final double HUGE_POSITIVE =  1000000.0;
  public static double eps = 2e-6;

  static Random random = new Random();

  // exploration factor for mutation operator
  static public double C = 2;

  public boolean canIdentical;

  // do binary version to begin with
  int nArms;

  // double[] rewards = new double[nArms];
  double[] deltaRewards;

  // note the number of times each arm has been pulled
  int[] armPulls;

  int nPulls;

  int x;  // arm index

  int xPrevious;  // previous pulled arm index

  // start all at one to avoid div zero
  int nMutations = 0;

  public MBanditGene(int nArms, boolean canIdentical) {
    this.canIdentical = canIdentical;
    this.nArms = nArms;
    armPulls = new int[nArms];
    deltaRewards = new double[nArms];
    randomize();
  }

  public MBanditGene(int nArms) {
    this(nArms, true);
  }

  public void randomize() {
    x = random.nextInt(nArms);
    armPulls[x]++;
    nPulls++;
  }

  public void resetX(int _x) {
    x = _x;
    armPulls[x]=1;
    nPulls=1;
  }

  public void mutate() {
    //randomMutate();
    banditMutate();
  }

  public int randomMutate() {
    int idx = random.nextInt(nArms);
    if(!canIdentical) {
      while(idx==x) {
          idx = random.nextInt(nArms);
      }
    }
    xPrevious = x;
    x = idx;
    armPulls[x]++;
    nPulls++;
    return x;
  }

  /**
   * select the mutated value using UCB
   * @return
   */
  public int banditMutate() {
    Picker<Integer> picker = new Picker<>(Picker.MAX_FIRST);
    if(canIdentical) {
      for (int i = 0; i < nArms; i++) {
        picker.add(ucb(i), i);
      }
    } else {
      for (int i = 0; i < nArms; i++) {
        if(i != x) {
          picker.add(ucb(i), i);
        }
      }
    }
    xPrevious = x;
    x = picker.getBest();
    armPulls[x]++;
    nPulls++;
    return x;
  }

  public double ucb(int idx) {
    double exploit = exploit(idx);
    double explore = exploreForMutation(idx);
    double noise = breakTies();
//    System.out.format("%d\t %.2f\t %.2f\t %f\n", idx, exploit, explore, exploit + explore + noise);
    return (exploit + explore + noise);
  }

  /**
   * Exploitation
   * @param idx
   * @return
   */
  public double exploit(int idx) {
    if(armPulls[idx] == 0) {
      return 0;
    }
    return deltaRewards[idx] / armPulls[idx];
  }

  /**
   * Exploration
   * @param idx
   * @return
   */
  public double exploreForMutation(int idx) {
    if(armPulls[idx] == 0) {
      return HUGE_POSITIVE;
    }
    return Math.sqrt(C * Math.log(nPulls) / armPulls[idx]);
  }

  /**
   * Random noise to break ties
   * @return
   */
    public double breakTies() {
        return random.nextDouble() * MBanditArray.eps;
    }

  /**
   * Mutate to the value indexed by idx
   * @param idx
   */
  public void mutateTo(int idx) {
    if(!canIdentical) {
      if(idx == x) {
        System.err.println("ERROR: The mutated one is identical to the current one: idx="
            + idx + "; current x=" + x + "; previous x=" + xPrevious);
        assert (idx != x);
      }
    }
    xPrevious = x;
    x = idx;
    armPulls[x]++;
    nPulls++;
  }

  /**
   * In bandit terms this would normally be called the exploit term but in an EA we need to use it
   * in the opposite sense since we need to stick with values that are already thought to be good
   * and instead modify ones that need to be rescued
   * @return
   */
  public double rescue() {
    if(nMutations == 0) {
      return 0;
    }
    return -maxDelta() / nMutations;
  }

  /**
   * Standard UCB Explore term
   * @param genomeNPulls
   * @return
   */
  public double exploreForUrgency(int genomeNPulls) {
    if(nPulls == 0) {
      return HUGE_POSITIVE;
    }
    return Math.sqrt(MBanditArray.k * Math.log(genomeNPulls) / nPulls);
  }

  /**
   *
   * @param genomeNPulls
   * @return
   */
  public double urgency(int genomeNPulls) {
    return rescue() + exploreForUrgency(genomeNPulls) + breakTies();
  }


  public double maxDelta() {
    StatSummary ss = new StatSummary();
    for (double d : deltaRewards) {
      ss.add(d);
    }
    return ss.max();
  }

  public double meanDelta() {
    StatSummary ss = new StatSummary();
    for (double d : deltaRewards) {
      ss.add(d);
    }
    return ss.mean();
  }

  public void applyReward(double delta) {
    deltaRewards[x] += delta;
//    deltaRewards[xPrevious] -= delta; // TODO: 14/10/2016 the penalty is necessary or not
  }

  public void revertOrKeep(double delta) {
    if (delta < 0) {
      x = xPrevious;
    }
  }

  public int getX() {
    return x;
  }

  public int getPreviousX() {
    return xPrevious;
  }

  public void setX(int _x) {
    this.x = _x;
  }
}
