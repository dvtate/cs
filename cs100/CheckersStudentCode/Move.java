public class Move {
	private int fx, fy;	// FROM row, FROM column
	private int tx, ty;	// TO row, TO column
	private boolean jump, madeKing;
	private char jumpedPiece;
	private Move next;

	public Move(int newFromX, int newFromY, int newToX, int newToY, boolean newJump, boolean newMadeKing, char newJumpedPiece, Move newNextMove) {
		setFromTo(newFromX, newFromY, newToX, newToY);
		jump=newJump;
		madeKing=newMadeKing;
		setJumpedPiece(newJumpedPiece);
		setNext(newNextMove);
	}
	
	public Move(Move newMove) {		// copy constructor
		setFromTo(newMove.fx, newMove.fy, newMove.tx, newMove.ty);
		jump=newMove.jump;
		madeKing=newMove.madeKing;
		setJumpedPiece(newMove.jumpedPiece);
		setNext(newMove.next);
	}	

	public void setFromTo(int newFromX, int newFromY, int newToX, int newToY) {
		if (newFromX>=1 && newFromX<=8 && newToX>=1 && newToY<=8) {
			fx=newFromX;
			fy=newFromY;
			tx=newToX;
			ty=newToY;
		}
		else
			System.err.println("Invalid Move ("+newFromX+","+newFromY+") to("+newToX+","+newToY+")");
	}

	public void setJumpedPiece(char newJumpedPiece) {
		if (newJumpedPiece==CheckersConstants.BCHEC || newJumpedPiece==CheckersConstants.BKING || newJumpedPiece==CheckersConstants.WCHEC || newJumpedPiece==CheckersConstants.WKING || newJumpedPiece==CheckersConstants.AVAIL) 
			jumpedPiece=newJumpedPiece;
		else
			System.err.println("Invalid Jumped Piece "+newJumpedPiece);
	}
	
	public void setNext(Move newNext) {
		if (newNext==null)
			next=newNext;
		else if (this.tx==newNext.fx && this.ty==newNext.fy)
			next=newNext;
		else
			System.err.println("Invalid Next Move "+ newNext);
	}
	
	public int getFromX() { return fx; }
	public int getFromY() { return fy; }
	public int getToX() { return tx; }
	public int getToY() { return ty; }
	public boolean getJump() { return jump; }
	public boolean getMadeKing() { return madeKing; }	
	public char getJumpedPiece() { return jumpedPiece; }	
	public Move getNextMove() { return next; }

	public String toString(){
		String temp="fx="+fx+" fy="+fy+" tx="+tx+" ty="+ty+" jump="+jump+" newKing="+madeKing+" jumped="+jumpedPiece;
		if (next!=null)
			temp=temp+" next=["+next.toString()+"]";
		return temp;
	}
}