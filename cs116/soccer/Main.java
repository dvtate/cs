import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        SoccerGame game1 = new SoccerGame("Holland", "Brazil",1,2);
        SoccerGame game2 = new SoccerGame("Ukraine", "Sweden",1,1,3,4);

        System.out.println("Game 1 winner is "+game1.winner()+"\nGame 2 winner is "+game2.winner());

        System.out.println("Historic High Score is "+game1.getHistoric());

        SoccerGame game3 = new SoccerGame(game1.winner(), game2.winner(),-1,-1);
        try
        {

            BufferedReader br  = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter goal score of Team 1 for game 3: ");
            int ft_goal_score = Integer.parseInt(br.readLine());
            System.out.println("Please enter goal score of Team 2 for game 3: ");
            int st_goal_score = Integer.parseInt(br.readLine());

            if(ft_goal_score != st_goal_score){
                game3.setGoalScores(ft_goal_score,st_goal_score);
            }
            else{
                System.out.println("Please enter penalty score of Team 1 for game 3: ");
                int ft_p_score = Integer.parseInt(br.readLine());
                System.out.println("Please enter penalty score of Team 2 for game 3: ");
                int st_p_score = Integer.parseInt(br.readLine());
                game3.setAllScores(ft_goal_score,st_goal_score,ft_p_score,st_p_score);

            }

            System.out.println("Game 3 winner is "+game3.winner());

        }
        catch (Exception e){
            System.out.println("You entered invalid input.");
            e.printStackTrace();
        }


    }
}
