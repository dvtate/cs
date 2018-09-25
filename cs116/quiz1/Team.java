package MainClass.OtherClasses;

import MainClass.OtherClasses.Conference;

public class Team {
    private static int uid;
    private int id;
    public String name;
    public int wins, ties, losses, games;
    public Conference conf;

    public Team(String name, Conference conf, int wins, int ties, int losses) {
        this.name = name;
        this.conf = conf;
        this.wins = wins;
        this.ties = ties;
        this.losses = losses;
        this.games = wins + losses + ties;
        this.id = uid++;
    }
    public Team() {
        this("NO_NAME", Conference.UNKNOWN, 0, 0, 0);
    }

    public int getId() { return this.id; }
    public int getWins() { return this.wins; }
    public int getTies() { return this.ties; }
    public int getLosses() { return this.losses; }
    public int getGames() { return this.games; }
    public String getName() { return this.name; }
    public Conference getConference() { return this.conf; }

    public void setWins(int in) { this.wins = in; }
    public void setTies(int in) { this.ties = in; }
    public void setLosses(int in) { this.losses = in; }
    public void setGames(int in) { this.games = in; }
    public void setName(String in) { this.name = in; }
    public void setConference(Conference in) { this.conf = in; }

    public float calculateStanding() {
        return (float)(this.wins * 2 - this.losses) / this.games;
    }

    public String toString() {
        return "Team Name: " + this.name
            + "\nConference: " + this.conf
            + "\nTotal Games Played: " + this.games
            + "\nWins: " + this.wins
            + "\nLosses: " + this.losses
            + "\nTies: " + this.ties
            + "\nStanding" + this.calculateStanding()
            + "\n";
    }

    // only told to check name and conference
    public boolean equals(Team t) {
        return t.name.toLowerCase().equals(this.name.toLowerCase())
            && t.conf.equals(this.conf);
    }
};
