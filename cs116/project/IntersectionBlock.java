


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
        final Direction[] flow = this.flow();
        final int ord = cur.ordinal(),
            leftOrd   = (4 + cur.ordinal() - 1) % 4,
            rightOrd  = (4 + cur.ordinal() + 1) % 4,
            left  = flow[0].ordinal() == leftOrd ? 0 : flow[1].ordinal() == leftOrd ? 1 : -1,
            right = flow[0].ordinal() == rightOrd ? 0 : flow[1].ordinal() == rightOrd ? 1 : -1,
            keep  = flow[0].ordinal() == ord ? 0 : flow[1].ordinal() == ord ? 1 : -1;

        /* test cases
        if (left >= 0)
            System.out.println("" + flow[left] + " is left of " + cur);
        if (right >= 0)
            System.out.println("" + flow[right] + " is right of " + cur);
        */

        return turnLeft ?
            ( left >= 0 ? flow[left] : flow[keep] )
            : ( right >= 0 ? flow[right] : flow[keep] );

    }

    public void tick(Log log, RoadNetwork roads) {
        if (super.vacant())
            return;

        Auto auto = roads.getAutoById(super.getAuto());

        final boolean turnLeft = Math.random() <= roads.getConstraints().turnRate;
        final Direction turnDir = this.getNextDir(turnLeft, auto.getDirection());

        Block nextBlock = roads.getBlockById(super.getNeighbor(turnDir));

        if (nextBlock != null && nextBlock.vacant() && auto.moveable(roads.getTicks())) {
            log.put("[" + roads.getTicks() + "]: Auto#" + super.getAuto() + " turned " + (turnLeft ? "left" : "right") + " to go " + turnDir);
            log.put("[" + roads.getTicks() + "]: Auto#" + super.getAuto() + " moved from " + super.getType() + " Block#" + super.getId() + " to " + nextBlock.getType() + " Block#" + nextBlock.getId());
            auto.setDirection(turnDir); // point car in right direction
            auto.setMoved(roads.getTicks());
            nextBlock.setAuto(super.getAuto());
            super.setAuto(0);
        } else if (nextBlock == null) {
            System.out.println("wtf null neighbor intersection");
        }

    }
}
