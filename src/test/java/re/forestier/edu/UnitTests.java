//package re.forestier.edu;
//
//import org.junit.jupiter.api.*;
//import re.forestier.edu.rpg.Player;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;
//import static org.junit.jupiter.api.Assertions.fail;
//
//import java.util.ArrayList;
//
//public class UnitTests {
//
//    @Test
//    @DisplayName("Sample test")
//    void testPlayerName() {
//        Player Player = new Player("Florian", "Grognak le barbare", "ADVENTURER", 100, new ArrayList<>());
//        assertThat(Player.playerName, is("Florian"));
//    }
//
//    @Test
//    @DisplayName("Impossible to have negative money")
//    void testNegativeMoney() {
//        Player p = new Player("Florian", "Grognak le barbare", "ADVENTURER", 100, new ArrayList<>());
//
//        try {
//            p.removeMoney(200);
//        } catch (IllegalArgumentException e) {
//            return;
//        }
//        fail();
//    }
//
//}
