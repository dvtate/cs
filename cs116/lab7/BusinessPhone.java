


public class BusinessPhone extends Phone {
    protected String extension;

    public BusinessPhone(String areaCode, String exchange, String number, String extension) {
        super(areaCode, exchange, number);

        Integer.parseInt(extension);
        if (extension.length() != 4)
            throw new Error("Extension must be 4 digits");
        this.extension = extension;
    }
    public BusinessPhone() {
        this("999", "999", "9999", "9999");
    }
    public BusinessPhone(Phone phone, String extension) {
        this(phone.getArea(), phone.getExchange(), phone.getNumber(), extension);
    }

    public String getExtension() { return this.extension; }
    public void setExtension(String extension) { this.extension = extension; }

    public String toString() {
        return super.toString() + " - " + this.extension;
    }
};
