
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
        this.contents += line + "\n";
    }
    public void write() {

    }
};
