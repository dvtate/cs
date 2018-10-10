public class Newspaper extends Record {

    private String editor, edition;
    public Newspaper(String editor, String edition) {
        this.editor = editor;
        this.edition = edition;
    }

    public Newspaper(Record rec, String editor, String edition) {
        super(rec);
        this.editor = editor;
        this.edition = edition;
    }

    public String getEdition() { return this.edition; }
    public String getEditor() { return this.editor; }

    public String toString() {
        return super.toString()
            + "\neditor: " + editor
            + "\nedition: " + edition;
    }
};
