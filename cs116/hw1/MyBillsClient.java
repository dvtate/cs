package Client;


import java.util.*;
import java.io.*;

// service classes
import Client.Services.Classes.MyBills;
import Client.Services.BillsType;



public class MyBillsClient {

    public String[] totalExpensesPerMonth(MyBills[] exps) {
        // 13 month slots in case default constructor is called somewhere
        String[] months = new String[13];
        double[] totals = new double[13];
        int len = 0;

        for (MyBills e : exps) {
            int index = arrayFind(months, e.getMonth());
            if (index != -1) {
                for (double v : e.getExpenses())
                    totals[index] += v;
            } else {
                len++;
                months[len - 1] = e.getMonth();
                totals[len - 1] = 0;
                for (double v : e.getExpenses())
                    totals[len - 1] += v;
            }
        }


        String[] strs = new String[exps.length];
        int i = 0;
        for (; i < months.length; i++)
            if (months[i] == null) {
                break;
            } else {
                strs[i] = "The total expenses for the month of "
                    + months[i] + " is: ";

                double net = 0;
                for (double e : exps[i].getExpenses())
                    net += e;

                strs[i] += '$' + String.format("%.2f", net);
            }

        String[] ret = new String[i--];
        for (; i >= 0; i--)
            ret[i] = strs[i];

        return ret;

    }

    public static void main(String[] args) {

        // read file
        Scanner dataFile;
        try {
            // set the delimiter to EOF so that we can read the entire file at once
            dataFile = new Scanner(new File("myexpenses.txt")).useDelimiter("\\Z");
        } catch (FileNotFoundException e) {
    		System.out.println("error: myexpenses.txt not found");
    		return;
    	}

        // why would they do this?
        if (!dataFile.hasNext()) {
            System.out.println("error: empty file");
            return;
        }

        // read entire file and split it into individual lines
        String[] data = dataFile.next().trim().split("\n");

        // there must be an even number of lines otherwise there's a problem...
        if (data.length % 2 != 0) {
            System.out.println("error: malformated data");
            return;
        }

        MyBills[] expenses = new MyBills[data.length / 2];

        // populate array
        for (int i = 0; i < data.length; i += 2) {
            // month and type are on first line
            String month = data[i].split(":")[1].trim();
            BillsType type = BillsType.valueOf(data[i].split(":")[3].trim());

            // expenses list on next line
            String[] elist = data[i + 1].split(",");
            double[] edata = new double[elist.length];
            for (int e = 0; e < elist.length; e++)
                edata[e] = Double.parseDouble(elist[e]);

            expenses[i / 2] = new MyBills(month, type, edata);
        }

        // output # 1
        System.out.println("\tOUTPUT # 1");
        for (MyBills b : expenses)
            System.out.println(b);

        // "Method totalExpensesPerMonth _is_not_static_"
        String[] expenseReport = (new MyBillsClient()).totalExpensesPerMonth(expenses);

        System.out.println("\tOUTPUT # 2");
        for (String r : expenseReport)
            System.out.println(r);

    }

    private static int arrayFind(String[] arr, String v) {
        for (int i = 0; i < arr.length; i++)
            if (arr[i] != null && arr[i].equals(v))
                return i;
        return -1;
    }

};
