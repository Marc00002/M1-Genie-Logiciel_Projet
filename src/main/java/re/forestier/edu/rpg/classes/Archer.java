package re.forestier.edu.rpg.classes;

import re.forestier.edu.rpg.Player;

import java.util.HashMap;
import java.util.Map;

public class Archer extends AvatarClass{
    private static final Map<Integer, Map<String,Integer>> ABILITIES = new HashMap<>();
    static {
        Map<String,Integer> lvl1 = new HashMap<>();
        lvl1.put("INT", 1);
        lvl1.put("ATK", 3);
        lvl1.put("CHA", 1);
        lvl1.put("VIS", 3);
        ABILITIES.put(1, lvl1);

        Map<String,Integer> lvl2 = new HashMap<>();
        lvl2.put("DEF", 1);
        lvl2.put("CHA", 2);
        ABILITIES.put(2, lvl2);

        Map<String,Integer> lvl3 = new HashMap<>();
        lvl3.put("ATK", 3);
        ABILITIES.put(3, lvl3);

        Map<String,Integer> lvl4 = new HashMap<>();
        lvl4.put("DEF", 2);
        ABILITIES.put(4, lvl4);

        Map<String,Integer> lvl5 = new HashMap<>();
        lvl5.put("ATK", 4);
        ABILITIES.put(5, lvl5);
    }

    @Override
    public void healBelowHalf(Player player) {
        int hp = player.getCurrentHealthPoints();
        hp += 1;
        if (player.getInventory().contains("Magic Bow")) {
            hp += hp / 8 - 1;
        }
        player.setCurrentHealthPoints(hp);
    }

    @Override
    public Map<String, Integer> getAbilitiesForLevel(int level) {
        return ABILITIES.get(level);
    }
}
