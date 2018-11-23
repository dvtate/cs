
abstract class Block {
	public static enum Type { NORMAL, TRAFFIC, INTERSECT };

	private int uid = 1;
	protected int id; // unique id
	protected int autoId; // unique id of the auto occupying this block
	protected int[] neighbors; // id's of neighboring blocks (n,e,s,w)
	protected Block.Type type;


	Block() {
		this.id = this.uid++;
		this.neighbors = new int[]{ 0, 0, 0, 0 };
	}

	// where should traffic go?
	// array bc intersection has options
	public abstract Direction[] flow();



	public Block.Type getType() { return this.type; }
	public void setType(Block.Type type) { this.type = type; }

	public int[] getNeighbors() { return this.neighbors; }
	public void setNeighbors(int[] neighborsIds) { this.neighbors = neighborsIds; }
	public void setNeighbors(int northId, int eastId, int southId, int westId) {
		this.neighbors = new int[]{ northId, eastId, southId, westId };
	}
	public int getNeighbor(Direction direction)
		{ return direction == null ? null : this.getNeighbors()[ direction.ordinal() ]; }
	public void setNeighbor(int ind, int id) { this.neighbors[ind] = val; }

	public int getId() { return this.id; }

	public int getAuto() { return this.autoId; }
	public void setAuto(int autoId) { this.autoId = autoId; }





	/*
		- try to move auto along
		normal:
			-
			-


	*/
	public void tick(Log log, RoadNetwork roads);


	// if there isn't a valid auto then it's vacant
	public boolean vacant() { return this.autoId <= 0; }

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
