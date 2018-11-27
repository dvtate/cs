


public class Auto {
    private static int uid = 1; // if auto has id 0 then it is invalid
    protected int id;
    protected Direction orientation; // is this needed?
    protected int ticks;

    // to prevent car from moving multiple blocks in one tick
    protected int moveable;

    public Auto() {
        this.id = uid++;
        this.ticks = 0;
    }
    
    public int getId() { return this.id; }

    public Direction getDirection() { return this.orientation; }
    public void setDirection(Direction direction) { this.orientation = direction; }

    // to prevent car from moving multiple blocks per tick
    public boolean moveable(int time) { return this.moveable != time; }
    public void setMoved(int time) { this.moveable = time; }

    // NOTE: movement of cars handled by block object tick methods
    public void tick() { this.ticks++; }
    public int getTicks() { return this.ticks; }

    public String toString() { return "Auto#" + this.getId(); }
}
