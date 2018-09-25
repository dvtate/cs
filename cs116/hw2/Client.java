
// encapsulated scaleable array of book objects
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

    public boolean contains(BookRecord book) {
        for (int i = 0; i < this.index; i++)
            if (book.equals(this.arr[i]))
                return true;
        return false;
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
            while (fin.hasNext()) {
                // Nikola Tesla:GENRE_HISTORY:Sean Patrick
                String[] TAG = fin.next().split(":");
                BookRecord rec = new BookRecord(TAG[0], TAG[2], TAG[1]);

                // have we seen this book before?
                if (books_db.contains(rec))
                    System.out.println("Duplicate found:\n" + rec);

                // expansion handled by this method
                else
                    books_db.push(rec);
            }


        } catch (IOException ioe) {
            System.out.println("The file can not be read");
        }


        while(true){
            System.out.println("Select an option:");
            System.out.println("\tType \"S\" to list books of a genre");
            System.out.println("\tType \"P\" to print out all the book records");
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

            case 'Q': case 'q':
                System.out.println("Quitting program");
                return;

            default:
                System.out.println("Wrong option! Try again");

            }
        }

    }
};
