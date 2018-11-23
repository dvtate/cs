

public class TrafficBlock extends Block {
    protected Direction forward;
    protected TrafficLight signal;


    public TrafficBlock(Direction forward, TrafficLight signal) {
        this.forward = forward;
        // prolly dont need to copy but dont want double ticking
        this.signal = new TrafficLight(signal);
        super.setType(Block.Type.TRAFFIC);
    }


    public TrafficLight getSignal() { return this.signal; }

    @Override
    public Direction[] flow() {
        Direction[] ret = new Direction[1];
        ret[0] = forward;
        return ret;
    }

    public void setFlow(Direction forward) { this.forward = forward; }



    public int getNext() { return super.getNeighbor(this.forward); }

    public void tick(Log log, RoadNetwork roads) {
        signal.tick();
        if (super.vacant())
            return;

        Block nextBlock = roads.getBlockById(this.getNext());
        // move car if u can
        if (nextBlock != null && nextBlock.vacant() && signal.isGreen()) {
            nextBlock.setAuto(super.getAuto());
            super.setAuto(0);
        } else if (nextBlock == null)
            System.out.println("WTF, trafficblock with null dest??");
    }
};
