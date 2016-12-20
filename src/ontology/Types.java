package ontology;

import tools.Direction;
import tools.Vector2d;

import java.awt.Graphics2D;
import java.awt.Color;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Diego
 * Date: 17/10/13
 * Time: 11:05
 * This is a Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 *
 * Edited by Jialin Liu on 04/10/2016.
 */
public class Types {
  public static final Color GREEN = new Color(0, 200, 0);
  public static final Color BLUE = new Color(0, 0, 200);
  public static final Color RED = new Color(200, 0, 0);
  public static final Color GRAY = new Color(90, 90, 90);
  public static final Color WHITE = new Color(250, 250, 250);
  public static final Color BROWN = new Color(140, 120, 100);
  public static final Color BLACK = new Color(0, 0, 0);
  public static final Color ORANGE = new Color(250, 160, 0);
  public static final Color YELLOW = new Color(250, 250, 0);
  public static final Color PINK = new Color(250, 200, 200);
  public static final Color GOLD = new Color(250, 212, 0);
  public static final Color LIGHTRED = new Color(250, 50, 50);
  public static final Color LIGHTORANGE = new Color(250, 200, 100);
  public static final Color LIGHTBLUE = new Color(50, 100, 250);
  public static final Color LIGHTGREEN = new Color(50, 250, 50);
  public static final Color DARKGREEN = new Color(35, 117, 29);
  public static final Color LIGHTYELLOW = new Color(255, 250, 128);
  public static final Color LIGHTGRAY = new Color(238, 238, 238);
  public static final Color DARKGRAY = new Color(30, 30, 30);
  public static final Color DARKBLUE = new Color(20, 20, 100);

  public static final Vector2d NIL = new Vector2d(0, 0);
  public static final Vector2d THRUST = new Vector2d(0, 1);
  public static final Vector2d RIGHT = new Vector2d(1, 0);
  public static final Vector2d LEFT = new Vector2d(-1, 0);
  public static final Vector2d FIRE = new Vector2d(0, 0);
  public static final Vector2d[] BASE_DIRS = new Vector2d[]{THRUST, LEFT, RIGHT, FIRE};


  public static final Direction DNIL = new Direction(0, 0);
  public static final Direction DRIGHT = new Direction(1, 0);
  public static final Direction DLEFT = new Direction(-1, 0);
  public static final Direction DTHRUST = new Direction(0, 1);
  public static final Direction DFIRE = new Direction(0, 0);
  public static final Direction[] DBASEDIRS = new Direction[]{DTHRUST, DLEFT, DRIGHT, DFIRE};

  public static Color[] PLAYER_COLOR = new Color[]{
      BLUE, GREEN, ORANGE, PINK
  };

  public static int[][] ALL_ACTIONS = new int[][]{
      {KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE},
      {KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SHIFT}
  };

  public static enum ACTIONS {
    ACTION_NIL(new int[]{0, 0}),
    ACTION_THRUST(new int[]{KeyEvent.VK_UP, KeyEvent.VK_W}),
    ACTION_LEFT(new int[]{KeyEvent.VK_LEFT, KeyEvent.VK_A}),
    ACTION_RIGHT(new int[]{KeyEvent.VK_RIGHT, KeyEvent.VK_D}),
    ACTION_FIRE(new int[]{KeyEvent.VK_SPACE, KeyEvent.VK_SHIFT});
    //ACTION_ESCAPE(new int[]{KeyEvent.VK_ESCAPE, KeyEvent.VK_ESCAPE});

    private int[] key;

    ACTIONS(int[] numVal) {
      this.key = numVal;
    }

    public int[] getKey() {
      return this.key;
    }

    public static ACTIONS fromString(String strKey) {
      if (strKey.equalsIgnoreCase("ACTION_THRUST")) return ACTION_THRUST;
      else if (strKey.equalsIgnoreCase("ACTION_LEFT")) return ACTION_LEFT;
      else if (strKey.equalsIgnoreCase("ACTION_RIGHT")) return ACTION_RIGHT;
      else if (strKey.equalsIgnoreCase("ACTION_FIRE")) return ACTION_FIRE;
        //else if (strKey.equalsIgnoreCase("ACTION_ESCAPE")) return ACTION_ESCAPE;
      else return ACTION_NIL;
    }

    public static ACTIONS fromVector(Direction move) {
      // Probably better to use .equals() instead of == to test for equality,
      // but not necessary for the current call hierarchy of this method
      if (move.equals(DTHRUST)) return ACTION_THRUST;
      else if (move.equals(DLEFT)) return ACTION_LEFT;
      else if (move.equals(DRIGHT)) return ACTION_RIGHT;
      else return ACTION_NIL;
    }
  }

  public static enum WINNER {
    PLAYER_DISQ(-100),
    NO_WINNER(-1),
    PLAYER_LOSES(0),
    PLAYER_WINS(1);

    private int key;

    WINNER(int val) {
      key = val;
    }

    public int key() {
      return key;
    }
  }


  public static HashMap<Integer, int[]> RESOURCE_INFO = new HashMap<Integer, int[]>() {
    {
      put(Constants.WEAPON_ID_MISSILE, new int[]{Constants.MISSILE_DESTRUCTIVE_POWER,
          Constants.MISSILE_COST, Constants.MISSILE_MAX_RESOURCE, Constants.MISSILE_COOLDOWN});
    }
  };

  public static ArrayList<ACTIONS> AVAILABLE_ACTIONS = new ArrayList<ACTIONS>() {{
    add(Types.ACTIONS.ACTION_NIL);
    add(Types.ACTIONS.ACTION_LEFT);
    add(Types.ACTIONS.ACTION_RIGHT);
    add(Types.ACTIONS.ACTION_THRUST);
    add(Types.ACTIONS.ACTION_FIRE);
  }};
}
