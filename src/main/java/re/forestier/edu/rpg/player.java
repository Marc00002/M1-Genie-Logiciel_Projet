package re.forestier.edu.rpg;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    private String playerName;
    private String avatarName;
    private String avatarClass;
    private Integer money;

    private int healthPoints;
    private int currentHealthPoints;
    protected int xp;

    private HashMap<String, Integer> abilities;
    private ArrayList<String> inventory;

    private static final HashMap<Integer,Integer> LEVEL_THRESHOLDS = new HashMap<>();
    static {
        LEVEL_THRESHOLDS.put(2,10);
        LEVEL_THRESHOLDS.put(3,27);
        LEVEL_THRESHOLDS.put(4,57);
        LEVEL_THRESHOLDS.put(5,111);
    }

    public Player(String playerName, String avatarName, String avatarClass, int money, ArrayList<String> inventory) {
        if (!avatarClass.equals("ARCHER") && !avatarClass.equals("ADVENTURER") && !avatarClass.equals("DWARF") ) {
            return;
        }

        this.playerName = playerName;
        this.avatarName = avatarName;
        this.avatarClass = avatarClass;
        this.money = money;
        this.inventory = inventory;
        this.abilities = UpdatePlayer.abilitiesPerTypeAndLevel().get(this.avatarClass).get(1);
    }

    public String getPlayerName() { return playerName; }
    public String getAvatarName() { return avatarName; }
    public String getAvatarClass() { return avatarClass; }

    public Integer getMoney() { return money; }
    public HashMap<String,Integer> getAbilities() { return abilities; }
    public ArrayList<String> getInventory() { return inventory; }

    public int getHealthPoints() { return healthPoints; }
    public int getCurrentHealthPoints() { return currentHealthPoints; }
    public void setHealthPoints(int hp) { this.healthPoints = hp; }
    public void setCurrentHealthPoints(int hp) { this.currentHealthPoints = hp; }

    public int getXp() { return xp; }

    public void removeMoney(int amount) {
        int result = money - amount;
        if (result < 0) {
            throw new IllegalArgumentException("Player can't have a negative money!");
        }
        money = result;
    }
    public void addMoney(int amount) {
        money += amount; // negative values still allowed, as in your tests
    }

    public int retrieveLevel() {
        if (xp < LEVEL_THRESHOLDS.get(2)) {
            return 1;
        } else if (xp < LEVEL_THRESHOLDS.get(3)) {
            return 2;
        } else if (xp < LEVEL_THRESHOLDS.get(4)) {
            return 3;
        } else if (xp < LEVEL_THRESHOLDS.get(5)) {
            return 4;
        }
        return 5;
    }

    /*
    Ингредиенты:
        Для теста:

            250 г муки
            125 г сливочного масла (холодное)
            70 г сахара
            1 яйцо
            1 щепотка соли
     */

}