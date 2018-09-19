import java.util.*;
import java.io.*;


class twoDimPrac{
	int grade;

	public twoDimPrac(int i, int j)//notice the constructor takes in the row and column index values
	{
		this.grade = i * j;
	}
	public String toString(){
		return "" + this.grade;
	}
	public static void main(String [] args){
		//use a random number generator to generate the number of rows and columns
		//the random number generator will generate values between 1 and 20

		int rows = (int)(Math.random() * 20 + 1);
		int cols = (int)(Math.random() * 20 + 1);

		//declare the 2 dim array
		twoDimPrac[][] myArray = new twoDimPrac[rows][cols];
		//Task: intantiate the 2 dimensional array

		// fucking java is the shittiest language oml lol xd
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				myArray[i][j] = new twoDimPrac(i, j);

		for (Auto[] arr : myArray) {
			for (Auto e : arr)
				System.out.print(e + "\t");
			System.out.println();
		}
		//Task: using a nested for-loop print out all the objects in the array

	}
}
