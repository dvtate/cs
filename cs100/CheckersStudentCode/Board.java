import java.util.*;
public class Board {
	private char [][] position;

	public Board( ) {
		position = new char[10][10];
		// illegal positions outside edges
		for (int i=0;i<=9;i++) {
			position[0][i] = CheckersConstants.OUT;
			position[9][i] = CheckersConstants.OUT;
			position[i][0] = CheckersConstants.OUT;
			position[i][9] = CheckersConstants.OUT;
		}

		// illegal positions within board
		for (int i=1;i<=8;i+=2)
			for (int j=2;j<=8;j+=2) {
				position[i][j-1] = CheckersConstants.OUT;
				position[i+1][j] = CheckersConstants.OUT;
			}

		// initial checker positions
		for (int j=2;j<=8;j+=2) {
			position[1][j]   = CheckersConstants.WCHEC;	// white
			position[2][j-1] = CheckersConstants.WCHEC;	// white
			position[3][j]   = CheckersConstants.WCHEC;	// white
			position[4][j-1] = CheckersConstants.AVAIL;	// legal and empty positons
			position[5][j]   = CheckersConstants.AVAIL;	// legal and empty postions
			position[6][j-1] = CheckersConstants.BCHEC;	// black
			position[7][j]   = CheckersConstants.BCHEC;	// black
			position[8][j-1] = CheckersConstants.BCHEC;	// black
		}
	}

	public ArrayList<Move> find_moves(int turn) {
		ArrayList<Move> allMoves = new ArrayList<Move>();
		boolean jumpExists=false;
		if (turn==CheckersConstants.BLACK) {
			for (int i=1;i<=8;i++){
				for (int j=1;j<=8;j++){
					if (position[i][j] == CheckersConstants.BCHEC || position[i][j] == CheckersConstants.BKING) {
						ArrayList<Move> oneCheckerMoves = find_moves(i,j);
						for (Move item: oneCheckerMoves) {
							if (item.getJump()) jumpExists=true;
							allMoves.add(item);
						}
					}
				}
			}
		}
		else {
			for (int i=1;i<=8;i++){
				for (int j=1;j<=8;j++){
					if (position[i][j] == CheckersConstants.WCHEC || position[i][j] == CheckersConstants.WKING) {
						ArrayList<Move> oneCheckerMoves = find_moves(i,j);
						for (Move item: oneCheckerMoves){
							if (item.getJump()) jumpExists=true;
							allMoves.add(item);
						}
					}
				}
			}
		}
		// FORCED JUMP LOGIC, IF AT LEAST ONE JUMP AVAILABLE ONLY RETURN JUMP MOVES
		if (jumpExists) {
			Iterator itr = allMoves.iterator();
			while (itr.hasNext()) {
				Move m = (Move)itr.next();
				if (m.getJump()==false) itr.remove();
			}
        }
		return allMoves;
	}

