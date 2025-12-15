package re.forestier.edu.rpg;

import re.forestier.edu.rpg.classes.AvatarClass;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String playerName;
    private String avatarName;
    private AvatarClass avatarClass;
    private Money money;

    private ArrayList<Ability> abilities;
    private final List<Item> inventory;

    private int healthPoints;
    private int currentHealthPoints;
    private int xp;

    private int maxWeight = 100;
    private int currentWeight = 0;

    public Player(String playerName, String avatarName, AvatarClass avatarClass, int money, List<Item> inventory) {
        this.playerName = playerName;
        this.avatarName = avatarName;
        this.avatarClass = avatarClass;
        this.money = new Money(money);
        this.inventory = inventory;
        for (Item item : this.inventory) {
            this.currentWeight += item.getWeight();
        }
        this.abilities = new ArrayList<>();
        applyAbilities(avatarClass.getAbilitiesForLevel(1));
    }

    public String getPlayerName() { return playerName; }
    public String getAvatarName() { return avatarName; }
    public AvatarClass getAvatarClass() { return avatarClass; }

    public Integer getMoney() {return money.getAmount(); }
    public List<Ability> getAbilities() { return abilities; }
    public List<Item> getInventory() { return inventory; }

    public int getHealthPoints() { return healthPoints; }
    public int getCurrentHealthPoints() { return currentHealthPoints; }
    public void setHealthPoints(int hp) { this.healthPoints = hp; }
    public void setCurrentHealthPoints(int hp) { this.currentHealthPoints = hp; }

    public int getXp() { return xp; }
    public void setXp(int xp) { this.xp = xp; }

    public int getCurrentWeight() { return currentWeight; }
    public void setCurrentWeight(int currentWeight) { this.currentWeight = currentWeight; }

    public void addMoney(int amount) {
        money.addMoney(amount);
    }

    public void removeMoney(int amount) {
        money.removeMoney(amount);
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

    public boolean hasItemNamed(String name) {
        for (Item item : inventory) {
            if (item.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean addItem(Item item) {
        if (item == null) {
            return false;
        }

        if (currentWeight + item.getWeight() > maxWeight) {
            return false;
        }

        inventory.add(item);
        currentWeight += item.getWeight();
        return true;
    }

    public boolean removeItem(Item item) {
        if (!inventory.remove(item)) {
            return false;
        }
        currentWeight -= item.getWeight();
        return true;
    }

    public boolean sellItem(Item item) {
        if (item == null) {
            return false;
        }
        if (!inventory.remove(item)) {
            return false;
        }

        int value = item.getValue();
        money.addMoney(value);
        currentWeight -= item.getWeight();
        return true;
    }

    public boolean buyItem(Item item) {
        if (item == null) {
            return false;
        }

        Integer price = item.getValue();
        if (price < 0) {
            return false;
        }

        if (money.getAmount() < price) {
            return false;
        }

        if (currentWeight + item.getWeight() > maxWeight) {
            return false;
        }

        money.removeMoney(price);
        inventory.add(item);
        currentWeight += item.getWeight();

        return true;
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