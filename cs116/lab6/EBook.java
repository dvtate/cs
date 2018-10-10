public class EBook extends Record {
    private String authors, url;

    public EBook(String authors, String url) {
        this.authors = authors;
        this.url = url;
    }
    public EBook(Record rec, String authors, String url) {
        super(rec);
        this.authors = authors;
        this.url = url;
    }

    public String getAuthors() { return this.authors; }
    public String getUrl() { return this.url; }


    public String toString() {
        return super.toString()
            + "\nauthors: " + authors
            + "\nurl: " + url;
    }
}
