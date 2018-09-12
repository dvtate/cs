

public class DailySales {
    public int[] sales;


    public DailySales(int totalDays) {
        this.sales = new int[totalDays];
    }
    public DailySales() { this(10); }


    public boolean addSales(int dayNumber, int sales) {
        try {
            this.sales[dayNumber - 1] += sales;
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    String[] stringifyDays(int[] days) {
        String[] ret = new String[days.length];
        for (int i = 0; i < days.length; i++)
            ret[i] = "Day " + (i + 1) + " : " + sales[i];

        return ret;
    }

    public int maxDay() {
        int max = sales[0];
        int imax = 0;
        for (int i = 1; i < sales.length; i++) {
            if (sales[i] > max) {
                max = sales[i];
                imax = i;
            }
        }
        return imax;
    }
    public int days() {
        return this.sales.length;
    }
    public int[] daysBelowGoal() {
        int numBelow = 0;
        for (int d : sales)
            if (d < 100)
                numBelow++;

        int[] ret = new int[numBelow];
        int n = 0;
        for (int i = 0; i < sales.length; i++)
            if (sales[i] < 100)
                ret[n++] = i;

        return ret;

    }

    public String toString() {
        String ret = "Sales Data:\n";

        for (int i = 0; i < sales.length; i++)
            ret += "Day " + (i + 1) + " : " + sales[i] + "\n";

        return ret;
    }

};
