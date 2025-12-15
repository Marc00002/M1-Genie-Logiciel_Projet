package re.forestier.edu.rpg.classes;

import re.forestier.edu.rpg.Ability;
import re.forestier.edu.rpg.Player;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public abstract class AvatarClass {
    protected final Map<Integer, List<Ability>> abilitiesByLevel;

    protected AvatarClass(Map<Integer, List<Ability>> abilitiesByLevel) {
        this.abilitiesByLevel = abilitiesByLevel;
    }

    public List<Ability> getAbilitiesForLevel(int level) {
        List<Ability> abilities = abilitiesByLevel.get(level);
        return new ArrayList<>(abilities);
    }
    public void healIfBelowHalf(Player player) {
        if (this instanceof LowHealthHealer healer) {
            healer.healBelowHalf(player);
        }
    }

}