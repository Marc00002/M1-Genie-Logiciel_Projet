package re.forestier.edu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import re.forestier.edu.rpg.Player;
import re.forestier.edu.rpg.UpdatePlayer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class UpdatePlayerTest {

    private static final String ADVENTURER = "ADVENTURER";
    private static final String ARCHER = "ARCHER";
    private static final String DWARF = "DWARF";

    private static final ArrayList<String> EXPECTED_ITEMS = new ArrayList<>(Arrays.asList(
            "Lookout Ring : Prevents surprise attacks",
            "Scroll of Stupidity : INT-2 when applied to an enemy",
            "Draupnir : Increases XP gained by 100%",
            "Magic Charm : Magic +10 for 5 rounds",
            "Rune Staff of Curse : May burn your ennemies... Or yourself. Who knows?",
            "Combat Edge : Well, that's an edge",
            "Holy Elixir : Recover your HP"
    ));

    // Test-only subclass -> to change after refactoring
    private static class TestPlayer extends Player {
        TestPlayer(String avatarClass, ArrayList<String> inventory) {
            super("Florian", "Grognak le barbare", avatarClass, 200, inventory);
        }

        void givenXp(int value) {
            this.xp = value;
        }
    }

    private static TestPlayer testPlayer(String avatarClass) {
        return new TestPlayer(avatarClass, new ArrayList<>());
    }

    private static TestPlayer testPlayer(String avatarClass, ArrayList<String> inventory) {
        return new TestPlayer(avatarClass, inventory);
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
        TestPlayer p = testPlayer(ADVENTURER);

        boolean leveled = UpdatePlayer.addXp(p, 5);

        assertFalse(leveled);
        assertEquals(5, p.getXp());
        assertTrue(p.getInventory().isEmpty());
    }

    @Test
    @DisplayName("addXp with DWARF level up adds item + upgrades abilities")
    void addXp_levelUp_dwarf_toLevel5() {
        ArrayList<String> inventory = new ArrayList<>();
        TestPlayer p = testPlayer(DWARF,inventory);

        p.givenXp(110);
        boolean leveled = UpdatePlayer.addXp(p, 1);

        assertTrue(leveled);
        assertEquals(111, p.getXp());
        assertEquals(5, p.retrieveLevel());

        assertThat(p.getInventory(), hasSize(1));
        assertThat(EXPECTED_ITEMS, hasItem(p.getInventory().get(0)));

        assertEquals(1, p.getAbilities().get("CHA"));
    }

    @Test
    @DisplayName("majFinDeTour when HP=0 prints KO and stops")
    void majFinDeTour_KO_prints() {
        TestPlayer p = testPlayer(ADVENTURER);
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

        ArrayList<String> inventory = new ArrayList<>();
        if (!"NONE".equals(item)) {
            inventory.add(item);
        }

        TestPlayer p = testPlayer(avatarClass, inventory);

        if (ARCHER.equals(avatarClass) && "Magic Bow".equals(item)) {
            p.setHealthPoints(40);
            p.setCurrentHealthPoints(18);
        } else {
            p.setHealthPoints(20);
            p.setCurrentHealthPoints(9);
        }

        p.givenXp(xp);

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
        TestPlayer p = testPlayer(ARCHER);
        p.setHealthPoints(hp);
        p.setCurrentHealthPoints(current);

        UpdatePlayer.majFinDeTour(p);

        assertEquals(expected, p.getCurrentHealthPoints());
    }

    //adventurer bug test (doesn't pass), should pass after fixing the bug
    @Test
    @DisplayName("Adventurer level 1 and level 2 abilities are correct")
    void adventurer_levelUp_updatesAbilities() {
        TestPlayer p = testPlayer(ADVENTURER);

        assertEquals(1, p.getAbilities().get("INT"));
        assertEquals(2, p.getAbilities().get("CHA"));

        p.givenXp(9);
        boolean leveled = UpdatePlayer.addXp(p, 1);

        assertTrue(leveled);
        assertEquals(2, p.retrieveLevel());

        assertEquals(2, p.getAbilities().get("INT"));
        assertEquals(3, p.getAbilities().get("CHA"));
    }


}
