
import java.util.*;

public class Bridge extends CardGame {


    public Bridge () {
        super();
        super.cardsPerPlayer = 13;
    }

    public List<Card> deal() { return super.deck.subList(0, super.cardsPerPlayer); }
    public void displayDiscription() {
        System.out.println("Bridge is a game of partnerships where each player gets 13 cards");
    }

};
