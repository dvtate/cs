


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


    // there's definitely a more efficient way to do this...
    public Direction getNextDir(boolean turnLeft, Direction cur) {
        final int ord = cur.ordinal(),
            leftOrd   = (cur.ordinal() - 1) % 4,
            rightOrd  = (cur.ordinal() + 1) % 4;

        final Direction[] flow = this.flow();
        final int
            left  = flow[0].ordinal() == leftOrd ? 0 : flow[1].ordinal() == leftOrd ? 1 : -1,
            right = flow[0].ordinal() == rightOrd ? 0 : flow[1].ordinal() == rightOrd ? 1 : -1,
            keep  = flow[0].ordinal() == ord ? 0 : flow[1].ordinal() == ord ? 1 : -1;

        if (left >= 0)
            System.out.println("" + flow[left] + " is left of " + cur);
        if (right >= 0)
            System.out.println("" + flow[right] + " is right of " + cur);


        if (turnLeft) {
            if (left >= 0)
                return flow[left];
            else
                return flow[keep];
        } else
            if (right >= 0)
                return flow[right];
            else
                return flow[keep];

    }

    public void tick(Log log, RoadNetwork roads) {
        if (super.vacant())
            return;

        Auto auto = roads.getAutoById(super.getAuto());
        Direction turnDir = this.getNextDir(
            Math.random() > roads.getConstraints().turnRate, auto.getDirection());

        Block nextBlock = roads.getBlockById(super.getNeighbor(turnDir));

        if (nextBlock != null && nextBlock.vacant() && auto.moveable(roads.getTicks())) {
            log.put("[" + roads.getTicks() + "]: Auto#" + super.getAuto() + " moved from " + super.getType() + " Block#" + super.getId() + " to " + nextBlock.getType() + " Block#" + nextBlock.getId());
            nextBlock.setAuto(super.getAuto());
            roads.getAutoById(super.getAuto()).setDirection(turnDir); // point car in right direction
            super.setAuto(0);
            auto.setMoved(roads.getTicks());
        } else if (nextBlock == null) {
            System.out.println("wtf null neighbor intersection");
        }

    }
}
