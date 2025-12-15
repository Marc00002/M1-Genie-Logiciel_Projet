package re.forestier.edu.rpg.classes;

import re.forestier.edu.rpg.Ability;

import java.util.*;

public class Goblin extends AvatarClass {

    private static final Map<Integer, List<Ability>> ABILITIES_BY_LEVEL = new HashMap<>();

    static {
        ABILITIES_BY_LEVEL.put(1, Arrays.asList(
                new Ability(Ability.AbilityType.INT, 2),
                new Ability(Ability.AbilityType.ATK, 2),
                new Ability(Ability.AbilityType.ALC, 1)
        ));

        ABILITIES_BY_LEVEL.put(2, Arrays.asList(
                new Ability(Ability.AbilityType.ATK, 1),
                new Ability(Ability.AbilityType.ALC, 3)
        ));

        ABILITIES_BY_LEVEL.put(3, Arrays.asList(
                new Ability(Ability.AbilityType.VIS, 1)
        ));

        ABILITIES_BY_LEVEL.put(4, Arrays.asList(
                new Ability(Ability.AbilityType.DEF, 1)
        ));

        ABILITIES_BY_LEVEL.put(5, Arrays.asList(
                new Ability(Ability.AbilityType.ATK, 1),
                new Ability(Ability.AbilityType.DEF, 1)
        ));
    }

    public Goblin() {
        super(ABILITIES_BY_LEVEL);
    }
}
