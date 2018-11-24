
import java.util.*;


public class RoadNetwork {

    protected List<Auto> autos;
    protected List<Block> blocks;

    protected List<List<Integer>> waitTimes;

    protected SimulatorConstraints constraints;

    protected int ticks;

    // cars spawn at entry blocks to road (nesw)
    protected int[] spawnPoints;
    protected int[] despawnPoints;

    public RoadNetwork (SimulatorConstraints constraints) {
        this.constraints = constraints;
        this.ticks = 0;

        // wait times for cars exiting at each exit point
        this.autos = new ArrayList<Auto>();
        this.blocks = new ArrayList<Block>();
        this.waitTimes = new ArrayList<List<Integer>>(4);
        for (int i = 0; i < 4; i++)
            this.waitTimes.add(new ArrayList<Integer>());
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

    private void spawnAuto(Log log, int entry) {
        Block b = this.getBlockById(this.spawnPoints[entry]);
        if (!b.vacant()) {
            System.out.println("Spawn block not vacant... too much traffic?");
            return;
        }
        Auto car = new Auto();
        b.setAuto(car.getId());
        car.setDirection(b.flow()[0]);

        this.autos.add(car);
        //System.out.println("this.autos: " + this.autos.toString());
        String[] roadNames = {"NS", "EW", "WE", "SN"};
        log.put("[" + this.getTicks() + "]: Auto#" + car.getId() + " entered simulation via " + roadNames[entry]);
    }

    // de-spawn car when it exits simulation
    public void deleteAuto(int id, int blockId) {
        System.out.printf("Despawn Points(" + blockId + "): ");
        for (int pt : this.despawnPoints)
            System.out.printf(pt + ", ");
        //System.out.println("Looking for Auto#" + id);
        //System.out.println("this.autos: " + autos.toString());
        for (int i = 0; i < this.autos.size(); i++)
            if (this.autos.get(i).getId() == id) {

                // increment corresponding exit counter
                for (int e = 0; e < 4; e++)
                    if (this.despawnPoints[e] == blockId)
                        waitTimes.get(e).add(this.autos.get(i).getTicks());

                this.autos.remove(i);

                return;
            }
    }


    public List<Auto> getAutos() { return this.autos; }
    public List<Block> getBlocks() { return this.blocks; }
    public SimulatorConstraints getConstraints() { return this.constraints; }
    public int getTicks() { return this.ticks; }

    // span is span n,s,e,w from intersection
    // this function is way longer than it should be.
    // This is so that i dont have to write as much documentation :)
    public void populate() {

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
        IntersectionBlock   nwib = new IntersectionBlock(Direction.WEST, Direction.SOUTH),
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
        NormalBlock[]   ncNorms = new NormalBlock[this.constraints.span - 1],
                        ecNorms = new NormalBlock[this.constraints.span - 1],
                        scNorms = new NormalBlock[this.constraints.span - 1],
                        wcNorms = new NormalBlock[this.constraints.span - 1];
        // start: intersection, end: despawn point
        NormalBlock[]   cnNorms = new NormalBlock[this.constraints.span],
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

                csNorms[i].setNeighbors(csNorms[i - 1].getId(), 0, 0, 0);
                csNorms[i - 1].setNeighbor(2, csNorms[i].getId());

                cwNorms[i].setNeighbors(0, cwNorms[i - 1].getId(), 0, 0);
                cwNorms[i - 1].setNeighbor(3, cwNorms[i].getId());
            }
        }

        // same concept as previous loop
        for (int i = 0; i < this.constraints.span - 1; i++) {
            ncNorms[i] = new NormalBlock(Direction.SOUTH);
            ecNorms[i] = new NormalBlock(Direction.WEST);
            scNorms[i] = new NormalBlock(Direction.NORTH);
            wcNorms[i] = new NormalBlock(Direction.EAST);

            if (i > 0) {
                ncNorms[i].setNeighbors(ncNorms[i - 1].getId(), 0, 0, 0); // northern neighbor is prev
                ncNorms[i - 1].setNeighbor(2, ncNorms[i].getId());

                ecNorms[i].setNeighbors(0, ecNorms[i - 1].getId(), 0, 0);
                ecNorms[i - 1].setNeighbor(3, ecNorms[i].getId());

                scNorms[i].setNeighbors(0, 0, scNorms[i - 1].getId(), 0);
                scNorms[i - 1].setNeighbor(0, scNorms[i].getId());

                wcNorms[i].setNeighbors(0, 0, 0, wcNorms[i - 1].getId());
                wcNorms[i - 1].setNeighbor(1, wcNorms[i].getId());
            }

        }


