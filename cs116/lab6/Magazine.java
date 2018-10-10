public class Magazine extends Record {
    private int issue, vol;
    private String edition, editor;

    public Magazine(int issue, int vol, String edition, String editor) {
        this.issue = issue;
        this.vol = vol;
        this.edition = edition;
        this.editor = editor;
    }

    public Magazine(Record rec, int issue, int vol, String edition, String editor) {
        super(rec);
        //this(issue, vol, edition, editor);
        this.issue = issue;
        this.vol = vol;
        this.edition = edition;
        this.editor = editor;
    }

    public int getIssue() { return this.issue; }
    public int getVol() { return this.vol; }
    public String getEditor() { return this.editor; }
    public String getEdition() { return this.edition; }

    public String toString() {

        return
            super.toString() + "\nissue: " + issue
            + "\nvol: " + vol
            + "\neditor: " + editor
            + "\nedition: " + edition;
    }
};
