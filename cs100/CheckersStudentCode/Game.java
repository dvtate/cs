import java.util.*;
public class Game {
	private Board b;
	private int BLACKdepth, WHITEdepth;
	private boolean display=false;
	
	public Game(Board newBoard, int newBLACKdepth, int newWHITEdepth, boolean newDisplay) {
		b=newBoard;
		display=newDisplay;
		if (newBLACKdepth>0) BLACKdepth=newBLACKdepth;
		else BLACKdepth=6;
		if (newWHITEdepth>0) WHITEdepth=newWHITEdepth;
		else WHITEdepth=6;
	}

	public void	comp_move(int turn) {
		Move m;
		if (turn == CheckersConstants.BLACK) {
			if (display) {
				System.out.println("Black move - thinking");
			}
			m = minmax(turn, BLACKdepth, turn);
		}
		else {
			if (display) {
				System.out.println("White move - thinking");
			}
			m = minmax(turn, WHITEdepth, turn);
		}
		if (display) System.out.println(m);
        b.make_move(m);		// make move
	}

	public Move minmax(int whoseMove, int level, int turn) {
		ArrayList<Move> possible_moves;
		ArrayList<Integer> scores = new ArrayList<Integer>();
		Move chosenMove;
		int best=Integer.MIN_VALUE, current=Integer.MIN_VALUE, worst=Integer.MAX_VALUE;

		// get moves for all checkers of player turn
		possible_moves=b.find_moves(turn);
		chosenMove=possible_moves.get(0);
		for (Move m : possible_moves) {	
			b.make_move(m);
			// recurse
			current = minmaxR(whoseMove, level-1, -1*turn);
			scores.add(current);			
			if (whoseMove==turn) {	// maximize level
				if (current>best) {
					best=current;
					chosenMove=m;
				}
				else if (current==best && Math.random()>.5) {
					chosenMove=m;
				}					
			}
			else {		// minimize level
				if (current<worst) {
					worst=current;
					chosenMove=m;
				}
				else if (current==worst && Math.random()>.5) {
					chosenMove=m;
				}				
			}
			b.unmake_move(m);
		}
		return chosenMove;
	}

	public int minmaxR(int whoseMove, int level, int turn) {
		ArrayList<Move> possible_moves;
		ArrayList<Integer> scores = new ArrayList<Integer>();
		int best=Integer.MIN_VALUE, current=Integer.MIN_VALUE, worst=Integer.MAX_VALUE;

		if (b.end_game(turn)) {	// turn player lost
			if (turn==whoseMove) return Integer.MIN_VALUE;
			else return Integer.MAX_VALUE;
		}

		if (level == 0) return b.evaluate(whoseMove);
		else {
			// get moves for all checkers of player turn
			possible_moves=b.find_moves(turn);
			for (Move m : possible_moves) {	
				b.make_move(m);
				current = minmaxR(whoseMove, level-1, -1*turn);
				scores.add(current);
				if (whoseMove==turn) {	// maximize level
					if (current>best || (current==best && Math.random()>.5))
						best=current;
				}
				else {		// minimize level
					if (current<worst || (current==worst && Math.random()>.5)) 
						worst=current;
				}
				b.unmake_move(m);
			}
		}
		
		if (whoseMove==turn) return best;
		else return worst;
	}
}