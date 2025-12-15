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
import re.forestier.edu.rpg.classes.Goblin;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TDD {

    private static final Set<String> EXPECTED_NAMES = new HashSet<>(Arrays.asList(
            "Lookout Ring",
            "Scroll of Stupidity",
            "Draupnir",
            "Magic Charm",
            "Rune Staff of Curse",
            "Combat Edge",
            "Holy Elixir",
            "Magic Bow"
    ));

    private static Player createGoblinPlayer() {
        return new Player("Florian", "Grognak le gobelin", new Goblin(), 200, new ArrayList<>());
    }

    @Test
    @DisplayName("Goblin level 1 abilities match original values")
    void goblinLevel1Abilities() {
        Player p = createGoblinPlayer();

        assertEquals(2, p.getAbilityValue(Ability.AbilityType.INT));
        assertEquals(2, p.getAbilityValue(Ability.AbilityType.ATK));
        assertEquals(1, p.getAbilityValue(Ability.AbilityType.ALC));

        assertEquals(0, p.getAbilityValue(Ability.AbilityType.DEF));
        assertEquals(0, p.getAbilityValue(Ability.AbilityType.VIS));
        assertEquals(0, p.getAbilityValue(Ability.AbilityType.CHA));
    }

    @Test
    @DisplayName("Goblin level 2 abilities after XP gain are correct")
    void goblinLevel2Abilities() {
        Player p = createGoblinPlayer();

        p.setXp(9);
        boolean leveled = UpdatePlayer.addXp(p, 1);

        assertTrue(leveled);
        assertEquals(2, p.retrieveLevel());

        assertEquals(2, p.getAbilityValue(Ability.AbilityType.INT));
        assertEquals(3, p.getAbilityValue(Ability.AbilityType.ATK));
        assertEquals(4, p.getAbilityValue(Ability.AbilityType.ALC));
    }

    @Test
    @DisplayName("Goblin does not heal at end of turn when low HP")
    void goblinDoesNotHealAtEndOfTurn() {
        Player p = createGoblinPlayer();
        p.setHealthPoints(20);
        p.setCurrentHealthPoints(9);

        UpdatePlayer.majFinDeTour(p);

        assertEquals(9, p.getCurrentHealthPoints());
    }

    private Player newPlayer(int currentWeight, int startingMoney) {
        Player p = new Player("Florian", "Grognak le barbare", new Adventurer(), startingMoney, new ArrayList<>());
        p.setCurrentWeight(currentWeight);
        return p;
    }

    private Item item(String name, int weight, int value) {
        return new Item(name,"no description", weight, value);
    }


    @ParameterizedTest(name = "addItem: startWeight={0}, itemWeight={1} -> success={2}, finalWeight={3}")
    @CsvSource({
            "0,   5,  true,  5",
            "95, 10, false, 95"
    })
    @DisplayName("addItem respects weight limits (within or exceeds)")
    void addItem_weightCases(int startWeight, int itemWeight, boolean expectedSuccess, int expectedFinalWeight) {

        Player p = newPlayer(startWeight, 0);
        Item item = item("Any", itemWeight, 10);

        boolean result = p.addItem(item);

        assertEquals(expectedSuccess, result);
        assertEquals(expectedFinalWeight, p.getCurrentWeight());
        if (expectedSuccess) {
            assertTrue(p.getInventory().contains(item));
        } else {
            assertFalse(p.getInventory().contains(item));
        }
    }

    @Test
    @DisplayName("addItem(null) returns false and does nothing")
    void addItem_null() {
        Player p = newPlayer(0, 0);

        boolean result = p.addItem(null);

        assertFalse(result);
        assertEquals(0, p.getCurrentWeight());
        assertTrue(p.getInventory().isEmpty());
    }


    @Test
    @DisplayName("removeItem removes item and decreases currentWeight")
    void removeItem_existing() {
        Player p = newPlayer(20, 0);
        Item ring = item("Ring", 2, 5);
        p.addItem(ring);

        boolean removed = p.removeItem(ring);

        assertTrue(removed);
        assertEquals(20, p.getCurrentWeight());
        assertFalse(p.getInventory().contains(ring));
    }

    @Test
    @DisplayName("removeItem on item not in inventory returns false and does not change weight")
    void removeItem_notInInventory() {
        Player p = newPlayer(0, 0);
        Item ring = item("Ring", 2, 5);

        boolean removed = p.removeItem(ring);

        assertFalse(removed);
        assertEquals(0, p.getCurrentWeight());
        assertTrue(p.getInventory().isEmpty());
    }


    @ParameterizedTest(name = "getItem(\"{0}\") -> found={1}")
    @CsvSource({
            "Magic Bow,      true",
            "NonExistentItem,false"
    })
    @DisplayName("getItem returns correct Item or null based on name")
    void getItem_foundOrNot(String name, boolean shouldExist) {
        Item result = Item.getItem(name);

        if (shouldExist) {
            assertNotNull(result);
            assertEquals(name, result.getName());
        } else {
            assertNull(result);
        }
    }


    @Test
    @DisplayName("sellItem removes item, decreases weight and increases money")
    void sellItem_success() {
        Player p = newPlayer(50, 100);
        Item charm = item("Magic Charm", 3, 40);
        p.addItem(charm);

        int weightBefore = p.getCurrentWeight();
        int moneyBefore = p.getMoney();

        boolean sold = p.sellItem(charm);

        assertTrue(sold);
        assertFalse(p.getInventory().contains(charm));
        assertEquals(weightBefore - charm.getWeight(), p.getCurrentWeight());
        assertEquals(moneyBefore + charm.getValue(), p.getMoney());
    }

    @Test
    @DisplayName("sellItem returns false and does nothing when item is null")
    void sellItem_null() {
        Player p = newPlayer(50, 100);
        int weightBefore = p.getCurrentWeight();
        int moneyBefore = p.getMoney();

        boolean sold = p.sellItem(null);

        assertFalse(sold);
        assertEquals(weightBefore, p.getCurrentWeight());
        assertEquals(moneyBefore, p.getMoney());
    }

    @Test
    @DisplayName("sellItem returns false and does nothing when player does not own the item")
    void sellItem_notOwned() {
        Player p = newPlayer(50, 100);
        Item charm = item("Magic Charm", 3, 40); // not added to inventory

        int weightBefore = p.getCurrentWeight();
        int moneyBefore = p.getMoney();

        boolean sold = p.sellItem(charm);

        assertFalse(sold);
        assertEquals(weightBefore, p.getCurrentWeight());
        assertEquals(moneyBefore, p.getMoney());
    }

    @ParameterizedTest(name = "buyItem: money={0}, startWeight={1}, price={2}, weight={3} -> success={4}")
    @CsvSource({
            "100, 0,  30,  2,  true",
            "10,  0,  30,  2,  false",
            "1000,98, 100, 10, false",
            "100, 0, -10,  1,  false"
    })
    @DisplayName("buyItem handles success, not enough money, too heavy, and negative price")
    void buyItem_cases(int startingMoney,
                       int startWeight,
                       int price,
                       int itemWeight,
                       boolean expectedSuccess) {

        Player p = newPlayer(startWeight, startingMoney);
        Item item = item("TestItem", itemWeight, price);

        int weightBefore = p.getCurrentWeight();
        int moneyBefore = p.getMoney();

        boolean bought = p.buyItem(item);

        assertEquals(expectedSuccess, bought);

        if (expectedSuccess) {
            assertTrue(p.getInventory().contains(item));
            assertEquals(weightBefore + itemWeight, p.getCurrentWeight());
            assertEquals(moneyBefore - price, p.getMoney());
        } else {
            assertFalse(p.getInventory().contains(item));
            assertEquals(weightBefore, p.getCurrentWeight());
            assertEquals(moneyBefore, p.getMoney());
        }
    }


    @Test
    @DisplayName("buyItem(null) returns false and does nothing")
    void buyItem_null() {
        Player p = newPlayer(50, 100);
        int weightBefore = p.getCurrentWeight();
        int moneyBefore = p.getMoney();

        boolean bought = p.buyItem(null);

        assertFalse(bought);
        assertEquals(weightBefore, p.getCurrentWeight());
        assertEquals(moneyBefore, p.getMoney());
    }
}