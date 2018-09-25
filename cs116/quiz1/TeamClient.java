package MainClass;

import java.util.*;
import java.io.*;

import MainClass.OtherClasses.Conference;
import MainClass.OtherClasses.Team;
public class TeamClient {

    private static boolean contains(Team[] teams, Team team) {
        for (Team t : teams)
            if (t != null && t.equals(team))
                return true;
        return false;
    }

    public static void main(String[] args) {

        String[] lines;
        try {
            Scanner fin = new Scanner(new File("data.txt")).useDelimiter("\\z");
            lines = fin.next().split("\n");
        } catch (FileNotFoundException e) {
            System.out.println("Error: 'data.txt' not found");
            return;
        }


        Team[] teams = new Team[lines.length];

        // for each line make a team object
        int i = 0;
        for (String line : lines) {
            // team-name:conference:wins:ties:losses
            String[] attrs = line.split(":");
            Team team = new Team(attrs[0], Conference.valueOf(attrs[1]),
                Integer.parseInt(attrs[2]), Integer.parseInt(attrs[3]),
                Integer.parseInt(attrs[4]));

            if (!contains(teams, team))
                teams[i++] = team;
        }

        // print team objects
        for (Team t : teams)
            if (t != null)
                System.out.println(t);

    }
}
