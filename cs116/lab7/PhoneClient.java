
import java.util.*;
import java.io.*;


public class PhoneClient {
	public static void main(String[] args) {

		// create a Phone
		Phone phone1 = new Phone("312", "867", "5309");
		System.out.println("phone1:" + phone1.toString() + "\n");

		// create a BusinessPhone
		BusinessPhone businessPhone1 = new BusinessPhone("312", "567", "3000", "5148");
		System.out.println("businessPhone1:" + businessPhone1.toString() + "\n");

		// create a BusinessPhone from a Phone
		BusinessPhone businessPhone2 = new BusinessPhone(phone1, "1111");
		System.out.println("businessPhone2:" + businessPhone2.toString() + "\n");

		// change the Phone
		phone1.setArea("773");
		System.out.println("phone1:" + phone1.toString() + "\n");

		// output the BusinessPhone created from the Phone
		System.out.println("businessPhone2:" + businessPhone2.toString() + "\n");

		// call Phone method on a BusinessPhone
		System.out.println("businessPhone1 exchange:" + businessPhone1.getExchange() + "\n");


		// try to create a bad phone number
		//Phone phone2 = new Phone("111", "867", "309");
		//System.out.println("phone2:" + phone2.toString() + "\n");

		// call a BusinessPhone method on a Phone
		//This should not work. why?
        // -> (inheritance goes one way) because phone1 is an instance of the Phone
        //    class and the `getExtension()` function is a member of class BusinessPhone

		//String temp = phone1.getExtension();

        System.out.println("What type of phone would you like to make?\n\tA:Phone\n\tB:BusinessPhone\n");
        Scanner cin = new Scanner(System.in);
        String type = cin.nextLine();

        System.out.printf("Enter the area code: ");
        String area = cin.nextLine();

        System.out.printf("Enter the exchange code: ");
        String exch = cin.nextLine();

        System.out.printf("Enter the number: ");
        String num = cin.nextLine();



        if (type.charAt(0) == 'B') {
            System.out.printf("Enter the extension code: ");
            String ext = cin.nextLine();
            BusinessPhone uphone = new BusinessPhone(area, exch, num, ext);
            System.out.println("BusinessPhone uphone: " + uphone.toString());

        } else {
            Phone uphone = new Phone(area, exch, num);
            System.out.println("Phone uphone: " + uphone.toString());

        }




	}

};
