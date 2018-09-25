
package library.service.classes;


public class BookRecord {

    private static int booksid = 0; // internal funcitonality
    private int id;
    public String title, author;
    public BookGenre genre;

    public BookRecord(String title, String author, String genre) {
        this.id = BookRecord.booksid++;
        this.title = title;
        this.author = author;
        this.genre = BookGenre.valueOf(genre);
    }

    public BookRecord() { this("", "", ""); }

    public boolean equals(BookRecord b) {
        return (b.title.equals(this.title)
            && b.author.equals(this.author)
            && b.genre.equals(this.genre));
    }

    //
    public String toString() {
        return "=============================\n"
            + "Record #" + this.id
            + "\n\tTitle: " + this.title
            + "\n\tAuthor(s): " + this.author
            + "\n\tGenre: " + this.genre
            + "\n=============================\n";
    }


    // useless funcitons (access modifiers)
    public String getTitle() { return this.title; }
    public String getAuthor() { return this.author; }
    public BookGenre getGenre() { return this.genre; }
    public int getId() { return this.id; }
    public static int getCurrentId() { return BookRecord.booksid; }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setGenre(BookGenre genre) { this.genre = genre; }


};
