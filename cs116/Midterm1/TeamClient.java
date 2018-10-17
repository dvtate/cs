package Midterm.Client;


import Midterm.Service.Team;
import java.io.*;
import java.util.*;


public class TeamClient {

	// return sorted version of arraylist
	public static ArrayList<Team> sort(ArrayList<Team> teams) {

		for (int i = 0; i < teams.size(); i++) {
			// find next highest elem (local max)
			int mi = i;
			double m = teams.get(mi).winningPercentage();

			for (int j = i; j < teams.size(); j++)
				if (teams.get(j).winningPercentage() > m) {
					mi = j;
					m = teams.get(j).winningPercentage();
				}

			// swap
			Team tmp = teams.get(mi);
			teams.set(mi, teams.get(i));
			teams.set(i, tmp);


		}
		return teams;

	}

	// if in the list already return index, else return -1
	public static int contains(ArrayList<Team> teams, String name) {
		for (int i = 0; i < teams.size(); i++)
			if (teams.get(i).getName() == name)
				return i;
		return -1;
	}

	public static void main(String args[]) {

		//read the file and create the objects
		String line = new String();
		ArrayList<Team> teams = new ArrayList<Team>();

		try {
			File textFile = new File("TeamTxt.txt");
			Scanner scan = new Scanner(textFile);
			while (scan.hasNextLine()) {

				line = scan.nextLine();
				String []tok = line.split(":");
				String name = tok[0];
				int wins = Integer.parseInt(tok[1]);
				int losses = Integer.parseInt(tok[2]);

				int ind = contains(teams, name);
				if (ind == -1)
					teams.add(new Team(name, wins, losses));
				else { // combine duplicate stats
					Team t = teams.get(ind);
					t.setWins(t.getWins() + wins);
					t.setLosses(t.getLosses() + losses);
					teams.set(ind, t);
				}
			}

			teams = sort(teams);
			for (Team t : teams)
				System.out.println(t);

		} catch(IOException e){
			System.out.println("Issuse with reading the file. Aborting");
			System.out.println(e.getMessage());
		}
	}
};
