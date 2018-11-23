

// in C++/JavaScript this would be a struct/raw JSON
// therefore everything should be public because im not a masochist

public class SimulatorConstraints {
    public double entryRate, turnRate;
    public int greenTicks, orangeTicks, simulationTicks, span;

    public SimulatorConstraints(
        double entryRate, double turnRate, int greenTicks, int orangeTicks,
        int simulationTicks, int span
    ) {
        this.entryRate = entryRate; this.turnRate = turnRate; this.greenTicks = greenTicks;
        this.orangeTicks = orangeTicks; this.simulationTicks = simulationTicks;
        this.span = span;
    }
}
