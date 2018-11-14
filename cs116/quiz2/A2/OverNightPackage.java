
public class OverNightPackage extends Package {
    OverNightPackage(String fromAddress, String toAddress, float weight) {
        super(fromAddress, toAddress, weight);
    }

    
    public float calculateCharge() {
        return super.getWeight() * (ChargeConstants.baseCharge + ChargeConstants.overNightExtraCharge);
    }

    String printRecipt() {
        return "Receipt for Package ID:" + super.getID()
            + "\nType: Overnight"
            + "\nFrom Address: " + super.getFromAddress()
            + "\nTo Address: " + super.getToAddress()
            + "\nWeight: " + super.getWeight() + "oz."
            + "\nCharge: $" + this.calculateCharge() + "\n";
    }
};
