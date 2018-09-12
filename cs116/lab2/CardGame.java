package library.client.classes;
import java.util.*;
import java.io.*;
import carddeck.service.classes.*;
/*Task 2: Import necessary user defined classes */


class CardGame{
	Card [] userHand;
	Card [] dealerHand;



	// populate hands
	private void generateDealerHand(){

		/*Task 5: Implement the class method generateDealerHand() in CardGame class. This method will use a random generator to generate signs, and values of the cards for the dealer.*/
		dealerHand = new Card[5];
		for (int i = 0; i < 5; i++)
			dealerHand[i] = new Card(
				CardSign.values()[(int) (Math.random() * 4)],
				CardValue.values()[(int) (Math.random() * 14)]);

	}


	private void generateUserHand(String path){
		userHand = new Card[5];
		File f;Scanner scan = null;
		try {
			f = new File(path);
			scan = new Scanner(f);
		} catch (FileNotFoundException ff) {

		}
		int i = 0;
		while (scan.hasNext()) {

				String[] card = scan.nextLine().split(":");
				Card c = new Card(CardSign.valueOf(card[0]),CardValue.valueOf(card[1]));
				userHand[i] = c;
				i++;

		}
	}

	private int getScore(){

		/*Task 3: Implement the class method getScore() in CardGame class.
		*	This method will compare the cards of the dealer and player and provide a score.
		*	If the score is zero, it means the game is a tie.
		*	If the score is positive, then the player has won the game.
		*	You should implement other methods as needed.
		*/

		int dealerScore = 0;
		for (Card c : dealerHand)
			dealerScore += c.getValue().ordinal();


		int userScore = 0;
		for (Card c : userHand)
			userScore += c.getValue().ordinal();

		if (isFlush(dealerHand))
			dealerScore += 70;
		if (isFlush(userHand))
			userScore += 70;


		if (isRoyalFlush(dealerHand))
			dealerScore = 500;

		if (isRoyalFlush(userHand))
			userScore = 500;


		//System.out.println("dealer: " + dealerScore);
		//System.out.println("user: " + userScore);

		return userScore - dealerScore;

	}

	private static void printHand(Card[] hand) {
		for (Card c: hand)
			System.out.println('\t' + c.toString());

	}
	private void printHands() {
		System.out.println("user hand:");
		CardGame.printHand(userHand);
		System.out.println("dealer hand:");
		CardGame.printHand(dealerHand);
	}

	private static boolean isFlush(Card[] hand) {
		CardSign suit = hand[0].getSign();
		for (Card c: hand)
			if (c.getSign() != suit)
				return false;
		return true;
	}
	private static boolean hasCard(Card[] hand, Card c) {
		for (Card cc : hand)
			if (cc.equals(c))
				return true;
		return false;
	}

	private static boolean isRoyalFlush(Card[] hand) {
		if (!isFlush(hand))
			return false;
		if (CardGame.hasCard(hand, new Card(hand[0].getSign(), CardValue.ACE))
			&& CardGame.hasCard(hand, new Card(hand[0].getSign(), CardValue.KING))
			&& CardGame.hasCard(hand, new Card(hand[0].getSign(), CardValue.JACK))
			&& CardGame.hasCard(hand, new Card(hand[0].getSign(), CardValue.QUEEN))
			&& CardGame.hasCard(hand, new Card(hand[0].getSign(), CardValue.TEN)))
			return true;
		return false;

	}


	public static void main(String []args){

	//arg[0]: file containing user hand

		CardGame game=new CardGame();
		//read the the files from text files
		int counter=0;
		Card aCard;
		Scanner scan;
		String str;
		try {
			game.generateUserHand(args[0]);

			//lets play iPoker!!
			//User interactive part
			String option1;
			scan = new Scanner(System.in);
			int score;
			while(true){
				System.out.println("Select an option:");
				System.out.println("Type \"P\" to play a round of iPoker");
				System.out.println("Type \"Q\" to Quit");
				option1=scan.nextLine();

				switch (option1) {
					case "P":   game.generateDealerHand();
								score=game.getScore();
								game.printHands();///First print out the hands
								System.out.println("\n\nCompare the two hands:");
								if(score < 0)
									System.out.println("Dealer Wins :-(");
								else if (score == 0)
									System.out.println("Its a draw");
								else if (score > 0)
									System.out.println("Congrats You win !!");
								else
									System.out.println("Somethings wrong!");
								break;

					case "Q":   System.out.println("Quitting program");
								System.exit(0);

					default:	System.out.println("Wrong option! Try again");
								break;

				}
			}

		//}catch(IOException ioe){
		//	System.out.println("The file can not be read");
		}catch(IllegalArgumentException ia){
            System.out.println(ia.getMessage());
		} catch(NullPointerException np){
			System.out.println(np.getMessage());
		}
	}

}
