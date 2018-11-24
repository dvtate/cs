
import java.util.*;
import java.io.*;


public class Simulator {

    public static void main(String[] args) {

        // handles log file writing
        Log log = new Log("out.txt");

        Scanner cin = new Scanner(System.in).useLocale(Locale.ENGLISH);


        SimulatorConstraints constraints = new SimulatorConstraints();


        System.out.printf("Enter the entry rate for incoming vehicles [0-1]: ");
        constraints.entryRate = cin.nextDouble();

        System.out.printf("Enter the turn rate (probability of turning left)[0-1]: ");
        constraints.turnRate = cin.nextDouble();

        System.out.printf("Enter the number of ticks for each green light [integer]: ");
        constraints.greenTicks = cin.nextInt();

        System.out.printf("Enter the number of ticks for each orange light [integer]: ");
        constraints.orangeTicks = cin.nextInt();

        System.out.printf("Enter the total number of ticks for the simulation[integer]: ");
        constraints.simulationTicks = cin.nextInt();

        System.out.printf("Enter the span of the roads leading up to the intersection [integer]: ");
        constraints.span = cin.nextInt();


        System.out.printf("\nGenerating road network... ");
        RoadNetwork rn = new RoadNetwork(constraints);
        rn.populate();
        System.out.println("done.");

        System.out.printf("Running simulation... ");
        for (int i = 0; i < constraints.simulationTicks; i++)
            rn.tick(log);
        System.out.println("done.");

        System.out.println("block32 nabos: ");
        final int[] n = rn.getBlockById(32).getNeighbors();
        for (int i = 0; i < 4; i++) {
            System.out.println(rn.getBlockById(n[i]));
        }

        System.out.println("Avg wait time: " + rn.averageWaitTime());
        double[] er = rn.exitRates();
        System.out.println("Exit Rates:"
            + "\n  - N: " + er[0] + " / " + constraints.simulationTicks
            + "\n  - E: " + er[1] + " / " + constraints.simulationTicks
            + "\n  - S: " + er[2] + " / " + constraints.simulationTicks
            + "\n  - W: " + er[3] + " / " + constraints.simulationTicks
        );

        log.write();

    }
};
