

public class Record {
    private static int uid = 0;
    protected int id;
    protected String title, date;

    public Record(String title, String date) {
        this.title = title;
        this.date = date;
        this.id = uid++;
    }
    public Record() {
        this(null, null);
    }
    public Record(Record rec) {
        this.id = rec.id;
        this.title = rec.title;
        this.date = rec.date;
    }

    public String getTitle() { return this.title; }
    public int getId() { return this.id; }
    public String getDate() { return this.date; }
    public static int getUid() { return Record.uid; }

    protected void setTitle(String title) { this.title = title; }
    protected void setDate(String date) { this.date = date; }


    public String toString() {
        return super.toString()
            + "\nid: " + id
            + "\ntitle: " + title
            + "\ndate: " + date;
    }
};
