package re.forestier.edu.rpg;

public class Affichage {

    public static String afficherJoueur(Player player) {
        final String[] finalString = {
                "Joueur " + player.getAvatarName() + " joué par " + player.getPlayerName()
        };

        finalString[0] += "\nNiveau : " + player.retrieveLevel()
                + " (XP totale : " + player.getXp() + ")";

        finalString[0] += "\n\nCapacités :";
        player.getAbilities().forEach(ability -> {
            // Ability.toString() returns "ATK : 3", etc.
            finalString[0] += "\n   " + ability.toString();
        });

        finalString[0] += "\n\nInventaire :";
        player.getInventory().forEach(item -> {
            finalString[0] += "\n   " + item;
        });

        return finalString[0];
    }
}
