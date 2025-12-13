package re.forestier.edu.rpg;

import re.forestier.edu.rpg.classes.Adventurer;
import re.forestier.edu.rpg.classes.Archer;
import re.forestier.edu.rpg.classes.AvatarClass;
import re.forestier.edu.rpg.classes.Dwarf;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    private String playerName;
    private String avatarName;
    private AvatarClass avatarClass;
    private Integer money;

    private int healthPoints;
    private int currentHealthPoints;
    protected int xp;

    private HashMap<String, Integer> abilities;
    private ArrayList<String> inventory;

    public Player(String playerName, String avatarName, AvatarClass avatarClass, int money, ArrayList<String> inventory) {
        this.playerName = playerName;
        this.avatarName = avatarName;
        this.avatarClass = avatarClass;
        this.money = money;
        this.inventory = inventory;
        this.abilities = new HashMap<>(avatarClass.getAbilitiesForLevel(1));
    }

    public String getPlayerName() { return playerName; }
    public String getAvatarName() { return avatarName; }
    public AvatarClass getAvatarClass() { return avatarClass; }

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
        money += amount;
    }

    public int retrieveLevel() {
        return LevelSystem.getLevel(xp);
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