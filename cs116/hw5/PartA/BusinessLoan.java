

public class BusinessLoan extends Loan {

    public BusinessLoan(String lastName, double amount, LoanConstants.Term term) {
        super(lastName, amount, term);
        super.setInterestRate(LoanConstants.primeInterestRate + 0.02);

    }
    public BusinessLoan(String lastName, double amount, int termYrs) {
        super(lastName, amount, LoanConstants.Term.at(termYrs));
        super.setInterestRate(LoanConstants.primeInterestRate + 0.02);
    }

};
