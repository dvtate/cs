
import java.util.*;
import java.io.*;

public class CreateLoans {

    public static void main(String a[]) {

        Scanner cin = new Scanner(System.in)
            // my computer is in danish so i have to do this or else it will expect commas instead of decimals
            .useLocale(Locale.ENGLISH);

        System.out.printf("Please Enter the current prime interest rate as a percentage: ");



        LoanConstants.primeInterestRate = Double.parseDouble(cin.nextLine().trim()) / 100;

        Loan loans[] = new Loan[5];

        for (int i = 0; i < 5; i++) {



            System.out.printf("Please enter your last name: ");
            String lastName = cin.nextLine();

            char type;
            do {
                System.out.printf("Business or Personal? ");
                type = cin.nextLine().charAt(0);
            } while (type != 'P' && type != 'p' && type != 'b' && type != 'B');


            System.out.printf("Term length (short, medium, long): ");

            LoanConstants.Term term;
            try {
                term = LoanConstants.Term.valueOf(cin.nextLine().trim().toUpperCase());
            } catch(IllegalArgumentException e) {
                term = LoanConstants.Term.SHORT;
                System.out.println("Assumed you wanted a short-term loan. Use ^C if thats not what you meant");
            }

            double amount;
            do {
                System.out.printf("Enter the amount: $");
                amount = Double.parseDouble(cin.nextLine().trim());

                if (amount < 0)
                    System.out.println("You would have to open a savings account with that, not a loan");
                else if (amount > LoanConstants.maxAmount) // if the constructor is called with this value it will error
                    System.out.println("We only provide loans of up to $100,000");
                else if (amount == 0)
                    System.out.println("A loan of nothing is not a loan");

            } while (amount <= 0 || amount > LoanConstants.maxAmount);


            if (type == 'p' || type == 'P')
                loans[i] = new PersonalLoan(lastName, amount, term);
            else
                loans[i] = new BusinessLoan(lastName, amount, term);


            System.out.println("\n");
        }

        for (Loan l : loans)
            System.out.println(l.toString());
    }

};
