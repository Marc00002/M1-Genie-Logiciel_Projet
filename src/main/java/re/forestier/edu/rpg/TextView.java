package re.forestier.edu.rpg;

public class TextView {

        private TextView() {}

    public static String afficherJoueur(Player player) {
        StringBuilder sb = new StringBuilder();

        sb.append("Joueur ")
                .append(player.getAvatarName())
                .append(" joué par ")
                .append(player.getPlayerName());

        sb.append("\nNiveau : ")
                .append(player.retrieveLevel())
                .append(" (XP totale : ")
                .append(player.getXp())
                .append(")");

        sb.append("\n\nCapacités :");

        Ability.AbilityType[] ORDER = {
                Ability.AbilityType.DEF,
                Ability.AbilityType.ATK,
                Ability.AbilityType.CHA,
                Ability.AbilityType.INT,
                Ability.AbilityType.ALC,
                Ability.AbilityType.VIS
        };

        for (Ability.AbilityType type : ORDER) {
            int value = player.getAbilityValue(type);
            if (value > 0) {
                sb.append("\n   ")
                        .append(type.name())
                        .append(" : ")
                        .append(value);
            }
        }

        sb.append("\n\nInventaire :");
        player.getInventory().forEach(item ->
                sb.append("\n   ").append(item.getName())
        );

        return sb.toString();
    }
}