	public ArrayList<Move> find_moves(int row, int col) {
		ArrayList<Move> oneCheckerMoves = new ArrayList<Move>();
		Move newMove;
		char piece=position[row][col];
		// for single checker moves
		if (piece == CheckersConstants.WCHEC || piece == CheckersConstants.BCHEC) {
			int k=0;
			// white up, black down
			if (piece == CheckersConstants.WCHEC) k = 1;
			else if (piece == CheckersConstants.BCHEC) k = -1;

			for (int i=0;i<2;i++) { // always 2 possible moves for each checker
				int j;
				if (i == 0) j = 1;
				else j = -1;

				// get the position moving to
				char check = position[row+k][col+j];

				// save non-jump move if space available
				if (check == CheckersConstants.AVAIL) {
					boolean newMoveKing=false;
					if (row+k==1 || row+k==8) newMoveKing=true;
					newMove=new Move(row, col, row+k, col+j, false, newMoveKing, CheckersConstants.AVAIL, null);
					oneCheckerMoves.add(newMove);
				}
				// if jump then save the jump
				else if ( (piece == CheckersConstants.WCHEC && (check == CheckersConstants.BCHEC || check == CheckersConstants.BKING)) ||
						(piece == CheckersConstants.BCHEC && (check == CheckersConstants.WCHEC || check == CheckersConstants.WKING)) ) {
					if (row+2*k>=1 && row+2*k<=8 && col+2*j>=1 && col+2*j<=8 ) {
						if (position[row+2*k][col+2*j] == CheckersConstants.AVAIL){
							boolean newMoveKing=false;
							if (row+2*k==1 || row+2*k==8) {
								newMoveKing=true;
							}
							newMove=new Move(row, col, row+2*k, col+2*j, true, newMoveKing, check, null);
							make_move(newMove);  // should make king if applicable
							ArrayList<Move> jumpMoves = find_moves(row+2*k, col+2*j);
							boolean continuedJump=false;
							if (!jumpMoves.isEmpty()) {
								Move moveToExtend = new Move(newMove);
								for (Move continuedMove : jumpMoves){
									// only extend jumps
									if (continuedMove.getJump()) {
										moveToExtend.setNext(continuedMove);
										oneCheckerMoves.add(moveToExtend);
										continuedJump=true;
										moveToExtend = new Move(newMove);
									}
								}
							}
							if (!continuedJump) {	// no continued jumps
								oneCheckerMoves.add(newMove);
							}
							unmake_move(newMove);	// should unmake king if applicable
						}
					}
				} // end of jump
			}	// end of two possible moves for each non-king checker
		}  // end of non-king checker moves

		// for single king checker moves
		if (piece == CheckersConstants.WKING || piece == CheckersConstants.BKING) {
			for (int i=0;i<4;i++) {
				// try all directions
				int j=0, k=0;
				switch(i) {
					case 0: j=1; k=1; break;
					case 1: j=1; k=-1; break;
					case 2: j=-1; k=1; break;
					case 3: j=-1; k=-1; break;
				}

				// get the position moving to
				char check = position[row+k][col+j];

				// save non-jump move if space available
				if (check == CheckersConstants.AVAIL) {
					newMove=new Move(row, col, row+k, col+j, false, false, CheckersConstants.AVAIL, null);
					oneCheckerMoves.add(newMove);
				}
				// if jump then save the jump
				else if ( (piece == CheckersConstants.WKING && (check == CheckersConstants.BCHEC || check == CheckersConstants.BKING)) ||
						(piece == CheckersConstants.BKING && (check == CheckersConstants.WCHEC || check == CheckersConstants.WKING)) ) {
					if ((row+2*k)>=1 && (row+2*k)<=8 && (col+2*j)>=1 && (col+2*j)<=8 ) {
						if (position[(row+2*k)][(col+2*j)] == CheckersConstants.AVAIL){
							newMove=new Move(row, col, row+2*k, col+2*j, true, false, check, null);
							make_move(newMove);
							ArrayList<Move> jumpMoves = find_moves(row+2*k, col+2*j);
							boolean continuedJump=false;
							if (!jumpMoves.isEmpty()) {
								Move moveToExtend = new Move(newMove);
								for (Move continuedMove : jumpMoves){
									// only extend jumps
									if (continuedMove.getJump()) {
										moveToExtend.setNext(continuedMove);
										oneCheckerMoves.add(moveToExtend);
										moveToExtend = new Move(newMove);
										continuedJump=true;
									}
								}
							}
							if (!continuedJump) {	// no continued jumps
								oneCheckerMoves.add(newMove);
							}
							unmake_move(newMove);
						}
					}
				} // end of jump
			}	// end of four possible moves for each king checker
		}  // end of king checker moves
		return oneCheckerMoves;
	}

	public void make_move(Move m) {
		do {
			int fx=m.getFromX();
			int fy=m.getFromY();
			char piece = position[fx][fy];
			position[fx][fy] = CheckersConstants.AVAIL;

			int tx=m.getToX();
			int ty=m.getToY();
			int k=0, l=0;
			// left, right, down or up
			if (tx > fx) k = 1; else k = -1;
			if (ty > fy) l = 1; else l = -1;

			// clear the space if it was a jump
			if (m.getJump()) position[fx+k][fy+l] = CheckersConstants.AVAIL;

			// change to king
			if (piece == CheckersConstants.WCHEC && m.getMadeKing()) piece = CheckersConstants.WKING;
			if (piece == CheckersConstants.BCHEC && m.getMadeKing()) piece = CheckersConstants.BKING;

			// update the board with moved piece
			position[tx][ty] = piece;
			m=m.getNextMove();

		} while (m != null);
	}

	public void unmake_move(Move m) {
		ArrayList<Move> moveList = new ArrayList<Move>();
		while (m != null) {
			moveList.add(m);
			m=m.getNextMove();
		}
		Collections.reverse(moveList);
		for (Move mm : moveList) {
			int fx=mm.getToX();
			int fy=mm.getToY();
			char piece = position[fx][fy];
			position[fx][fy] = CheckersConstants.AVAIL;

			int tx=mm.getFromX();
			int ty=mm.getFromY();
			int k=0, l=0;
			// left, right, down or up
			if (tx > fx) k = 1; else k = -1;
			if (ty > fy) l = 1; else l = -1;

			// if it was a jump put back the jumped piece
			if (mm.getJump()) position[fx+k][fy+l] = mm.getJumpedPiece();

			// change back to normal piece if it was a king
			if (piece == CheckersConstants.WKING && mm.getMadeKing()) piece = CheckersConstants.WCHEC;
			if (piece == CheckersConstants.BKING && mm.getMadeKing()) piece = CheckersConstants.BCHEC;

			// update the board with moved piece
			position[tx][ty] = piece;
		}
	}

