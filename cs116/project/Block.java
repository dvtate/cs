
abstract class Block {
	public static enum Type { NORMAL, TRAFFIC, INTERSECT };

	private int uid = 1;
	protected int id; // unique id
	protected int autoId; // unique id of the auto occupying this block
	protected int[] neighbors; // id's of neighboring blocks (n,s,e,w)
	protected Block.Type type;

	// where should traffic go?
	// array bc intersection has options
	public abstract Direction[] flow();



	public Block.Type getType() { return this.type; }
	public void setType(Block.Type type) { this.type = type; }

	public int[] getNeighbors() { return this.neighbors; }
	public void setNeighbhors(int[] neighborsIds) { this.neighbors = neighborsIds; }

	public int getId() { return this.id; }

	public int getAuto() { return this.autoId; }
	public void setAuto(int autoId) { this.autoId = autoId; }





	/*
		- try to move auto along
		normal:
			-
			-


	*/
	public abstract void tick(Log log);


	// if there isn't a valid auto then it's vacant
	public boolean vacant() { return this.autoId > 0; }

	/* would get hairy for intersections
	public int getNextId(flow()) {

	}
	public int getPrevId() {

	}
	public void setPrev(int id) {

	}
	public void setNext(int id) {

	}
	*/


}
