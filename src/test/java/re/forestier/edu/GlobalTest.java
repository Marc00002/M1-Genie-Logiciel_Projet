package re.forestier.edu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.forestier.edu.rpg.*;
import re.forestier.edu.rpg.classes.Adventurer;
import re.forestier.edu.rpg.classes.Dwarf;

import java.util.ArrayList;
import java.util.List;

import static org.approvaltests.Approvals.verify;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;

public class GlobalTest {

    @Test
    @DisplayName("TextView: adventurer with empty inventory")
    void testAffichageBase() {
        Player Player = new Player("Florian", "Gnognak le Barbare", new Adventurer(), 200, new ArrayList<>());
        UpdatePlayer.addXp(Player, 20);
        Player.getInventory().clear();

        verify(TextView.afficherJoueur(Player));
    }
    @Test
    @DisplayName("MarkDownView: full markdown for a dwarf with items and HP")
    void testMarkdownViewDwarf() {
        List<Item> inventory = new ArrayList<>();
        inventory.add(Item.getItem("Holy Elixir"));
        inventory.add(Item.getItem("Combat Edge"));

        Player player = new Player("Florian", "Gnognak le Barbare", new Dwarf(), 150, inventory);
        player.setHealthPoints(30);
        player.setCurrentHealthPoints(12);
        player.setXp(57);

        MarkDownView view = new MarkDownView();
        String markdown = view.render(player);

        verify(markdown);
    }
    @Test
    @DisplayName("TextView: adventurer with items in inventory")
    void testTextViewWithItems() {
        List<Item> inventory = new ArrayList<>();
        inventory.add(Item.getItem("Holy Elixir"));
        inventory.add(Item.getItem("Combat Edge"));

        Player player = new Player("Florian", "Gnognak le Barbare", new Adventurer(), 200, inventory);

        verify(TextView.afficherJoueur(player));
    }

    @Test
    @DisplayName("MarkDownView: adventurer with empty inventory")
    void testMarkdownViewNoItems() {
        Player player = new Player("Florian", "Gnognak le Barbare", new Adventurer(), 200, new ArrayList<>());
        MarkDownView view = new MarkDownView();
        String markdown = view.render(player);

        verify(markdown);
    }


}