package re.forestier.edu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import re.forestier.edu.rpg.Player;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private static final String ADVENTURER = "ADVENTURER";
    private static final String ARCHER = "ARCHER";
    private static final String DWARF = "DWARF";

    // Test-only subclass -> temporarily to change after refactoring
    private static class TestPlayer extends Player {
        TestPlayer(String avatarClass, int money, ArrayList<String> inventory) {
            super("Florian", "Grognak le barbare", avatarClass, money, inventory);
        }

        void givenXp(int value) {
            this.xp = value;
        }
    }

    private static TestPlayer testPlayer(String avatarClass, int money) {
        return new TestPlayer(avatarClass, money, new ArrayList<>());
    }

    private static TestPlayer testPlayer(String avatarClass, ArrayList<String> inventory) {
        return new TestPlayer(avatarClass, 200, inventory);
    }

    @Test
    @DisplayName("Valid Player creation initializes fields")
    void validPlayerCreation() {
        ArrayList<String> inventory = new ArrayList<>();
        TestPlayer p = testPlayer(ARCHER, inventory);

        assertEquals("Florian", p.getPlayerName());
        assertEquals("Grognak le barbare", p.getAvatarName());
        assertEquals(ARCHER, p.getAvatarClass());
        assertEquals(200, p.getMoney());
        assertSame(inventory, p.getInventory());
        assertNotNull(p.getAbilities());
    }

    @Test
    @DisplayName("Invalid class returns early and leaves fields null")
    void invalidClassLeavesDefaults() {
        TestPlayer p = testPlayer("INVALID",  new ArrayList<>());

        assertNull(p.getAvatarClass());
        assertNull(p.getPlayerName());
        assertNull(p.getAvatarName());
        assertNull(p.getInventory());
        assertNull(p.getMoney());
        assertNull(p.getAbilities());
    }

    @ParameterizedTest(name = "addMoney: {0} + {1} → {2}")
    @CsvSource({
            "100, 50, 150",
            "100, 0, 100",
            "100, -30, 70"
    })
    void addMoney_characterization(int initial, int amount, int expected) {
        TestPlayer p = testPlayer(ADVENTURER, initial);
        p.addMoney(amount);
        assertEquals(expected, p.getMoney());
    }

    @ParameterizedTest(name = "removeMoney: {0} - {1} → {2}")
    @CsvSource({
            "200, 50, 150",
            "200, 0, 200",
            "200, -50, 250",
            "200, 200, 0"
    })
    void removeMoney_validCases(int initial, int amount, int expected) {
        TestPlayer p = testPlayer(DWARF, initial);
        p.removeMoney(amount);
        assertEquals(expected, p.getMoney());
    }

    @Test
    @DisplayName("removeMoney throws if result would be negative")
    void removeMoneyTooMuchThrows() {
        TestPlayer p = testPlayer(ADVENTURER, 50);
        assertThrows(IllegalArgumentException.class, () -> p.removeMoney(100));
    }

    @Test
    void defaultXpIsZero() {
        assertEquals(0, testPlayer(ADVENTURER, 100).getXp());
    }

    @ParameterizedTest (name = "XP = {0} → retrieveLevel() = {1}")
    @CsvSource({
            "0, 1",
            "9, 1",
            "10, 2",
            "26, 2",
            "27, 3",
            "56, 3",
            "57, 4",
            "110, 4",
            "111, 5",
            "200, 5"
    })
    void retrieveLevel_boundaries(int xp, int expected) {
        TestPlayer p = testPlayer(ADVENTURER, 100);
        p.givenXp(xp);

        assertEquals(expected, p.retrieveLevel());
    }


}