import java.io.*; 
import java.util.Scanner; 
public class Hiring {
	public static void main(String[] args) throws IOException {
		int [][] exampleList = new int [5000][10];
		File inputFile = new File("input.txt");
		Scanner in = new Scanner(inputFile);
		int count=0;
		while (in.hasNextLine()){
			String token [] =in.nextLine().split(",");
			for (int i=0; i<10; i++)
				exampleList[count][i]=Integer.parseInt(token[i]);
			count++;
		}
		
		int [][] finalists = getBestApplicants(exampleList);
		
		System.out.println("The finalists are...");
		for (int i=0; i<finalists.length; i++){
			for (int j=0; j<finalists[0].length; j++){
				System.out.print(finalists[i][j]+" ");
			}
			System.out.println(" ");
		}
	}
	
	public static int [][] getBestApplicants(int [][] appList){
		int [][] finalists = new int[appList.length][appList[0].length];
		for (int i=0; i<appList.length; i++)
			for (int j=0; j<appList[0].length; j++)
				finalists[i][j] = appList[i][j];
		return finalists;
	}
}