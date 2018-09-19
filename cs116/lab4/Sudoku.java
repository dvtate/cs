

public class Sudoku {
    public byte grid[][];


    public Sudoku(byte[][] in) {
        this.grid = in;
    }
    public Sudoku() {
        this.grid = new byte[9][9];
    }

    public byte get(byte i, byte j) {
        return this.grid[i][j];
    }
    public void set(byte i, byte j, byte v) {
        this.grid[i][j] = v;
    }

    // util - returns true iff elem is in arr
    private static boolean findElem(byte[] arr, byte elem) {
        for (byte b : arr)
            if (b == elem)
                return true;
        return false;
    }

    public boolean checkRow(byte row) {
        byte[] r = this.grid[row];

        byte i = 0;
        byte [] found = new byte[9];
        for (byte e : r) {
            if (findElem(found, e))
                return false;
            found[i++] = e;
        }
        return true;
    }

    public boolean checkCol(byte col) {
        byte i = 0;
        byte [] found = new byte[9];
        for (byte[] r : this.grid) {
            if (findElem(found, r[col]))
                return false;
            found[i++] = r[col];
        }
        return true;
    }


    private byte[][] getBox(byte i, byte j) {
        byte[][] ret = {
            { grid[i * 3][j * 3],      this.grid[i * 3][j * 3 + 1],     this.grid[i * 3][j * 3 + 2] },
            { grid[i * 3 + 1][j * 3],  this.grid[i * 3 + 1][j * 3 + 1], this.grid[i * 3 + 1][j * 3 + 2] },
            { grid[i * 3 + 2][j * 3],  this.grid[i * 3 + 2][j * 3 + 1], this.grid[i * 3 + 2][j * 3 + 2] },
        };
        return ret;
    }

    public static boolean checkBox(byte[][] box) {
        byte ret = 0;
        byte i = 0;
        byte[] found = new byte[9];

        for (byte[] r : box)
            for (byte v : r) {
                if (findElem(found, v))
                    return false;
                found[i++] = v;
            }

        return true;
    }

    public boolean isSolved() {

        // check if rows and cols have sum of 9
        for (byte i = 0; i < 9; i++)
            if (!checkCol(i) || !checkRow(i))
                return false;

        // check if boxes have sum of 9
        for (byte i = 0; i < 3; i++)
            for (byte j = 0; j < 3; j++)
                if (!checkBox(this.getBox(i, j)))
                    return false;

        return true;
    }

    public String toString() {
        String ret = "";
        for (byte[] r : grid) {
            for (byte e : r)
                ret += e + "  ";
            ret += "\n";
        }
        return ret;
    }
}
