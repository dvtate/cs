
import library.service.classes.BookRecord;
import library.service.classes.BookGenre;

import java.util.*;
import java.io.*;

/*
*    initially i made a separate class called BookArray in order to encapsulate
*    this functionality, but after i read the assignment again i realzed it wanted
*    it all in one class. My solution was to simply add a main method to the class
*    i created and change the name to client. :^)
*/

class Client {
    protected BookRecord[] arr;
    public int expansionFactor = 2;


    // current max element index
    protected int index;

    public Client(int initSize) {
        arr = new BookRecord[initSize];
        index = 0;
    }
    public Client() { this(5); }

    //
    public void push(BookRecord book) {
        if (this.index + 2 > this.arr.length)
            grow();
        this.arr[index++] = book;
    }

    public BookRecord[] getArr() { return this.arr; }
    public BookRecord at(int index) { return this.arr[index]; }
    public void set(int index, BookRecord book) { this.arr[index] = book; }

    // change to given size
    // replace arr with bigger array and copy values
    private void realloc(int size) {
        BookRecord[] newArr = new BookRecord[size];
        for (int i = 0; i < this.index; i++)
            newArr[i] = this.arr[i];
        System.out.println("Resized array from " + this.arr.length + " to " + newArr.length);
        this.arr = newArr;
        newArr = null;
    }

    // i would have preferred to have used multiplied the current size by two, (like std::vector in C++)
    // but the assignment says to add the expansion factor
    public void grow() {
        this.realloc(this.arr.length + this.expansionFactor);
    }
    public void grow(int addedElems) { this.realloc(this.arr.length + addedElems); }

    // remove null elements
    public void trimSize() {
        BookRecord[] newArr = new BookRecord[index];
        for (int i = 0; i < this.index; i++)
            newArr[i] = this.arr[i];
        this.arr = newArr;
        newArr = null;
    }

    public boolean contains(BookRecord book) {
        for (int i = 0; i < this.index; i++)
            if (book.equals(this.arr[i]))
                return true;
        return false;
    }

    /* i wrote this to emulate what I'm used to in c, but apparently this is built
    *    into the langauge..
    private static int strcmp(String a, String b) {
        // should all be 8 chars but still adding this
        // shorter strings sorted higher
        if (a.length() != b.length())
            return b.length() - a.length();

        // compare chars until one is different
        for (int i = 0; i < a.length(); i++)
            if (a.charAt(i) != b.charAt(i))
                return a.charAt(i) - b.charAt(i);

        // identical strings :/
        return 0;
    }
    */

    public BookRecord [] sortString(BookRecord[] myArray, int noRecords) {

        for (int i = 0; i < noRecords; i++) {

            // initially assume lowest val is first one (prolly isnt)
            int ind = i;
            String lval = myArray[i].tag;

            // find actual lowest value from values remaining
            for (int j = i; j < noRecords; i++)
                if (lval.compareTo(myArray[j].tag) > 0) {
                    ind = j;
                    lval = myArray[j].tag;
                }

            // swap with first elem
            BookRecord tmp = myArray[i];
            myArray[i] = myArray[ind];
            myArray[ind] = tmp;
        }

        return myArray;
    }

    public BookRecord [] sortPages(BookRecord[] myArray, int noRecords) {

        for (int i = 0; i < noRecords; i++) {

            // initially assume lowest val is first one (prolly isnt)
            int ind = i, lval = myArray[i].pages;

            // find actual lowest value from values remaining
            for (int j = i; j < noRecords; i++)
                if (lval < myArray[j].pages) {
                    ind = j;
                    lval = myArray[j].pages;
                }

            // swap with first elem
            BookRecord tmp = myArray[i];
            myArray[i] = myArray[ind];
            myArray[ind] = tmp;
        }

        return myArray;
    }

    public void searchTag(String tag) {
        int len = this.index, ind = len / 2;

        while (len > 0) {
            if (arr[ind].tag.compareTo(tag) < 0) {
                // eliminate half of elems
                len /= 2;
                // index moves to new center
                ind -= len / 2;
                System.out.println("lt");

            } else if (arr[ind].tag.compareTo(tag) < 0) { // compareTo > 0
                // eliminate half of elems
                len /= 2;
                // index moves to new center
                ind += len / 2;

            } else {
                System.out.println(arr[ind]);
                return;
            }

        }
    }

    public static void main(String[] args) {
        //arg[0]: text file //arg[1]: resize factor

        // array starts with 5 elems
        Client books_db = new Client(5);

        // expansionFactor from terminal args
        books_db.expansionFactor = Integer.parseInt(args[1]);

        // populate arr
        try {

            Scanner fin = new Scanner(new File(args[0])).useDelimiter("\\n");
            // Nikola Tesla:GENRE_HISTORY:Sean Patrick:NJKG7456:987
            // title : genre : author : tag : pages
            while (fin.hasNext()) {
                String[] label = fin.next().split(":");
                BookRecord rec = new BookRecord(label[0], label[2], label[1], label[3], Integer.parseInt(label[4].trim()));

                // have we seen this book before?
                if (books_db.contains(rec))
                    System.out.println("Duplicate found:\n" + rec);

                // expansion handled by this method
                else
                    books_db.push(rec);
            }

            books_db.arr = books_db.sortString(books_db.arr, books_db.index);


        } catch (IOException ioe) {
            System.out.println("The file can not be read");
        }


        for (;;) {
            System.out.println("Select an option:");
            System.out.println("\tType \"S\" to list books of a genre");
            System.out.println("\tType \"P\" to print out all the book records");
            System.out.println("\tType \"T\" to search for a record with a specific tag");
            System.out.println("\tType \"Q\" to Quit");

            Scanner cin = new Scanner(System.in);
            char opt = cin.nextLine().charAt(0);

            switch (opt) {
            case 'S': case 's':
                System.out.println("Type a genre. The genres are:");
                for (BookGenre d : BookGenre.values())
                    System.out.println("\t" + d);

                BookGenre genre = BookGenre.valueOf(cin.nextLine().trim());

                for (BookRecord book : books_db.getArr())
                    if (book != null && book.genre.equals(genre))
                        System.out.println(book);

                break;

                //list out all the records
            case 'P': case 'p':
                for (BookRecord book : books_db.getArr())
                    if (book != null)
                        System.out.println(book);
                break;

            case 'T': case 't':
                System.out.print("Enter the tag:");
                String tag = cin.nextLine();
                books_db.searchTag(tag);
                break;

            case 'Q': case 'q':
                System.out.println("Quitting program");
                return;

            default:
                System.out.println("Wrong option! Try again");

            }
        }
    }
};
