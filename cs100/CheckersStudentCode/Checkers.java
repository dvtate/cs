import java.util.*;
public class Checkers {
	public static void main(String[] args) {
		int depth=7, totalMoves=150;
		boolean display=true;
		Scanner in = new Scanner(System.in);
		System.out.println("Black Moves First");
		System.out.print("Who goes first, Human or Computer (H or C)? ");
		Board b = new Board();
		Game g = new Game(b, depth, depth, display);
		int counter=0;		
		if (in.next().equals("C")) {  // Computer first (black)
			System.out.println("Computer is Black");
			while(counter<totalMoves) {
				if (b.end_game(CheckersConstants.BLACK)) {
					System.out.println("Human Win");	
					break;
				}
				g.comp_move(CheckersConstants.BLACK);
				counter++;
				System.out.println(b);	
		
				if (b.end_game(CheckersConstants.WHITE)) {
					System.out.println("Computer Win");	
					break;
				}
				// Find and display available human moves 
				ArrayList<Move> possible = b.find_moves(CheckersConstants.WHITE);
				System.out.println("White move");
				
				int i=0;
				for (Move m : possible) {
					System.out.println(i+": "+ m);
					i++;
				}
				// prompt human to choose one
				boolean moveChosen=false;
				int n=0;
				do {
					System.out.print("Which move? ");
					n = in.nextInt();
					if (n>=0 && n<possible.size()) moveChosen=true;
				} while (!moveChosen);
				b.make_move(possible.get(n));
				counter++;
				System.out.println(b);		
			}
			if (counter==totalMoves) 
				System.out.print("TieComputer("+b.checkerCount(CheckersConstants.BLACK)+")Human("+b.checkerCount(CheckersConstants.WHITE)+")  "); 
		}
		else {  // Human first (black)
			System.out.println("Computer is White");
			while(counter<totalMoves) {
				if (b.end_game(CheckersConstants.BLACK)) {
					System.out.println("Human Win");	
					break;
				}
				// Find and display available human moves 
				ArrayList<Move> possible = b.find_moves(CheckersConstants.BLACK);
				System.out.println("Black move");
				
				int i=0;
				for (Move m : possible) {
					System.out.println(i+": "+ m);
					i++;
				}
				// prompt human to choose one
				boolean moveChosen=false;
				int n=0;
				do {
					System.out.print("Which move? ");
					n = in.nextInt();
					if (n>=0 && n<possible.size()) moveChosen=true;
				} while (!moveChosen);
				b.make_move(possible.get(n));
				counter++;
				System.out.println(b);	
				
				if (b.end_game(CheckersConstants.WHITE)) {
					System.out.println("Computer Win");	
					break;
				}
				g.comp_move(CheckersConstants.WHITE);
				counter++;
				System.out.println(b);	
			}
			if (counter==totalMoves) 
				System.out.print("TieHuman("+b.checkerCount(CheckersConstants.BLACK)+")Computer("+b.checkerCount(CheckersConstants.WHITE)+")  "); 
		}
	}
}