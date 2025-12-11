package re.forestier.edu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import re.forestier.edu.rpg.player;
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
    private static class TestPlayer extends player {
        TestPlayer(String playerName, String avatarName, String avatarClass, int money, ArrayList<String> inventory) {
            super(playerName, avatarName, avatarClass, money, inventory);
        }

        void givenXp(int value) {
            this.xp = value;
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
        TestPlayer p = new TestPlayer("Florian", "Grognak le barbare", ADVENTURER, 200, new ArrayList<>());

        boolean leveled = UpdatePlayer.addXp(p, 5);

        assertFalse(leveled);
        assertEquals(5, p.getXp());
        assertTrue(p.inventory.isEmpty());
    }

    @Test
    @DisplayName("addXp with DWARF level up adds item + upgrades abilities")
    void addXp_levelUp_dwarf_toLevel5() {
        ArrayList<String> inv = new ArrayList<>();
        TestPlayer p = new TestPlayer("Florian", "Grognak le barbare", DWARF, 200, inv);

        p.givenXp(110);
        boolean leveled = UpdatePlayer.addXp(p, 1);

        assertTrue(leveled);
        assertEquals(111, p.getXp());
        assertEquals(5, p.retrieveLevel());

        assertThat(p.inventory, hasSize(1));
        assertThat(EXPECTED_ITEMS, hasItem(p.inventory.get(0)));

        assertEquals(1, p.abilities.get("CHA"));
    }

    @Test
    @DisplayName("majFinDeTour when HP=0 prints KO and stops")
    void majFinDeTour_KO_prints() {
        TestPlayer p = new TestPlayer("Florian", "Grognak le barbare", ADVENTURER, 200, new ArrayList<>());
        p.healthpoints = 20;
        p.currenthealthpoints = 0;

        String output = captureOutput(() -> UpdatePlayer.majFinDeTour(p));

        assertEquals(0, p.currenthealthpoints);
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
    void majFinDeTour_underHalfHealth(String clazz, String item, int xp, int expectedHp) {

        ArrayList<String> inv = new ArrayList<>();
        if (!"NONE".equals(item)) {
            inv.add(item);
        }

        TestPlayer p = new TestPlayer("Florian", "Grognak le barbare", clazz, 200, inv);

        if (ARCHER.equals(clazz) && "Magic Bow".equals(item)) {
            p.healthpoints = 40;
            p.currenthealthpoints = 18; // <20
        } else {
            p.healthpoints = 20;
            p.currenthealthpoints = 9;  // <10
        }

        p.givenXp(xp);

        UpdatePlayer.majFinDeTour(p);

        assertEquals(expectedHp, p.currenthealthpoints);
    }

    @ParameterizedTest(name = "majFinDeTour HP boundaries: hp={0}, current={1} → {2}")
    @CsvSource({
            "20, 10, 10",
            "20, 20, 20",
            "20, 25, 20"
    })
    void majFinDeTour_boundaries(int hp, int current, int expected) {
        TestPlayer p = new TestPlayer("Florian", "Grognak le barbare", ARCHER, 200, new ArrayList<>());
        p.healthpoints = hp;
        p.currenthealthpoints = current;

        UpdatePlayer.majFinDeTour(p);

        assertEquals(expected, p.currenthealthpoints);
    }

}
