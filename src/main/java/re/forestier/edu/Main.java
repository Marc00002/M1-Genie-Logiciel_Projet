package re.forestier.edu;
import re.forestier.edu.rpg.TextView;
import re.forestier.edu.rpg.UpdatePlayer;
import re.forestier.edu.rpg.Player;
import re.forestier.edu.rpg.classes.Dwarf;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Player firstPlayer = new Player("Florian", "Ruzberg de Rivehaute", new Dwarf(), 200, new ArrayList<>());
        firstPlayer.addMoney(400);

        UpdatePlayer.addXp(firstPlayer, 15);
        System.out.println(TextView.afficherJoueur(firstPlayer));
        System.out.println("------------------");
        UpdatePlayer.addXp(firstPlayer, 20);
        System.out.println(TextView.afficherJoueur(firstPlayer));
    }
}