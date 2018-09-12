
import java.util.Scanner;

public class DailySalesClient {
    public static void main(String[] args) {

        Scanner cin = new Scanner(System.in);

        System.out.print("Enter number of days (defualt: 10): ");

        DailySales ds = new DailySales(Integer.parseInt(cin.nextLine().trim()));

        System.out.println("Now entering sales data...");


        while (true) {
            // get data from user
            System.out.print("Enter the day number (-1 to quit): ");
            int day = Integer.parseInt(cin.nextLine().trim());

            if (day < 0) { break; }
            System.out.print("Enter sales for day " + day + ": ");
            int sales = Integer.parseInt(cin.nextLine().trim());


            // add data
            boolean succ = ds.addSales(day, sales);
            if (!succ) {
                System.out.println("Invalid day number");
                continue;
            }

            System.out.println(ds);


        }

        System.out.println("Finished entering data\nAnalysis:");
        System.out.println(ds);

        System.out.println("Max sales occured on day " + ds.maxDay());
        System.out.println("Days Not meeting sales goal:");
        for (String s : ds.stringifyDays(ds.daysBelowGoal()))
            System.out.println(s);




    }
};
