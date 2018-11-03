

import java.util.*;

public abstract class CardGame {

    
    protected List<Card> deck = new ArrayList<Card>();



    public void populateDeck() {
        for (int s = 0; s < 4; s++)
            for (int v = 1; v < 14; v++)
                deck.add(new Card(s, v));
    }


    public CardGame() {
        this.populateDeck();
    }

    //
    public void shuffle() {
        Collections.shuffle(deck); // me lazy
    }

    protected int cardsPerPlayer;
    abstract void displayDiscription();
    abstract List<Card> deal();

    public List<Card> getDeck() { return this.deck; }

};