	public int checkerCount(int who) { // WHITE=-1, CheckersConstants.BLACK=1
		int count=0;
		for (int i=1;i<=8;i++){
			for (int j=1;j<=8;j++){
				if (who==CheckersConstants.WHITE && (position[i][j]==CheckersConstants.WCHEC || position[i][j]==CheckersConstants.WKING))
						count++;
				if (who==CheckersConstants.BLACK && (position[i][j]==CheckersConstants.BCHEC || position[i][j]==CheckersConstants.BKING))
						count++;
			}
		}
		return count;
	}

	public String toString( ) {
		String temp = "\t1\t2\t3\t4\t5\t6\t7\t8\n";
		for (int i=1;i<=8;i++){
			temp=temp+i+"\t";
			for (int j=1;j<=8;j++){
			// display coresponding figures
				switch (position[i][j]){
					case CheckersConstants.WCHEC: temp=temp+"W\t"; break;
					case CheckersConstants.WKING: temp=temp+"WK\t"; break;
					case CheckersConstants.BCHEC: temp=temp+"B\t"; break;
					case CheckersConstants.BKING: temp=temp+"BK\t"; break;
					case CheckersConstants.AVAIL: temp=temp+".\t"; break;
					case CheckersConstants.OUT:   temp=temp+"\t"; break;
				}
			}
			temp=temp+"\n";
		}
		return temp;
	}


	public boolean end_game(int player) {
		ArrayList<Move> data=find_moves(player);
		if (data.isEmpty()) return true;
		else return false;
	}

	public int weightedCheckerCount(int who) {
		int ret = 0;
		for (char[] row : position)
			for (char piece : row)
				if (who == CheckersConstants.WHITE)
					if (piece == CheckersConstants.WCHEC)
						ret++;
					else if (piece == CheckersConstants.WKING)
						ret += 2;

				else if (who == CheckersConstants.BLACK)
					if (piece == CheckersConstants.BCHEC)
						ret++;
					else if (piece == CheckersConstants.BKING)
						ret += 2;
		return ret;
	}

	public int edgePieces(int who) {
		int ret = 0;
		for (int i = 1; i < 9; i++) {
			if (who == CheckersConstants.WHITE) {
				if (position[1][i] == CheckersConstants.WCHEC || position[1][i] == CheckersConstants.WKING)
					ret++;
				if (position[8][i] == CheckersConstants.WCHEC || position[8][i] == CheckersConstants.WKING)
					ret++;
				if (position[i][8] == CheckersConstants.WCHEC || position[i][8] == CheckersConstants.WKING)
					ret++;
				if (position[i][1] == CheckersConstants.WCHEC || position[i][1] == CheckersConstants.WKING)
					ret++;
			} else if (who == CheckersConstants.BLACK) {
				if (position[1][i] == CheckersConstants.BCHEC || position[1][i] == CheckersConstants.BKING)
					ret++;
				if (position[8][i] == CheckersConstants.BCHEC || position[8][i] == CheckersConstants.BKING)
					ret++;
				if (position[i][8] == CheckersConstants.BCHEC || position[i][8] == CheckersConstants.BKING)
					ret++;
				if (position[i][1] == CheckersConstants.BCHEC || position[i][1] == CheckersConstants.BKING)
					ret++;
			}
		}
		return ret;
	}
	private int evaluateWhite() {
		// pieces on board
	 	int selfTotal = this.weightedCheckerCount(CheckersConstants.WHITE),
			enemyTotal = this.weightedCheckerCount(CheckersConstants.BLACK),
			netTotal = selfTotal - enemyTotal,
			edgePieces = this.edgePieces(CheckersConstants.WHITE);

			return netTotal + edgePieces;
	}
	private int evaluateBlack() {
		// pieces on board
		int selfTotal = this.weightedCheckerCount(CheckersConstants.BLACK),
			enemyTotal = this.weightedCheckerCount(CheckersConstants.WHITE),
			netTotal = selfTotal - enemyTotal,
			edgePieces = this.edgePieces(CheckersConstants.BLACK);

			return netTotal + edgePieces;
	}

	public int evaluate(int player) {
		// player is either CheckersConstants.BLACK or CheckersConstants.WHITE
		if (player == CheckersConstants.WHITE)
			return this.evaluateWhite();
		else if (player == CheckersConstants.BLACK)
			return this.evaluateBlack();
		return 1;
	}
}
