package re.forestier.edu.rpg;

public class Money {

    private int amount;

    public Money(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void addMoney(int amount) {
        this.amount += amount;
    }

    public void removeMoney(int value) {
        int result = amount - value;
        if (result < 0) {
            throw new IllegalArgumentException("Player can't have a negative money!");
        }
        this.amount = result;
    }
}