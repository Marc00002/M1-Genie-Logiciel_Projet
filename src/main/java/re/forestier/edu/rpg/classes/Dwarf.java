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
        // Level 1: base stats from 0
        ABILITIES.put(1, Arrays.asList(
                new Ability(Ability.AbilityType.ALC, 4),
                new Ability(Ability.AbilityType.INT, 1),
                new Ability(Ability.AbilityType.ATK, 3)
        ));

        // Level 2: ALC 4→5 (+1), DEF 0→1 (+1)
        ABILITIES.put(2, Arrays.asList(
                new Ability(Ability.AbilityType.ALC, 1),
                new Ability(Ability.AbilityType.DEF, 1)
        ));

        // Level 3: ATK 3→4 (+1)
        ABILITIES.put(3, Arrays.asList(
                new Ability(Ability.AbilityType.ATK, 1)
        ));

        // Level 4: DEF 1→2 (+1)
        ABILITIES.put(4, Arrays.asList(
                new Ability(Ability.AbilityType.DEF, 1)
        ));

        // Level 5: CHA 0→1 (+1)
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
        if (player.getInventory().contains("Holy Elixir")) {
            hp += 1;
        }
        hp += 1;
        player.setCurrentHealthPoints(hp);
    }
}