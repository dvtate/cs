
/*
    it is stated that the value must be set by the user and be an `interface class`
    but interfaces only allow static and final members, so i implemeted it.
*/


interface stuff {

    public static final String companyName = "Sanchez Construction Loan Co.";
    public static final double maxAmount = 100000;
    public static final int shortTerm = 1, mediumTerm = 3, longTerm = 5;
    public static enum Term {
        SHORT(1), MEDIUM(3), LONG(5);

        public final int years;
        Term(int years) {
            this.years = years;
        }

        public boolean includes(int years) {
            return years == 1 || years == 3 || years == 5;
        }
        // defaults to short term loan of 1 yrs
        public static Term at(int years) {
            return years == 3 ?
                Term.MEDIUM : years == 5 ?
                    Term.LONG : Term.SHORT;
        }
    };

    public static char currency = '$';

}


public class LoanConstants implements stuff {
    public static double primeInterestRate = 0.0233;
    // should be a pure virtual constructor or a namespace
}
