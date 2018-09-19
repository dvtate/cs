

public class SudokuApp {

    public static void main(String[] args) {
        byte[][] testGrid = new byte[9][9];

        /* correctly solved sudoku
        testGrid =
            {
               { 7,4,2,5,8,9,6,1,3 },
               { 9,8,6,3,2,1,7,4,5 },
               { 1,5,3,6,4,7,8,9,2 },
               { 8,9,1,2,3,4,5,6,7 },
               { 4,6,7,1,5,8,2,3,9 },
               { 2,3,5,9,7,6,4,8,1 },
               { 3,7,8,4,1,5,9,2,6 },
               { 5,2,9,8,6,3,1,7,4 },
               { 6,1,4,7,9,2,3,5,8 }
           }
        */

        for (byte i = 0; i < 9; i++)
            for (byte j = 0; j < 9; j++)
                testGrid[i][j] = (byte)(Math.random() * 9 + 1);

        Sudoku test = new Sudoku(testGrid);
        System.out.println(test + "\nValidity: " + test.isSolved());

    }


};
