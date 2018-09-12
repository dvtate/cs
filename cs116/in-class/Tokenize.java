import java.util.*;
import java.io.*;

public class Tokenize {
    public static void main(String[] args){
        try {
            File feed = new File("feed.txt");
            Scanner scan = new Scanner(feed);

            while (scan.hasNextLine()) {
                final String[] tokens = scan.nextLine().split(" ");
                for (int i = 0; i < tokens.length; i++)
                    System.out.println(tokens[i]);
            }


        } catch (IOException e) {
            System.out.println("error");
            return;
        }


    }

};
