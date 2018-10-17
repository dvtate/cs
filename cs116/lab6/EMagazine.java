

public class EMagazine extends Record {
    private int issue, vol;
    private String editor, url;

    public EMagazine(int issue, int vol, String url, String editor) {
        this.issue = issue;
        this.vol = vol;
        this.editor = editor;
        this.url = url;
    }

    public EMagazine(Record rec, int issue, int vol, String edition, String editor) {
        super(rec);
        this.issue = issue;
        this.vol = vol;
        this.url = edition;
        this.editor = editor;
    }

    public int getIssue() { return this.issue; }
    public int getVol() { return this.vol; }
    public String getEditor() { return this.editor; }
    public String getUrl() { return this.url; }

    public String toString() {
        return super.toString()
            + "\nissue: " + issue
            + "\nvol: " + vol
            + "\neditor: " + editor
            + "\nurl: " + url;
    }
};
