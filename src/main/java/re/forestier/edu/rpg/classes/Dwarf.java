package re.forestier.edu.rpg.classes;

import re.forestier.edu.rpg.Player;

import java.util.HashMap;
import java.util.Map;

public class Dwarf extends AvatarClass {

    private static final Map<Integer, Map<String,Integer>> ABILITIES = new HashMap<>();
    static {
        Map<String,Integer> lvl1 = new HashMap<>();
        lvl1.put("ALC", 4);
        lvl1.put("INT", 1);
        lvl1.put("ATK", 3);
        ABILITIES.put(1, lvl1);

        Map<String,Integer> lvl2 = new HashMap<>();
        lvl2.put("DEF", 1);
        lvl2.put("ALC", 5);
        ABILITIES.put(2, lvl2);

        Map<String,Integer> lvl3 = new HashMap<>();
        lvl3.put("ATK", 4);
        ABILITIES.put(3, lvl3);

        Map<String,Integer> lvl4 = new HashMap<>();
        lvl4.put("DEF", 2);
        ABILITIES.put(4, lvl4);

        Map<String,Integer> lvl5 = new HashMap<>();
        lvl5.put("CHA", 1);
        ABILITIES.put(5, lvl5);
    }

    @Override
    public void healBelowHalf(Player player) {
        int hp = player.getCurrentHealthPoints();
        if (player.getInventory().contains("Holy Elixir")) {
            hp += 1;
        }
        hp += 1;
        player.setCurrentHealthPoints(hp);
    }

    @Override
    public Map<String, Integer> getAbilitiesForLevel(int level) {
        return ABILITIES.get(level);
    }
}