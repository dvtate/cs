

public abstract class Loan {
        private static int uid = 0;
        protected int id;
        protected double amount, interestRate;
        protected String lastName;
        protected LoanConstants.Term term;

        public Loan(String lastName, double amount, LoanConstants.Term term) {

            if (amount > 100000)
                throw new Error("Loan amount too high");

            this.amount = amount;
            this.lastName = lastName;
            this.term = term;

            this.id = uid++;
        }

        public Loan(String lastName, double amount, int termYrs) {
            this(lastName, amount, LoanConstants.Term.at(termYrs));
        }

        // get
        public int getId() { return this.id; }
        public double getAmount() { return this.amount; }
        public double interestRate() { return this.interestRate; }
        public String getLastName() { return this.lastName; }
        public LoanConstants.Term getTerm() { return this.term; }
        public int getTermYrs() { return this.term.years; }
        // set
        public void setInterestRate(double in) { this.interestRate = in; }
        public void setAmount(double in) { this.amount = in; }
        public void setLastName(String in) { this.lastName = in; }
        public void setTerm(LoanConstants.Term in) { this.term = in; }

        public double amountOwed() {
            // Principle  ( 1 + rate * time)
            return this.amount * (1 + this.interestRate * this.term.years);
        }

        public String toString() {
            return super.toString()
                + "\nid: " + this.id
                + "\namount: $" + (Math.round(this.amount * 100) / 100)
                + "\ninterest rate: " + (this.interestRate * 100) + "%"
                + "\nlast name: " + this.lastName
                + "\nterm: " + this.term.toString()
                + "\nOwed at due date: $" + (Math.round(this.amountOwed() * 100.0) / 100.0)
                + "\n";
        }

}
