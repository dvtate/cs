


public class RepeatedOrder extends Order {

    protected int period; // days in between orders
    protected String end; // final order date

    public RepeatedOrder(Order o, int period, String end) {
        super(o);
        this.period = period;
        this.end = end;
    }

    public int getPeriod() { return this.period; }
    public String getEnd() { return this.end; }

    public void setPeriod(int per) { this.period = per; }
    public void setEnd(String end) { this.end = end; }

    public String toString() {
        return super.toString()
            + "\nperiod: " + this.period
            + "\nend: " + this.end;
    }

};
