
import java.util.*;
import java.io.*;


public class Log {
    public String contents;
    public String fname;

    public Log(String fname) {
        this.fname = fname;
    }
    public Log() { this("out.txt"); }

    public void put(String line) {
        //System.out.println(line);
        this.contents += line + "\n";
    }

    // write to file...
    public void write() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fname, false));
            bw.write(contents);
            bw.close();
        } catch (IOException e) {
            System.out.println("Logfile failed to write");
        }
    }

    public String toString() {
        return this.contents;
    }
};
