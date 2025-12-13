package re.forestier.edu.rpg.classes;

import re.forestier.edu.rpg.Player;

import java.util.Map;

public abstract class AvatarClass {
    public abstract void healBelowHalf(Player player);
    public abstract Map<String, Integer> getAbilitiesForLevel(int level);
}
