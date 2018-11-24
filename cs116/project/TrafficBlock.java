

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
        Auto auto = roads.getAutoById(super.getAuto());
        // move car if u can
        if (nextBlock != null && nextBlock.vacant() && signal.isGreen() && auto.moveable(roads.getTicks())) {
            log.put("[" + roads.getTicks() + "]: Auto#" + super.getAuto() + " moved from " + super.getType() + " Block#" + super.getId() + " to " + nextBlock.getType() + " Block#" + nextBlock.getId());
            nextBlock.setAuto(super.getAuto());
            super.setAuto(0);
            auto.setMoved(roads.getTicks());
        } else if (nextBlock == null)
            System.out.println("WTF, trafficblock with null dest??");
        else if (!signal.isGreen()) {
            log.put("[" + roads.getTicks() + "]: Auto#" + super.getAuto() + " is waiting at the " + this.signal.getState() + " light.");
        }
    }
};
