


 public class Phone {
     protected String areaCode, exchange, number;


     public Phone(String areaCode, String exchange, String number) {

            Integer.parseInt(areaCode);
            Integer.parseInt(exchange);
            Integer.parseInt(number);

            if (areaCode.length() != 3)
                throw new Error("Area Code must be 3 digits");
            if (exchange.length() != 3)
                throw new Error("Exchange must be 3 digits");
            if (number.length() != 4)
                throw new Error("Number must be 4 digits");

            if (areaCode.charAt(0) < '2' || areaCode.charAt(0) > '9')
                throw new Error("Areacode first digit must between 2 and 9");

            this.areaCode = areaCode;
            this.exchange = exchange;
            this.number = number;
     }

      public Phone() {
          this("999", "999", "9999");
      }

      public String getArea() { return this.areaCode; }
      public String getExchange() { return this.exchange; }
      public String getNumber() { return this.number; }

      public void setArea(String areaCode) { this.areaCode = areaCode; }
      public void setExchange(String exchange) { this.exchange = exchange; }
      public void setNumber(String number) { this.number = number; }

      public String toString() {
          return "(" + this.areaCode + ") " + this.exchange + " - " + this.number;
      }
}
