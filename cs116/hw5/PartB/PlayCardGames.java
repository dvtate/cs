
import java.util.*;

public class PlayCardGames {
    public static void main(String[] args) {

        // separated scopes so i can reuse variable names

        {
            Poker poker = new Poker();

            System.out.println("Description: ");
            poker.displayDiscription();

            System.out.println("\nfirst 3 cards (unshuffled):");
            for (Card c: poker.getDeck().subList(0, 3))
                System.out.println(c.toString());

            System.out.printf("\nshuffling... ");
            poker.shuffle();
            System.out.println("done");

            System.out.println("\nfirst 3 cards (shuffled):");
            for (Card c: poker.getDeck().subList(0, 3))
                System.out.println(c.toString());

            List<Card> hand = poker.deal();
            System.out.println("\nDealt " + hand.size() + " cards:");
            for (Card c : hand)
                System.out.println(c.toString());

        }

        {

            Bridge bridge = new Bridge();

            System.out.println("Description: ");
            bridge.displayDiscription();

            System.out.println("first 3 cards (unshuffled):");
            for (Card c: bridge.getDeck().subList(0, 3))
                System.out.println(c.toString());

            System.out.printf("shuffling... ");
            bridge.shuffle();
            System.out.println("done");

            System.out.println("first 3 cards (shuffled):");
            for (Card c: bridge.getDeck().subList(0, 3))
                System.out.println(c.toString());

            List<Card> hand = bridge.deal();
            System.out.println("Dealt " + hand.size() + " cards");
            for (Card c : hand)
                System.out.println(c.toString());

        }

    }
}
