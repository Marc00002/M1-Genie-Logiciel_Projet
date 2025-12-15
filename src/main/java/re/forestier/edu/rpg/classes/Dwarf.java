package re.forestier.edu.rpg.classes;

import re.forestier.edu.rpg.Ability;
import re.forestier.edu.rpg.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dwarf extends AvatarClass implements LowHealthHealer {

    private static final Map<Integer, List<Ability>> ABILITIES = new HashMap<>();

    static {
        ABILITIES.put(1, Arrays.asList(
                new Ability(Ability.AbilityType.ALC, 4),
                new Ability(Ability.AbilityType.INT, 1),
                new Ability(Ability.AbilityType.ATK, 3)
        ));

        ABILITIES.put(2, Arrays.asList(
                new Ability(Ability.AbilityType.ALC, 1),
                new Ability(Ability.AbilityType.DEF, 1)
        ));

        ABILITIES.put(3, Arrays.asList(
                new Ability(Ability.AbilityType.ATK, 1)
        ));

        ABILITIES.put(4, Arrays.asList(
                new Ability(Ability.AbilityType.DEF, 1)
        ));

        ABILITIES.put(5, Arrays.asList(
                new Ability(Ability.AbilityType.CHA, 1)
        ));
    }

    public Dwarf() {
        super(ABILITIES);
    }

    @Override
    public void healBelowHalf(Player player) {
        int hp = player.getCurrentHealthPoints();
        if (player.hasItemNamed("Holy Elixir")) {
            hp += 1;
        }
        hp += 1;
        player.setCurrentHealthPoints(hp);
    }
}