package Client.Services.Classes;

import java.util.*;

import Client.Services.BillsType;


public class MyBills {

    protected String month;
    protected BillsType type;
    protected double[] expenses;
    protected int days;

    protected int id;               // expense id
    private static int uid = 0;     // static id



    public MyBills(String eMonth, BillsType eType, double[] eList, int eListSize) {
        this.month = eMonth;
        this.type = eType;
        this.expenses = eList;
        this.days = eListSize;
        this.id = uid++;
    }

    // by default assume the number of days is equal to the number provided by the expenses array
    public MyBills(String eMonth, BillsType eType, double[] eList)
        { this(eMonth, eType, eList, eList.length); }

    // default constructor as specified
    public MyBills()
        { this("any month", null, null, 0); }

    // accessor and mitilator methods
    // single lined functions are a reccomendation for inlining, appologies for my style
    public String getMonth() { return this.month; }
    public void setMonth(String eMonth) { this.month = eMonth; }

    public BillsType getType() { return this.type; }
    public void setType(BillsType eType) { this.type = eType; }

    public int getDays() { return this.days; }
    public void setDays(int billsCount) { this.days = billsCount; }

    public double[] getExpenses() { return this.expenses; }
    public void setExpenses(double[] eArr, int len) {
        this.expenses = eArr;
        this.days = len;
    }
    public void setExpenses(double[] eArr) { this.setExpenses(eArr, eArr.length); }
    public void addExpense(double expense) {
        Arrays.copyOf(this.expenses, this.expenses.length + 1);
        this.expenses[this.expenses.length - 1] = expense;
        this.days++;
    }

    // kinda immutable
    public int getId() { return this.id; }
    public static int getCurrentId() { return uid; }


    public boolean equals(MyBills bill) {
        return this.type.equals(bill.getType()) // "equal type of expense name"
            && this.expenses.length == bill.getExpenses().length; // "and equal number of days in that expense ."
    }

    public String toString() {
        String ret = "The month is: " + this.month
            + "\nThe type of expenses is: " + this.type // automatically calls .toString()
            + "\nThe ammounts are: ";

        // list individual expenses
        for (double e : this.expenses)
            ret += String.format("%.2f", e) + ", ";
        // end list w/ closing brace
        ret = ret.substring(0, ret.length() - 2);

        ret += "\nThe number of days for data is: " + days;

        ret += "\nthe expense object id is " + this.id
            + "\nand the static id is " + uid + '\n';

        return ret;
    }

};
