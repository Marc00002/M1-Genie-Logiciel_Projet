package re.forestier.edu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import re.forestier.edu.rpg.Item;
import re.forestier.edu.rpg.Player;
import re.forestier.edu.rpg.classes.Adventurer;
import re.forestier.edu.rpg.classes.Archer;
import re.forestier.edu.rpg.classes.AvatarClass;
import re.forestier.edu.rpg.classes.Dwarf;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private static Player createPlayer(AvatarClass avatarClass) {
        return new Player("Florian", "Grognak le barbare", avatarClass, 200, new ArrayList<Item>());
    }
    private static Player createMoneyPlayer(int money) {
        return new Player("Florian", "Grognak le barbare", new Adventurer(), money, new ArrayList<Item>());
    }

    @Test
    @DisplayName("Valid Player creation initializes fields")
    void validPlayerCreation() {
        List<Item> inventory = new ArrayList<>();
        Player p = new Player("Florian", "Grognak le barbare", new Archer(), 200, inventory);

        assertEquals("Florian", p.getPlayerName());
        assertEquals("Grognak le barbare", p.getAvatarName());
        assertTrue(p.getAvatarClass() instanceof Archer);
        assertEquals(200, p.getMoney());
        assertSame(inventory, p.getInventory());
        assertNotNull(p.getAbilities());
    }

    // Old "invalid class" test no longer makes sense as we used AvatarClass, so we removed it.

    @ParameterizedTest(name = "addMoney: {0} + {1} → {2}")
    @CsvSource({
            "100, 50, 150",
            "100, 0, 100",
            "100, -30, 70"
    })
    void addMoney_characterization(int initial, int amount, int expected) {
        Player p = createMoneyPlayer(initial);
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
        Player p = createMoneyPlayer(initial);
        p.removeMoney(amount);
        assertEquals(expected, p.getMoney());
    }

    @Test
    @DisplayName("removeMoney throws if result would be negative")
    void removeMoneyTooMuchThrows() {
        Player p = createMoneyPlayer(50);
        assertThrows(IllegalArgumentException.class, () -> p.removeMoney(100));
    }

    @Test
    void defaultXpIsZero() {
        assertEquals(0, createPlayer(new Adventurer()).getXp());
    }

    @ParameterizedTest(name = "XP = {0} → retrieveLevel() = {1}")
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
        Player p = createPlayer(new Adventurer());
        p.setXp(xp);

        assertEquals(expected, p.retrieveLevel());
    }
}