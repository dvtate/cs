

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
        roads.getAutoById(super.getAuto()).setDirection(this.forward);
        // car can move to next block
        if (nextBlock != null && nextBlock.vacant()) {
            nextBlock.setAuto(super.getAuto());
            super.setAuto(0);

        // car is leaving simulation; delete it
        } else if (nextBlock == null) {
            roads.deleteAuto(super.getAuto());
            super.setAuto(0);
        }
    }
};
