



public class Card {
    public static enum Suite {
        DIAMONDS(0), CLUBS(1), HEARTS(2), SPADES(3);
        public int val;
        Suite(int v) { this.val = v; }

        public boolean isRed() { return this.val % 2 == 0; }
        public boolean isBlack() { return this.val % 2 == 1; }
        public static Suite at(int v) { return values()[v]; }
    }


    public static enum Value {
        ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8),
        NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13);

        public int val;
        Value(int v) { this.val = v; }
        public boolean isFaceCard() { return this.val > 10; }
        public static Value at(int v) { return values()[v - 1]; }
    }

    private Suite suite;
    private Value value;

    public Card (Suite suite, Value value) {
        this.suite = suite;
        this.value = value;
    }

    public Card (int suite, int value) {
        this(Suite.at(suite), Value.at(value));
    }

    public Suite getSuite() { return this.suite; }
    public Value getValue() { return this.value; }
    public void setSuite(Suite s) { this.suite = s; }
    public void setValue(Value v) { this.value = v; }


    public String toString() {
        return this.value.toString() + " of " + this.suite.toString();
    }



}
