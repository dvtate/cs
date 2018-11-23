
import java.util.*;


public class RoadNetwork {

    protected List<Auto> autos;
    protected List<Block> blocks;

    protected List<int>[] waitTimes;

    protected SimulatorConstraints constraints;

    protected int ticks;

    // cars spawn at entry blocks to road (nesw)
    protected int[] spawnPoints;
    protected int[] despawnPoints;
    protected int[] exits;

    public RoadNetwork () {
        this.ticks = 0;

        this.exits = new int[]{ 0, 0, 0, 0 };

        // wait times for cars exiting at each exit point
        this.waitTimes = new ArrayList<int>()[4];
    }

    public Auto getAutoById(int id) {
        for (int i = 0; i < autos.size(); i++)
            if (autos.get(i).getId() == id)
                return autos.get(i);
        return null;
    }

    public Block getBlockById(int id) {
        for (int i = 0; i < blocks.size(); i++)
            if (blocks.get(i).getId() == id)
                return blocks.get(i);
        return null;
    }

    // de-spawn car when it exits simulation
    public void deleteAuto(int id, int blockId) {
        for (int i = 0; i < autos.size(); i++)
            if (autos.get(i).getId() == id) {
                waitTimes.add(autos.get(i).getTicks());
                autos.remove(i);

                // increment corresponding exit counter
                for (int e = 0; e < 4; e++)
                    if (this.despawnPoints[e] == blockId)
                        this.exits[e]++;

                return;
            }
    }


    public List<Auto> getAutos() { return this.autos; }
    public List<Block> getBlocks() { return this.blocks; }
    public SimulatorConstraints getConstraints() { return this.constraints; }

    // span is span n,s,e,w from intersection
    // this function is way longer than it should be.
    // This is so that i dont have to write as much documentation :)
    private void populate() {

        /*
        * = NormalBlock
        # = IntersectionBlock
        s = TrafficBlock

                 **
                 s*
             ****##s****
             ***s##*****
                 *s
                 **

        * constraints.span is number of NormalBLocks in given direction ahead of intersection
        * NOTE: for lanes leading to a traffic block there are constraints.span-1 normal blocks (my understanding)

        */


        // declaring and defining the key intersection components
        TrafficLight    nstl = new TrafficLight(this.constraints.greenTicks, this.constraints.orangeTicks),
                        wetl = new TrafficLight(this.constraints.greenTicks, this.constraints.orangeTicks);
        nstl.setRed();
        wetl.setGreen();

        // unfortunately left+right depends on where you're coming from
        IntersectionBlock   nwib = new IntersectionBlock(Direction.SOUTH, Direction.WEST),
                            neib = new IntersectionBlock(Direction.WEST, Direction.NORTH),
                            swib = new IntersectionBlock(Direction.SOUTH, Direction.EAST),
                            seib = new IntersectionBlock(Direction.NORTH, Direction.EAST);


        TrafficBlock    ntb = new TrafficBlock(Direction.SOUTH, nstl),  // north-facing trafficblock
                        etb = new TrafficBlock(Direction.WEST, wetl),
                        stb = new TrafficBlock(Direction.NORTH, nstl),
                        wtb = new TrafficBlock(Direction.EAST, wetl);


        // declaring the road leading to the intersection
        // span-1 because traffic block
        // start: spawn, end: traffic signal
        NormalBLock[]   ncNorms = new NormalBlock[this.constraints.span - 1],
                        ecNorms = new NormalBlock[this.constraints.span - 1],
                        scNorms = new NormalBlock[this.constraints.span - 1],
                        wcNorms = new NormalBlock[this.constraints.span - 1];
        // start: intersection, end: despawn point
        NormalBLock[]   cnNorms = new NormalBlock[this.constraints.span],
                        ceNorms = new NormalBlock[this.constraints.span],
                        csNorms = new NormalBlock[this.constraints.span],
                        cwNorms = new NormalBlock[this.constraints.span];

        // initialization and linking :/
        for (int i = 0; i < this.constraints.span; i++) {
            // initialization
            cnNorms[i] = new NormalBlock(Direction.NORTH);
            ceNorms[i] = new NormalBlock(Direction.EAST);
            csNorms[i] = new NormalBlock(Direction.SOUTH);
            cwNorms[i] = new NormalBlock(Direction.WEST);

            // linking
            if (i > 0) {
                // link central to north road
                cnNorms[i].setNeighbors(0, 0, cnNorms[i - 1].getId(), 0); // southern neighbor is prev
                cnNorms[i - 1].setNeighbor(0, cnNorms[i].getId()); // prev northern neighbor is current

                ceNorms[i].setNeighbors(0, 0, 0, ceNorms[i - 1].getId());
                ceNorms[i - 1].setNeighbor(1, ceNorms[i].getId());

                csNorms[i].setNeighbors(csNorms[i - 1].getId, 0,0,0);
                csNorms[i - 1].setNeighbor(2, csNorms[i].getId());
            }
        }

        // same concept as previous loop
        for (int i = 0; i < this.constraints.span - 1; i++) {

        }

        // link roads to intersection

        // link intersection

        // add all blocks to this.blocks[]


    }

    private void spawnCar(int entry) {


    }


    public void tick(Log log) {
        log.put("tick#" + (this.ticks++));

        // ajust clock on all cars on the road
        for (Auto a : this.autos)
            a.tick();

        for (Block b : this.blocks)
            b.tick(log, this);



        // spwn car if rng says so
        for (int i = 0; i < 4; i++)
            if (Math.random() < this.constraints.entryRate)
                spawnCar(i);




    }

    public double averageWaitTime() {
        int total = 0;
        for (int time : waitTimes)
            total += time;

        return (double) total / waitTimes.size();
    }

    public double[] exitRates() {

    }
};
