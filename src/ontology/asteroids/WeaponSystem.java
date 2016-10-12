package ontology.asteroids;

import tools.Utils;
import ontology.Types;

/**
 * Created by Jialin Liu on 06/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class WeaponSystem {
  /**
   * weaponId, destructive power, cost, resource, weapon cooldown time
   */
  private int weaponId;
  private int power;
  private int cost;
  private int resource;
  private int cooldown;

  WeaponSystem(int weaponId) {
    this.weaponId = weaponId;
    this.power = Types.RESOURCE_INFO.get(weaponId)[0];
    this.cost = Types.RESOURCE_INFO.get(weaponId)[1];
    this.resource = Types.RESOURCE_INFO.get(weaponId)[2];
    this.cooldown = 0;
  }

  public void resetCooldown() {
    this.cooldown = Types.RESOURCE_INFO.get(weaponId)[3];
  }

  public boolean fire() {
    boolean fire = canFire();
    if (fire) {
      this.resource--;
      resetCooldown();
    }
    return fire;
  }

  public boolean canFire() {
    return (this.resource>0 && this.cooldown==0);
  }

  public WeaponSystem copy() {
    WeaponSystem ws = new WeaponSystem(this.getWeaponId());
    ws.resource = this.resource;
    ws.cooldown = this.cooldown;
    return ws;
  }

  public int getWeaponId() {
    return weaponId;
  }

  public int getPower() {
    return power;
  }

  public int getCost() {
    return cost;
  }

  public int getResource() {
    return resource;
  }

  public int getCooldown() {
    return cooldown;
  }

  public void update() {
    this.cooldown--;
    this.cooldown = Utils.clamp(0, this.cooldown, Types.RESOURCE_INFO.get(weaponId)[3]);
  }
}
