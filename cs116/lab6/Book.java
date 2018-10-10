public class Book extends Record {
    private String authors, edition;

    public Book(String authors, String edition) {
        this.authors = authors;
        this.edition = edition;
    }
    public Book(Record rec, String authors, String edition) {
        super(rec);
        this.authors = authors;
        this.edition = edition;
    }

    public String getAuthors() { return this.authors; }
    public void setAuthors(String in) { this.authors = in; }


    public String toString() {
        return super.toString()
            + "\nauthors: " + authors
            + "\nedition: " + edition;
    }
}
