package re.forestier.edu.rpg;

import re.forestier.edu.rpg.classes.AvatarClass;
import re.forestier.edu.rpg.classes.LowHealthHealer;

import java.util.Map;
import java.util.Random;

public class UpdatePlayer {

    private final static String[] objectList = {
            "Lookout Ring : Prevents surprise attacks",
            "Scroll of Stupidity : INT-2 when applied to an enemy",
            "Draupnir : Increases XP gained by 100%",
            "Magic Charm : Magic +10 for 5 rounds",
            "Rune Staff of Curse : May burn your ennemies... Or yourself. Who knows?",
            "Combat Edge : Well, that's an edge",
            "Holy Elixir : Recover your HP"
            };

    public static boolean addXp(Player player, int xp) {
        int currentLevel = player.retrieveLevel();
        int newLevel = updateXpAndGetNewLevel(player, xp);

        if (newLevel == currentLevel) {
            return false;
        }

        giveRandomItem(player);
        applyLevelUpBonuses(player, currentLevel, newLevel);
        return true;
    }

    private static int updateXpAndGetNewLevel(Player player, int xpGain) {
        int newXp = player.getXp() + xpGain;
        player.setXp(newXp);
        return player.retrieveLevel();
    }

    private static void applyLevelUpBonuses(Player player, int fromLevel, int toLevel) {
        AvatarClass avatarClass = player.getAvatarClass();
        for (int lvl = fromLevel + 1; lvl <= toLevel; lvl++) {
            player.applyAbilities(avatarClass.getAbilitiesForLevel(lvl));
        }
    }

    private static void giveRandomItem(Player player) {
        Item randomItem = Item.randomItem();
        player.getInventory().add(randomItem);
    }

    public static void majFinDeTour(Player player) {
        if (player.getCurrentHealthPoints() == 0) {
            System.out.println("Le joueur est KO !");
            return;
        }
        if (player.getCurrentHealthPoints() < player.getHealthPoints() / 2) {
            AvatarClass avatarClass = player.getAvatarClass();
            avatarClass.healIfBelowHalf(player);
        }
        if (player.getCurrentHealthPoints() >= player.getHealthPoints()) {
            player.setCurrentHealthPoints(player.getHealthPoints());
        }
    }
}