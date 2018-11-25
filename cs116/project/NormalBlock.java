

public class NormalBlock extends Block {
    protected Direction forward;

    public NormalBlock(Direction forward) {
        this.forward = forward;
        super.setType(Block.Type.NORMAL);
    }

    @Override
    public Direction[] flow() {
        Direction[] ret = new Direction[1];
        ret[0] = forward;
        return ret;
    }

    public int getNext() { return super.getNeighbor(this.forward); }



    public void tick(Log log, RoadNetwork roads) {
        if (super.vacant())
            return;

        Block nextBlock = roads.getBlockById(this.getNext());
        Auto auto = roads.getAutoById(super.getAuto());
        auto.setDirection(this.forward);
        
        // car can move to next block
        if (nextBlock != null && nextBlock.vacant() && auto.moveable(roads.getTicks())) {
            if (nextBlock.getType() != Block.Type.NORMAL)
                log.put("[" + roads.getTicks() + "]: Auto#" + super.getAuto() + " moved from " + super.getType() + " Block#" + super.getId() + " to " + nextBlock.getType() + " Block#" + nextBlock.getId());
            nextBlock.setAuto(super.getAuto());
            super.setAuto(0);
            auto.setMoved(roads.getTicks());

        // car is leaving simulation; delete it
        } else if (nextBlock == null && auto.moveable(roads.getTicks())) {

            String[] roadNames = {"SN", "WE", "EW", "NS"};
            log.put("[" + roads.getTicks() + "]: Auto#" + super.getAuto() + " left simulation via Block#" + super.getId() + " going " + this.forward);

            roads.deleteAuto(super.getAuto(), super.getId());
            super.setAuto(0);
        }
    }
};
