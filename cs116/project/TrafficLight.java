

public class TrafficLight {

    public static enum State { RED, ORANGE, GREEN };

    protected int ticks, greenTicks, orangeTicks;

    public TrafficLight(int greenTicks, int orangeTicks) {
        this.setRed();
        this.greenTicks = greenTicks;
        this.orangeTicks = orangeTicks;
    }

    public int getTicks() { return this.ticks; }
    public void setTicks(int ticks) { this.ticks = ticks; }

    public void setGreen() { this.setTicks(this.greenTicks + this.orangeTicks); }
    public void setRed() { this.setTicks(0); }

    // this is failsafe
    public State getState() {
        final int t = this.ticks % 14;
        if (t < 7)
            return State.RED;
        if (t < 12)
            return State.GREEN;
        //if (t < 14)
        return State.ORANGE;
    }

    public boolean isGreen() { return this.getState() == State.GREEN; }

    // clocklike adder
    public void tick() {
        this.ticks = ++this.ticks % (2 * (this.greenTicks + this.orangeTicks));
    }
};
