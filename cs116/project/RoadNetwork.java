
import java.util.*;


public class RoadNetwork {

    protected List<Auto> autos;
    protected List<Block> blocks;

    // cars spawn at entry blocks to road
    protected int[] spawnPoints;


    public RoadNetwork () {

    }

    // span is span n,s,e,w from intersection
    private void populate(int[] span) {

        /*
        * = NormalBlock
        # = IntersectionBlock
        s = TrafficBlock

                 **
             ***s##s*****
             ***s##s*****
                 **

        span is number of NormalBLocks in given direction ahead of intersection
        */
    }

    private void spawnCar(int entry) {


    }
    public void tick(Log log) {
        for (TrafficLight tl : this.signals)
            tl.tick();
        log.put("tick")
    }
};
