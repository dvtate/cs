
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

    // equicalent to overloading operator[]
    public BookRecord[] getArr() { return this.arr; }
    public BookRecord at(int index) { return this.arr[index]; }
    public void set(int index, BookRecord book) { this.arr[index] = book; }

    // how many elems
    public int size() { return this.index; }
    public int length() { return this.size(); }

    // change to given size
    // replace arr with bigger array and copy values
    private void realloc(int size) {
        // if (size < index) { throw }
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

    // is elem in arr?
    public boolean contains(BookRecord book) {
        for (int i = 0; i < this.index; i++)
            if (book.equals(this.arr[i]))
                return true;
        return false;
    }

    // selsction sort
    public BookRecord [] sortString(BookRecord[] myArray, int noRecords) { // required signature

        for (int i = 0; i < noRecords; i++) {

            // initially assume lowest val is first one (prolly isnt)
            int ind = i;
            String lval = myArray[i].tag; // char** lval = &arr[i]

            // find actual lowest value from values remaining
            for (int j = i; j < noRecords; j++)
                // if lower than first/current lowest, make that our new lowest value
                if (lval.compareTo(myArray[j].tag) > 0) // strcmp(*lval, arr[j].tag)
                    lval = myArray[ind = j].tag; // lval = &arr[ind]

            // swap with first elem // std::swap
            BookRecord tmp = myArray[i];
            myArray[i] = myArray[ind];
            myArray[ind] = tmp;
        }

        return myArray;
    }

    // selsction sort
    public BookRecord [] sortPages(BookRecord[] myArray, int noRecords) { // required signature

        for (int i = 0; i < noRecords; i++) {

            // initially assume lowest val is first one (prolly isnt)
            int ind = i, lval = myArray[i].pages;

            // find actual lowest value from values remaining
            for (int j = i; j < noRecords; j++)
                // if lower than first/current lowest, make that our new lowest value
                if (lval > myArray[j].pages)
                    lval = myArray[ind = j].pages;

            // swap with first elem
            BookRecord tmp = myArray[i];
            myArray[i] = myArray[ind];
            myArray[ind] = tmp;
        }

        return myArray;
    }

    // BST
    public void searchTag(String tag) {

        int start = 0, end = index - 1;

        while (end >= start) {
            final int mid = (start + end) / 2; // avg

            // could be optimized into cmp; jlt; jgt; je
            // not sure how jvm works tho so prolly different
            final int cmp = arr[mid].tag.compareTo(tag);
            if (cmp == 0) {
                System.out.println(arr[mid]);
                return;
            } else if (cmp > 0) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }
        System.out.println("No book with tag " + tag + " was found");
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

            // for each line
            while (fin.hasNext()) {
                // title : genre : author : tag : pages
                final String[] label = fin.next().split(":");
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

                final BookGenre genre = BookGenre.valueOf(cin.nextLine().trim());

                // sort by pages
                final BookRecord[] sortedByPages
                    = books_db.sortPages(books_db.getArr(), books_db.size());

                for (BookRecord book : sortedByPages)
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
                final String tag = cin.nextLine().trim();
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
