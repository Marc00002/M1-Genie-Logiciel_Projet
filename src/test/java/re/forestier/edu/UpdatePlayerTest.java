package re.forestier.edu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import re.forestier.edu.rpg.Ability;
import re.forestier.edu.rpg.Item;
import re.forestier.edu.rpg.Player;
import re.forestier.edu.rpg.UpdatePlayer;
import re.forestier.edu.rpg.classes.Adventurer;
import re.forestier.edu.rpg.classes.Archer;
import re.forestier.edu.rpg.classes.AvatarClass;
import re.forestier.edu.rpg.classes.Dwarf;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class UpdatePlayerTest {

    private static final String ADVENTURER = "ADVENTURER";
    private static final String ARCHER = "ARCHER";
    private static final String DWARF = "DWARF";

    private static final List<String> EXPECTED_ITEM_NAMES = Arrays.asList(
            "Lookout Ring",
            "Scroll of Stupidity",
            "Draupnir",
            "Magic Charm",
            "Rune Staff of Curse",
            "Combat Edge",
            "Holy Elixir",
            "Magic Bow"
    );

    private static Player createPlayer(AvatarClass avatarClass) {
        return new Player("Florian", "Grognak le barbare", avatarClass, 200, new ArrayList<>());
    }

    private static Player createPlayer(AvatarClass avatarClass, List<Item> inventory) {
        return new Player("Florian", "Grognak le barbare", avatarClass, 200, inventory);
    }

    private static AvatarClass toAvatarClass(String name) {
        switch (name) {
            case DWARF:
                return new Dwarf();
            case ARCHER:
                return new Archer();
            case ADVENTURER:
                return new Adventurer();
            default:
                return null;
        }
    }

    private static String captureOutput(Runnable action) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(out));
        try {
            action.run();
        } finally {
            System.setOut(old);
        }
        return out.toString();
    }

    @Test
    @DisplayName("addXp without level up -> returns false for level up and adds no item")
    void addXp_noLevelUp() {
        Player p = createPlayer(new Adventurer());

        boolean leveled = UpdatePlayer.addXp(p, 5);

        assertFalse(leveled);
        assertEquals(5, p.getXp());
        assertTrue(p.getInventory().isEmpty());
    }

    @Test
    @DisplayName("addXp with DWARF level up adds item + upgrades abilities")
    void addXp_levelUp_dwarf_toLevel5() {
        List<Item> inventory = new ArrayList<>();
        Player p = createPlayer(new Dwarf(), inventory);

        p.setXp(110);
        boolean leveled = UpdatePlayer.addXp(p, 1);

        assertTrue(leveled);
        assertEquals(111, p.getXp());
        assertEquals(5, p.retrieveLevel());

        assertThat(p.getInventory(), hasSize(1));
        String itemName = p.getInventory().get(0).getName();
        assertThat(EXPECTED_ITEM_NAMES, hasItem(itemName));

        assertEquals(1, p.getAbilityValue(Ability.AbilityType.CHA));
    }

    @Test
    @DisplayName("majFinDeTour when HP=0 prints KO and stops")
    void majFinDeTour_KO_prints() {
        Player p = createPlayer(new Adventurer());
        p.setHealthPoints(20);
        p.setCurrentHealthPoints(0);

        String output = captureOutput(() -> UpdatePlayer.majFinDeTour(p));

        assertEquals(0, p.getCurrentHealthPoints());
        assertTrue(output.contains("Le joueur est KO !"));
    }

    @ParameterizedTest(name = "majFinDeTour: class={0}, item={1}, xp={2} → HP={3}")
    @CsvSource({
            "DWARF, Holy Elixir, 0, 11",
            "DWARF, NONE,        0, 10",
            "ARCHER, NONE,       0, 10",
            "ARCHER, Magic Bow,  0, 20",
            "ADVENTURER, NONE,   0, 10",
            "ADVENTURER, NONE,  30, 11"
    })
    void majFinDeTour_underHalfHealth(String avatarClass, String item, int xp, int expectedHp) {

        List<Item> inventory = new ArrayList<>();
        if (!"NONE".equals(item)) {
            inventory.add(Item.getItem(item));
        }

        Player p = createPlayer(toAvatarClass(avatarClass), inventory);

        if (ARCHER.equals(avatarClass) && "Magic Bow".equals(item)) {
            p.setHealthPoints(40);
            p.setCurrentHealthPoints(18);
        } else {
            p.setHealthPoints(20);
            p.setCurrentHealthPoints(9);
        }

        p.setXp(xp);

        UpdatePlayer.majFinDeTour(p);

        assertEquals(expectedHp, p.getCurrentHealthPoints());
    }

    @ParameterizedTest(name = "majFinDeTour HP boundaries: hp={0}, current={1} → {2}")
    @CsvSource({
            "20, 10, 10",
            "20, 20, 20",
            "20, 25, 20"
    })
    void majFinDeTour_boundaries(int hp, int current, int expected) {
        Player p = createPlayer(new Archer());
        p.setHealthPoints(hp);
        p.setCurrentHealthPoints(current);

        UpdatePlayer.majFinDeTour(p);

        assertEquals(expected, p.getCurrentHealthPoints());
    }

    @Test
    @DisplayName("Adventurer level 1 and level 2 abilities are correct")
    void adventurer_levelUp_updatesAbilities() {
        Player p = createPlayer(new Adventurer());

        assertEquals(1, p.getAbilityValue(Ability.AbilityType.INT));
        assertEquals(2, p.getAbilityValue(Ability.AbilityType.CHA));

        p.setXp(9);
        boolean leveled = UpdatePlayer.addXp(p, 1);

        assertTrue(leveled);
        assertEquals(2, p.retrieveLevel());

        assertEquals(2, p.getAbilityValue(Ability.AbilityType.INT));
        assertEquals(3, p.getAbilityValue(Ability.AbilityType.CHA));
    }
}