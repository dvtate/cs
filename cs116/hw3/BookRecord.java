
package library.service.classes;


public class BookRecord {

    private static int booksid = 0; // internal funcitonality
    private int id;
    public String title, author, tag;
    public int pages;
    public BookGenre genre;

    public BookRecord(String title, String author, String genre, String tag, int pages) {
        this.id = BookRecord.booksid++;
        this.title = title;
        this.author = author;
        this.tag = tag;
        this.pages = pages;
        this.genre = BookGenre.valueOf(genre);
    }

    public BookRecord() { this("", "", "", "", 0); }

    public boolean equals(BookRecord b) {
        return b.pages == this.pages
            && b.title.equals(this.title)
            && b.author.equals(this.author)
            && b.genre.equals(this.genre)
            && b.tag.equals(this.tag);
    }


    //
    public String toString() {
        return "=============================\n"
            + "Record #" + this.id
            + "\n\tTitle: " + this.title
            + "\n\tAuthor(s): " + this.author
            + "\n\tGenre: " + this.genre
            + "\n\tTag: " + this.tag
            + "\n\tPages: " + this.pages
            + "\n=============================\n";
    }


    // useless funcitons (why use access modifiers when u can use public memers?)
    public String       getTitle()      { return this.title; }
    public String       getAuthor()     { return this.author; }
    public BookGenre    getGenre()      { return this.genre; }
    public int          getId()         { return this.id; }
    public static int   getCurrentId()  { return BookRecord.booksid; }
    public String       getTag()        { return this.tag; }
    public int          getPages()      { return this.pages; }

    public void setTitle    (String title)      { this.title    = title; }
    public void setAuthor   (String author)     { this.author   = author; }
    public void setGenre    (BookGenre genre)   { this.genre    = genre; }
    public void setTag      (String tag)        { this.tag      = tag; }
    public void setPages    (int pages)         { this.pages    = pages; }

};
