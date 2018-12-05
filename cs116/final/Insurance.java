


public abstract class Insurance {
    private static int uid = 0;
    protected int id, cost, age;
    protected String name, type;

    public Insurance() { this.setPolicyID(); }

    public Insurance(int age, String name, String type) {
        this.setAge(age);
        this.setName(name);
        this.setType(type);
    }
    public void setPolicyID() { this.id = uid++; }
    public int getPolicyID() { return this.id; }

    public abstract void setCost();
    public int getCost() { return this.cost; }

    public void setName(String policyHolderName) { this.name = policyHolderName; }
    public String getName() { return this.name; }

    public void setAge(int policyHolderAge) { this.age = policyHolderAge; }
    public int getAge() { return this.age; }

    public void setType(String policyType) { this.type = policyType; }
    public String getType() { return this.type; }
};
