 public class SoccerGame {
    private String first_team_name;
    private String second_team_name;
    private int ft_goal_score;
    private int st_goal_score;
    private int ft_penalty_score;
    private int st_penalty_score;
    private static int record_score = 10;


    public SoccerGame(){

    }

    public SoccerGame(String first_team_name, String second_team_name, int ft_goal_score, int st_goal_score) {
        this.first_team_name = first_team_name;
        this.second_team_name = second_team_name;
        this.ft_goal_score = ft_goal_score;
        this.st_goal_score = st_goal_score;

        if(ft_goal_score < 0){
            this.ft_goal_score = 0;
        }

        if(st_goal_score < 0){
            this.st_goal_score = 0;
        }

        check_historic_record(ft_goal_score, st_goal_score);

    }

    public SoccerGame(String first_team_name, String second_team_name, int ft_goal_score, int st_goal_score, int ft_penalty_score, int st_penalty_score) {
        this.first_team_name = first_team_name;
        this.second_team_name = second_team_name;
        this.ft_goal_score = ft_goal_score;
        this.st_goal_score = st_goal_score;
        if(ft_goal_score < 0){
            this.ft_goal_score = 0;
        }

        if(st_goal_score < 0){
            this.st_goal_score = 0;
        }

        this.ft_penalty_score = ft_penalty_score;
        this.st_penalty_score = st_penalty_score;

        if(ft_penalty_score < 0){
            this.ft_penalty_score = 0;
        }

        if(st_penalty_score < 0){
            this.st_penalty_score = 0;
        }

        check_historic_record(ft_goal_score, st_goal_score);
    }

    private void check_historic_record(int ft_score, int st_score){
        int hs = ft_score > st_score?ft_score:st_score;
        if(hs > record_score){
            record_score = hs;
        }
    }

    public int getScore1() {
        return ft_goal_score;
    }

    public String getScore2() {
        return second_team_name;
    }

    //first mutator
    public void setGoalScores(int ft_goal_score, int st_goal_score){
        this.ft_goal_score = ft_goal_score;
        this.st_goal_score = st_goal_score;

        if(ft_goal_score != st_goal_score ){
            ft_penalty_score = 0;
            st_penalty_score = 0;
        }
    }

    //second mutator
    public void setAllScores(int ft_goal_score, int st_goal_score, int ft_penalty_score, int st_penalty_score){
        this.ft_goal_score = ft_goal_score;
        this.st_goal_score = st_goal_score;
        this.ft_penalty_score = ft_penalty_score;
        this.st_penalty_score = st_penalty_score;
    }



    public String winner(){
        String winner = "Tie";
        if(ft_goal_score > st_goal_score){
            winner = first_team_name;
        }
        else if(ft_goal_score < st_goal_score){
            winner = second_team_name;
        }
        else{
            winner = ft_penalty_score>st_penalty_score? first_team_name: second_team_name;
        }
        return winner;
    }

    public int getHistoric(){
        return record_score;
    }
}
