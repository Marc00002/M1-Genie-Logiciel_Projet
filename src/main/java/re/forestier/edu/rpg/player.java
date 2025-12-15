package re.forestier.edu.rpg;

import re.forestier.edu.rpg.classes.AvatarClass;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String playerName;
    private String avatarName;
    private AvatarClass avatarClass;
    private Integer money;

    private int healthPoints;
    private int currentHealthPoints;
    private int xp;

    private ArrayList<Ability> abilities;
    private ArrayList<String> inventory;

    public Player(String playerName, String avatarName, AvatarClass avatarClass, int money, ArrayList<String> inventory) {
        this.playerName = playerName;
        this.avatarName = avatarName;
        this.avatarClass = avatarClass;
        this.money = money;
        this.inventory = inventory;
        this.abilities = new ArrayList<>();
        applyAbilities(avatarClass.getAbilitiesForLevel(1));
    }

    public String getPlayerName() { return playerName; }
    public String getAvatarName() { return avatarName; }
    public AvatarClass getAvatarClass() { return avatarClass; }

    public Integer getMoney() { return money; }
    public List<Ability> getAbilities() {
        return abilities;
    }
    public ArrayList<String> getInventory() { return inventory; }

    public int getHealthPoints() { return healthPoints; }
    public int getCurrentHealthPoints() { return currentHealthPoints; }
    public void setHealthPoints(int hp) { this.healthPoints = hp; }
    public void setCurrentHealthPoints(int hp) { this.currentHealthPoints = hp; }

    public int getXp() { return xp; }
    public void setXp(int xp) { this.xp = xp; }

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
        return LevelSystem.getLevel(getXp());
    }

    public void applyAbilities(List<Ability> deltas) {
        for (Ability delta : deltas) {
            applyAbilityDelta(delta);
        }
    }

    private void applyAbilityDelta(Ability delta) {
        Ability.AbilityType type = delta.getType();
        int valueToAdd = delta.getValue();

        Ability existing = findAbility(type);

        if (existing == null) {
            abilities.add(new Ability(type, valueToAdd));
        } else {
            existing.update(valueToAdd);
        }
    }


    private Ability findAbility(Ability.AbilityType type) {
        for (Ability a : abilities) {
            if (a.getType() == type) {
                return a;
            }
        }
        return null;
    }



    public int getAbilityValue(Ability.AbilityType type) {
        int total = 0;
        for (Ability a : abilities) {
            if (a.getType() == type) {
                total += a.getValue();
            }
        }
        return total;
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