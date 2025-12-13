package re.forestier.edu.rpg;

import re.forestier.edu.rpg.classes.AvatarClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UpdatePlayer {

    private final static String[] objectList = {"Lookout Ring : Prevents surprise attacks","Scroll of Stupidity : INT-2 when applied to an enemy", "Draupnir : Increases XP gained by 100%", "Magic Charm : Magic +10 for 5 rounds", "Rune Staff of Curse : May burn your ennemies... Or yourself. Who knows?", "Combat Edge : Well, that's an edge", "Holy Elixir : Recover your HP"
    };

    public static boolean addXp(Player player, int xp) {
        int currentLevel = player.retrieveLevel();
        player.xp += xp;
        int newLevel = player.retrieveLevel();

        if (newLevel != currentLevel) {
            giveRandomItem(player);
            applyLevelAbilities(player, newLevel);
            return true;
        }
        return false;
    }

    private static void giveRandomItem(Player player) {
        Random random = new Random();
        int index = random.nextInt(objectList.length);
        player.getInventory().add(objectList[index]);
    }

    private static void applyLevelAbilities(Player player, int newLevel) {
        AvatarClass avatarClass = player.getAvatarClass();
        Map<String, Integer> abilitiesForLevel = avatarClass.getAbilitiesForLevel(newLevel);

        abilitiesForLevel.forEach((ability, level) -> {
            player.getAbilities().put(ability, level);
        });
    }

    public static void majFinDeTour(Player player) {
        if (player.getCurrentHealthPoints() == 0) {
            System.out.println("Le joueur est KO !");
            return;
        }
        if (player.getCurrentHealthPoints() < player.getHealthPoints() / 2) {
            AvatarClass avatarClass = player.getAvatarClass();
            avatarClass.healBelowHalf(player);
        }
        if (player.getCurrentHealthPoints() >= player.getHealthPoints()) {

            player.setCurrentHealthPoints(player.getHealthPoints());
        }
    }
}