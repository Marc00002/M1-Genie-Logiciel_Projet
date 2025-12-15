package re.forestier.edu.rpg;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Item {
    String name;
    String description;
    Integer weight;
    Integer value;

    private static final List<Item> itemList = new ArrayList<>();


    static {
        itemList.add(new Item("Lookout Ring", "Prevents surprise attacks", 1, 100));
        itemList.add(new Item("Scroll of Stupidity", "INT-2 when applied to an enemy", 2, 50));
        itemList.add(new Item("Draupnir", "Increases XP gained by 100%", 5, 300));
        itemList.add(new Item("Magic Charm", "Magic +10 for 5 rounds", 3, 150));
        itemList.add(new Item("Rune Staff of Curse", "May burn your enemies... Or yourself. Who knows?", 7, 200));
        itemList.add(new Item("Combat Edge", "Well, that's an edge", 4, 80));
        itemList.add(new Item("Holy Elixir", "Recover your HP", 2, 120));
        itemList.add(new Item("Magic Bow", "Recover a part of your HP", 40, 1550));
    }

    public Item(String name, String description, int weight, int value){
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.value = value;
    }

    public String getName()          { return this.name; }
    public String getDescription()   { return description; }
    public Integer getWeight()       { return weight; }
    public Integer getValue()        { return value; }


    public static Item randomItem() {
        Random random = new Random();
        return itemList.get(random.nextInt(itemList.size()));
    }

    public static Item getItem(String name) {
        for (Item o : itemList) {
            if (o.getName().equals(name)) {
                return o;
            }
        }
        return null;
    }

}

