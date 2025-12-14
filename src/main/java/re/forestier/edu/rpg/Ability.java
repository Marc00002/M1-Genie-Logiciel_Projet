package re.forestier.edu.rpg;

public class Ability {

    public enum AbilityType {
        ATK, ALC, DEF, INT, CHA, VIS
    }

    private final AbilityType type;
    private int value;

    public Ability(AbilityType type, int value){
        this.type = type;
        this.value = value;
    }

    public AbilityType getType() { return type; }
    public int getValue()       { return value; }

    public void update(int delta) {
        this.value += delta;
    }

    @Override
    public String toString() {
        return type + " : " + value;
    }
}
