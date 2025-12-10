package re.forestier.edu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import re.forestier.edu.rpg.player;
import re.forestier.edu.rpg.UpdatePlayer;

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
}
