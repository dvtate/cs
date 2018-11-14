


public abstract class Package {
    protected String fromAddress, toAddress;
    protected float weight;
    protected int packageId;
    private static int idCounter = 10000; // must be 5 digits

    // constructors
    public Package(String from, String to, float weight) {
        this.fromAddress = from;
        this.toAddress = to;
        this.weight = weight;

        this.setID();

    }


    // access modifiers
    public void setFromAddress(String address) { this.fromAddress = address; }
    public void setToAddress(String address) { this.toAddress = address; }
    public void setWeight(float weight) { this.weight = weight; }
    public String getFromAddress() { return this.fromAddress; }
    public String getToAddress() { return this.toAddress; }
    public float getWeight() { return this.weight; }
    public int getID() { return this.packageId; }


    public void setID() {
        this.packageId = Package.idCounter++;
        if (this.packageId > 99999)
            throw new Error("maximum package id reached (package id must be 5 digits)");
    }



    public float calculateCharge() {
        return ChargeConstants.baseCharge * this.weight;
    }
    abstract String printRecipt();


    public boolean equals(Package pkg) {
        return this.getFromAddress() == pkg.getFromAddress()
            && this.getToAddress() == pkg.getToAddress()
            && this.getWeight() == pkg.getWeight();

            // uncomment if u want to test to make sure they are same type of package
            //&& this.calculateCharge() == pkg.calculateCharge();
    }
};
