

public class HealthInsurance extends Insurance {

    public HealthInsurance() {
        this.setCost();
        super.setType("Health");
    }

    public HealthInsurance(int age, String name) {
        super(age, name, "Health");
        this.setCost();
    }

    public void setCost() { this.cost = 180; }

};
