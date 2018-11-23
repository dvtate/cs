


public class IntersectionBlock extends Block {

    protected Direction[] forward;

    public IntersectionBlock(Direction left, Direction right) {
        this.forward = new Direction[2];
        this.forward[0] = left;
        this.forward[1] = right;
        super.setType(Block.Type.INTERSECT);
    }

    @Override
    public Direction[] flow() { return this.forward; }


    public Direction getNextDir(boolean turnLeft) {

    }

    public void tick(Log log, RoadNetwork roads) {
        if (super.vacant())
            return;

        Direction turnDir = this.getNextDir(
            Math.random() > roads.getConstraints().turnRate);

        Block nextBlock = roads.getBlockById(super.getNeighbor(turn));

        if (nextBlock != null && nextBlock.vacant()) {
            nextBlock.setAuto(super.getAuto());
            roads.getAutoById(super.getAuto()).setDirection(turnDir); // point car in right direction
            super.setAuto(0);
        } else if(nextBlock == null) {
            System.out.println("wtf null neighbor intersection");
        }

    }
}
