package re.forestier.edu.rpg;

import java.util.List;

public class MarkDownView {

    public String render(Player player) {
        StringBuilder sb = new StringBuilder();

        sb.append("# Joueur ")
                .append(player.getAvatarName())
                .append(" joué par ")
                .append(player.getPlayerName())
                .append("\n\n");

        sb.append("## Niveau : ")
                .append(player.retrieveLevel())
                .append(" (XP totale : ")
                .append(player.getXp())
                .append(")\n");

        sb.append("- Classe : ")
                .append(player.getAvatarClass().getClass().getSimpleName())
                .append("\n");

        sb.append("- Points de vie : ")
                .append(player.getCurrentHealthPoints())
                .append(" / ")
                .append(player.getHealthPoints())
                .append("\n");

        sb.append("- Argent : ")
                .append(player.getMoney())
                .append("\n\n");

        sb.append("## Capacités :\n");
        List<Ability> abilities = player.getAbilities();

        for (Ability ability : abilities) {
            sb.append("- **")
                    .append(ability.getType().name())
                    .append("** : ")
                    .append(ability.getValue())
                    .append("\n");
        }

        sb.append("\n## Inventaire :\n");
        List<Item> inventory = player.getInventory();
        if (inventory.isEmpty()) {
            sb.append("_Inventaire vide._\n");
        } else {
            for (Item item : inventory) {
                sb.append("- ")
                        .append(item.getName());
                sb.append(" — ").append(item.getDescription());
                sb.append(" (poids : ")
                        .append(item.getWeight())
                        .append(", valeur : ")
                        .append(item.getValue())
                        .append(")\n");
            }
        }

        return sb.toString();
    }
}
