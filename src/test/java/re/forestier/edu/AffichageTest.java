package re.forestier.edu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.forestier.edu.rpg.Affichage;
import re.forestier.edu.rpg.Player;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AffichageTest {

    @Test
    @DisplayName("afficherJoueur formats header, level, abilities and inventory")
    void afficherJoueur_formatsPlayerInfo() {        Player p = new Player("Florian", "Grognak le barbare", "ARCHER", 200, new ArrayList<>());

        p.getAbilities().clear();
        p.getAbilities().put("ATK", 3);

        p.getInventory().add("Magic Bow");

        String s = Affichage.afficherJoueur(p);

        assertNotNull(s);
        assertTrue(s.contains("Joueur Grognak le barbare joué par Florian"));
        assertTrue(s.contains("Niveau : 1 (XP totale : 0)"));
        assertTrue(s.contains("ATK : 3"));
        assertTrue(s.contains("Magic Bow"));
    }

}
