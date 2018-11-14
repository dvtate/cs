
public class TwoDayPackage extends Package {
    TwoDayPackage(String fromAddress, String toAddress, float weight) {
        super(fromAddress, toAddress, weight);
    }

    public float calculateCharge() {
        return super.getWeight() * (ChargeConstants.baseCharge + ChargeConstants.twoDayExtraCharge);
    }

    String printRecipt() {
        return "Receipt for Package ID:" + super.getID()
            + "\nType: Two Day"
            + "\nFrom Address: " + super.getFromAddress()
            + "\nTo Address: " + super.getToAddress()
            + "\nWeight: " + super.getWeight() + "oz."
            + "\nCharge: $" + this.calculateCharge() + "\n";
    }
};
