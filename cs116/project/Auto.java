


public class Auto {
    private static int uid = 1; // if auto has id 0 then it is invalid
    protected int id;
    protected Direction orientation; // is this needed?
    protected int ticks;


    public Auto() {
        this.id = uid++;
        this.ticks = 0;
    }
    public Auto(){ this(0); }


    public int getId() { return this.id; }

    public Direction getDirection() { return this.orientation; }
    public void setDirection(Direction direction) { this.orientation = direction; }

    // NOTE: movement of cars handled by block object tick methods
    public void tick() { this.ticks++; }
    public void getTicks() { return this.ticks; }

}
