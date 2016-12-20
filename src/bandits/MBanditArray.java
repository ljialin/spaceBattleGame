package bandits;

import tools.Picker;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by simonmarklucas on 27/05/2016.
 *
 * Edited by Jialin Liu  on 14/10/2016
 */
public class MBanditArray extends BanditEA {

  public static double k = 2;  // exploration factor for urgency operator
  private int nBandits;
  private ArrayList<MBanditGene> genome;
  private Random random = new Random();

  public MBanditArray(int _nBandits) {
    super(_nBandits);
  }

  @Override
  public ArrayList<BanditGene> mutateGenome(int evalsSoFar) {
    return null;
  }

  public MBanditArray(SearchSpace searchSpace) {
    this.nBandits = searchSpace.nDims();
    genome = new ArrayList<>();
    urgencies = new ArrayList<>();
    for (int i=0; i<nBandits; i++) {
      genome.add(new MBanditGene(searchSpace.nValues(i)));
      urgencies.add(0.0);
    }
  }

  public int[] toArray() {
    int[] a = new int[nBandits];
    int ix = 0;
    for (MBanditGene gene : genome) {
      a[ix++] = gene.x;
    }
    return a;
  }

  public MBanditGene selectOneGeneToMutate(int genomeNPulls) {
    Picker<MBanditGene> picker = new Picker<>();
    for (MBanditGene gene : genome) {
      picker.add(gene.urgency(genomeNPulls), gene);
    }
    return picker.getBest();
  }

  public int selectOneGeneIdxToMutate(int genomeNPulls) {
    Picker<Integer> picker = new Picker<>();
    int idx = 0;
    for (MBanditGene gene : genome) {
      picker.add(gene.urgency(genomeNPulls), idx);
      idx++;
    }
    return picker.getBest();
  }

  public int selectRandomGeneIdx() {
    return random.nextInt(this.nBandits);
  }

  public double updateUrgency(int genomeNPulls) {
    double sum = 0.0;
    for (int i=0; i<genome.size(); i++) {
      double urgency = genome.get(i).urgency(genomeNPulls);
      this.urgencies.set(i,urgency);
      sum += urgency;
    }
    return sum;
  }

  public ArrayList<MBanditGene> getMGenome() {
    return genome;
  }


  public MBanditGene getGene(int idx) {
    return this.genome.get(idx);
  }

  @Override
  public ArrayList<BanditGene> selectGeneToMutate(int genomeNPulls) {
    // no use
    return new ArrayList<>();
  }



}