        System.out.println("nc ids: [" + ncNorms[0].getId() + ", " + ncNorms[this.constraints.span - 2].getId() + "]");
        System.out.println("ec ids: [" + ecNorms[0].getId() + ", " + ecNorms[this.constraints.span - 2].getId() + "]");
        System.out.println("sc ids: [" + scNorms[0].getId() + ", " + scNorms[this.constraints.span - 2].getId() + "]");
        System.out.println("wc ids: [" + wcNorms[0].getId() + ", " + wcNorms[this.constraints.span - 2].getId() + "]");
        System.out.println("cn ids: [" + cnNorms[0].getId() + ", " + cnNorms[this.constraints.span - 1].getId() + "]");
        System.out.println("ce ids: [" + ceNorms[0].getId() + ", " + ceNorms[this.constraints.span - 1].getId() + "]");
        System.out.println("cs ids: [" + csNorms[0].getId() + ", " + csNorms[this.constraints.span - 1].getId() + "]");
        System.out.println("cw ids: [" + cwNorms[0].getId() + ", " + cwNorms[this.constraints.span - 1].getId() + "]");


        // we now have 8 linked roads, 4 traffic blocks and 4 intersection blocks
        // time to link them together *correctly*

        // glhf
        ncNorms[this.constraints.span - 2].setNeighbor(2, ntb.getId());
        ecNorms[this.constraints.span - 2].setNeighbor(3, etb.getId());
        scNorms[this.constraints.span - 2].setNeighbor(0, stb.getId());
        wcNorms[this.constraints.span - 2].setNeighbor(1, wtb.getId());
        ntb.setNeighbors(ncNorms[this.constraints.span - 2].getId(), 0, nwib.getId(), 0);
        etb.setNeighbors(0, ecNorms[this.constraints.span - 2].getId(), 0, neib.getId());
        stb.setNeighbors(seib.getId(), 0, scNorms[this.constraints.span - 2].getId(), 0);
        wtb.setNeighbors(0, swib.getId(), 0, wcNorms[this.constraints.span - 2].getId());
        cnNorms[0].setNeighbor(3, neib.getId());
        ceNorms[0].setNeighbor(2, seib.getId());
        csNorms[0].setNeighbor(0, swib.getId());
        cwNorms[0].setNeighbor(1, nwib.getId());
        nwib.setNeighbors(ntb.getId(), neib.getId(), swib.getId(), csNorms[0].getId());
        neib.setNeighbors(cnNorms[0].getId(), etb.getId(), seib.getId(), nwib.getId());
        seib.setNeighbors(neib.getId(), ceNorms[0].getId(), stb.getId(), swib.getId());
        swib.setNeighbors(nwib.getId(), seib.getId(), csNorms[0].getId(), wtb.getId());

        // add all blocks to this.blocks[]
        this.blocks.addAll(Arrays.asList(ncNorms));
        this.blocks.addAll(Arrays.asList(cnNorms));
        this.blocks.addAll(Arrays.asList(ecNorms));
        this.blocks.addAll(Arrays.asList(ceNorms));
        this.blocks.addAll(Arrays.asList(scNorms));
        this.blocks.addAll(Arrays.asList(csNorms));
        this.blocks.addAll(Arrays.asList(wcNorms));
        this.blocks.addAll(Arrays.asList(cwNorms));
        this.blocks.add(nwib); this.blocks.add(neib); this.blocks.add(ntb);
        this.blocks.add(seib); this.blocks.add(swib); this.blocks.add(stb);
        this.blocks.add(etb);  this.blocks.add(wtb);

        this.spawnPoints = new int[]{
            ncNorms[0].getId(), ecNorms[0].getId(), scNorms[0].getId(), wcNorms[0].getId()
        };

        this.despawnPoints = new int[]{
            cnNorms[this.constraints.span - 1].getId(),
            ceNorms[this.constraints.span - 1].getId(),
            csNorms[this.constraints.span - 1].getId(),
            cwNorms[this.constraints.span - 1].getId(),
        };
        // populate spawn and despawn point arrays

    }



    public void tick(Log log) {
        log.put("tick#" + (this.ticks++));

        // ajust clock of all cars on the road
        for (Auto a : this.autos)
            a.tick();

        for (Block b : this.blocks)
            b.tick(log, this);



        // spwn car if rng says so
        for (int i = 0; i < 4; i++) {
            double rng = Math.random();
            //System.out.println("RNG:" + rng);
            if (rng < this.constraints.entryRate)
                this.spawnAuto(log, i);
        }



    }

    public double averageWaitTime() {
        int total = 0, exits = 0;
        for (int i = 0; i < 4; i++)
            for (int time : this.waitTimes.get(i)) {
                total += time;
                exits++;
            }
        return (double) total / exits;
    }

    public double[] exitRates() {
        return new double[] {
            this.waitTimes.get(0).size(),
            this.waitTimes.get(1).size(),
            this.waitTimes.get(2).size(),
            this.waitTimes.get(3).size(),
        };
    }
};
