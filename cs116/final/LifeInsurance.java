

public class LifeInsurance extends Insurance {
    public LifeInsurance() {
        this.setCost();
        super.setType("Life");
    }
    public LifeInsurance(int age, String name) {
        super(age, name, "Life");
        this.setCost();
    }
    public void setCost() { this.cost = 40; }
};
