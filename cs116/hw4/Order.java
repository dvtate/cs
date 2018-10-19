


public class Order {
    /*
    • Unique order ID (numeric)
    • Customer ID(alpha)
    • Product ID(alphanumeric)
    • Date of the order
    • Order amount
    */
    private static int uid = 0;
    protected int id;
    protected String customerId, productId, date;
    protected int quantity;

    public Order(String cid, String pid, String date, int quantity) {
        this.customerId = cid;
        this.productId = pid;
        this.date = date;
        this.quantity = quantity;
        id = uid++;
    }
    public Order() {
        this("", "", "", 0);
    }

    // copy constructor
    public Order(Order o) {
        this.id = o.id;
        this.customerId = o.customerId;
        this.productId = o.productId;
        this.date = o.date;
        this.quantity = o.quantity;
    }

    public String getCustomerId() { return this.customerId; }
    public String getProductId() { return this.productId; }
    public String getDate() { return this.date; }
    public int getQuantity() { return this.quantity; }
    public int getId() { return this.id; }

    public void setCustomerId(String cid) { this.customerId = cid; }
    public void setProductId(String pid) { this.productId = pid; }
    public void setDate(String date) { this.date = date; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public char type() { return super.toString().split("@")[0].charAt(0); }

    public String toString() {
        return super.toString()
        + "\nid: " + this.id
        + "\ncustomer id: " + this.customerId
        + "\nproduct id: " + this.productId
        + "\ndate: " + this.date
        + "\nquantity: " + this.quantity;
    }
};
