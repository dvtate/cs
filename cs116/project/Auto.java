


public class Auto {
    private static int uid = 1; // if auto has id 0 then it is invalid
    protected int id;
    protected Direction orientation; // is this needed?
    protected int blockId; // is it on a block?


    public Auto(int blockId) {
        this.blockId = blockId;
        this.id = uid++;
    }
    public Auto(){ this(0); }


    public int getId() { return this.id; }
    public int getBlockId() { return this.blockId; }
    public Direction getDirection() { return this.orientation; }
    public Direction getOrientation() { return this.getDirection(); }

    public void setDirection(Direction direction) { this.orientation = direction; }
    public void setOrientation(Direction direction) { this.setDirection(direction); }
    public void setBlockId(int id) { this.blockId = id; }


    // public boolean canMoveForward();
    // public void tick();
}
