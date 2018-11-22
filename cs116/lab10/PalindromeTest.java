
import java.util.*;
import java.io.*;

public class PalindromeTest {


    private static boolean testPalindrome(String str) {
        str = str.trim();

        if (str.length() < 2)
            return true;


        if (str.charAt(0) != str.charAt(str.length() - 1))

            return false;

        int start = 1, end = str.length() - 1;
        while (str.charAt(start) == ' ')
            start++;
        while (str.charAt(end) == ' ')
            end--;


        return testPalindrome(str.substring(start, end));
    }

    public static void main(String[] arrgs) {
        Scanner cin = new Scanner(System.in);
        System.out.println("Enter a word/phrase");
        String in = cin.nextLine();
        System.out.println(in  + " is " + ( testPalindrome(in) ? "" : "not " ) + "a palindrome");

    }
}
