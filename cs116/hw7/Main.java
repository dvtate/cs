

import java.util.*;
import java.io.*;

public class Main {


    // I initially used an array here, but this is better as it has a default case
    private static String keypad(char c) {
        switch (c) {
            case '2' : return "ABC";
            case '3' : return "DEF";
            case '4' : return "GHI";
            case '5' : return "JKL";
            case '6' : return "MNO";
            case '7' : return "PQRS";
            case '8' : return "TUV";
            case '9' : return "WXYZ";
            default: return "" + c;
        }
    }
    // number only contains 0-9 digits
    Vector<String> listMnemonics(String number) {


        // cut off first char
        // find mnemonics for it
        Vector<String> prefix = new Vector<String>();
        try {
        for (String c : keypad(number.charAt(0)).split(""))
            prefix.add(c);
        } catch (Exception e) {
            prefix.add("" + number.charAt(0));

        }
        if (number.length() <= 1)
            return prefix;

        // call self on remainnig string
        Vector<String> suffix = listMnemonics(number.substring(1, number.length()));

        // combine like a tree
        Vector<String> ret = new Vector<String>();
        for (String pre : prefix)
            for (String suf : suffix)
                ret.add(pre + suf);


        return ret;

    }

    public static void main(String[] args) {
        Main m = new Main();

        for (String s : m.listMnemonics(args[0]))
            System.out.println(s);
    }
}
