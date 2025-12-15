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
import re.forestier.edu.rpg.classes.Goblin;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

public class UnitTests {

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
        return createPlayer(avatarClass, new ArrayList<>());
    }

    private static Player createPlayer(AvatarClass avatarClass, List<Item> inventory) {
        return new Player("Florian", "Grognak le barbare", avatarClass, 200, inventory);
    }

    private static Player createMoneyPlayer(int money) {
        return new Player("Florian", "Grognak le barbare",
                new Adventurer(), money, new ArrayList<>());
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

    private Player newPlayer(int currentWeight, int startingMoney) {
        Player p = new Player("Florian", "Grognak le barbare",
                new Adventurer(), startingMoney, new ArrayList<>());
        p.setCurrentWeight(currentWeight);
        return p;
    }

    private Item item(String name, int weight, int value) {
        return new Item(name, "no description", weight, value);
    }

    private static Player createGoblinPlayer() {
        return new Player("Florian", "Grognak le gobelin",
                new Goblin(), 200, new ArrayList<>());
    }


    @Test
    @DisplayName("Valid Player creation initializes fields")
    void validPlayerCreation() {
        List<Item> inventory = new ArrayList<>();
        Player p = new Player("Florian", "Grognak le barbare",
                new Archer(), 200, inventory);

        assertEquals("Florian", p.getPlayerName());
        assertEquals("Grognak le barbare", p.getAvatarName());
        assertTrue(p.getAvatarClass() instanceof Archer);
        assertEquals(200, p.getMoney());
        assertSame(inventory, p.getInventory());
        assertNotNull(p.getAbilities());
    }

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
            "DWARF,      Holy Elixir, 0, 11",
            "DWARF,      NONE,        0, 10",
            "ARCHER,     NONE,        0, 10",
            "ARCHER,     Magic Bow,   0, 20",
            "ADVENTURER, NONE,        0, 10",
            "ADVENTURER, NONE,       30, 11"
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

    @ParameterizedTest(
            name = "addItem: startWeight={0}, itemWeight={1} -> success={2}, finalWeight={3}"
    )
    @CsvSource({
            "0, 5,  true,  5",
            "95,10, false, 95"
    })
    @DisplayName("addItem respects weight limits (within or exceeds)")
    void addItem_weightCases(int startWeight,
                             int itemWeight,
                             boolean expectedSuccess,
                             int expectedFinalWeight) {
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
            "Magic Bow,       true",
            "NonExistentItem, false"
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

    @ParameterizedTest(
            name = "buyItem: money={0}, startWeight={1}, price={2}, weight={3} -> success={4}"
    )
    @CsvSource({
            "100,  0,  30,  2,  true",   // success
            "10,   0,  30,  2,  false",  // not enough money
            "1000,98, 100, 10, false",  // too heavy
            "100,  0, -10,  1, false"   // negative price
    })
    @DisplayName("buyItem handles success, not enough money, too heavy, and negative price")
    void buyItem_cases(int startingMoney, int startWeight, int price, int itemWeight, boolean expectedSuccess) {
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
