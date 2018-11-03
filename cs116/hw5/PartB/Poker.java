
import java.util.*;

public class Poker extends CardGame {

    public Poker () {
        super();
        super.cardsPerPlayer = 5;
    }

    public List<Card> deal() { return super.deck.subList(0, super.cardsPerPlayer); }
    public void displayDiscription() {
        System.out.println("Poker is a family of card games which are the kings of gambling");
    }

};
