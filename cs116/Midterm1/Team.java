package Midterm.Service;

public class Team {

    // members
    protected String name;
    protected int wins, losses;


    // constructors
    public Team(String name, int wins, int losses) {
        this.name = name;
        this.wins = wins;
        this.losses = losses;
    }
    public Team() {
        this("no name", 0, 0);
    }


    // access modifiers
    public String getName() { return name; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }

    public void setName(String name) { this.name = name; }
    public void setWins(int wins) { this.wins = wins; }
    public void setLosses(int losses) { this.losses = losses; }

    public double winningPercentage() {
        return (this.wins + this.losses == 0) ?
            0.0
            : (double) this.wins / (this.wins + this.losses);
    }

    public String toString() {
        return "==========================="
            + "\nTeam: " + this.name
            + "\nWins: " + this.wins
            + "\nLosses: " + this.losses
            + "\nWinning Percentage: " + this.winningPercentage()
            + (this.winningPercentage() > .5 ?
                "\nOver 500 Team"
                : (this.winningPercentage() == .5 ?
                    "\n500 Team" : "\nUnder 500 Team"))
            + "\n===========================\n";
    }



};
