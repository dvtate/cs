
import java.util.*;
import java.io.*;


public class EnumPractice {
    enum Day { MON, TUE, WED, THR, FRI, SAT, SUN };

    public static void main(String[] args) {
        Day start = Day.WED, end = Day.SAT;

        System.out.println(start + " comes "
            + (start.compareTo(end) > 0 ? "after" : "before") + " " + end);

        System.out.println(start.toString() + ' ' + (start.equals(end) ? "==" : "!=") + " " + end);

        System.out.println(start.toString() + " day number " + start.ordinal());
        System.out.println(end.toString() + " day number " + end.ordinal());

        start = Day.valueOf("TUE");


    }

};
