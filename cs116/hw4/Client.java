

import java.util.*;
import java.io.*;


public class Client {


    public static int cmpDates(String d1, String d2) {
        String[] d1a = d1.split("/");
        String[] d2a = d2.split("/");
        int[] diffs = new int[3];

        // american date format forces me to write ugly code :(
        int diff;

        diff = Integer.parseInt(d2a[2]) - Integer.parseInt(d1a[2]);
        if (diff != 0)
            return diff;
        diff = Integer.parseInt(d2a[0]) - Integer.parseInt(d1a[0]);
        if (diff != 0)
            return diff;
        diff = Integer.parseInt(d2a[1]) - Integer.parseInt(d1a[1]);
        if (diff != 0)
            return diff;

        return 0;

    }


	// return sorted version of arraylist
	public static ArrayList<Order> sort(ArrayList<Order> orders) {

		for (int i = 0; i < orders.size(); i++) {
			// find next highest elem (local max)
			int mi = i;
			String m = orders.get(mi).getDate();

			for (int j = i; j < orders.size(); j++)
				if (cmpDates(orders.get(j).getDate(), m) > 0) {
					mi = j;
					m = orders.get(j).getDate();
				}

			// swap
			Order tmp = orders.get(mi);
			orders.set(mi, orders.get(i));
		    orders.set(i, tmp);


		}
		return orders;

	}


    public static void main(String cripplingDepression[]) {

        ArrayList<Order> orders = new ArrayList<Order>();

        try {
            Scanner fin = new Scanner(new File("orders.txt")).useDelimiter("\\Z");
            String[] lines = fin.next().split("\n");


            for (String l : lines) {
                // : O/R, customerID, productID, orderDate, orderAmount, and if a repeated order, period and endDate.
                String[] e = l.split(",");
                if (e[0].equals("R"))
                    orders.add(new RepeatedOrder(new Order(e[1],e[2],e[3],Integer.parseInt(e[4].trim())), Integer.parseInt(e[5].trim()), e[6]));
                else if (e[0].equals("O"))
                    orders.add(new Order(e[1],e[2],e[3],Integer.parseInt(e[4])));
                else
                    System.out.println("irregular entry ignored...");
            }

        } catch (IOException e) {
            System.out.println(e);
            return;
        }

        // sort by date
        orders = sort(orders);


        Scanner cin = new Scanner(System.in);
        for (;;) {

            System.out.println("enter a command:\n\tOption ‘A’: add an order\n\tOption ‘D’: delete an order\n\tOption ‘L’: List all the orders for a particular customer ID");
            char cmd = cin.nextLine().charAt(0);

            switch (cmd) {

            case 'A': case 'a':
                System.out.print("Enter order in following format(separated by commas):\n\t\tO/R (one-time or repeating), customerID, productID, orderDate, orderAmount, and if a repeated order, period and endDate.\n:");
                String[] e = cin.nextLine().trim().split(",");
                if (e[0].equals("R"))
                    orders.add(new RepeatedOrder(new Order(e[1],e[2],e[3],Integer.parseInt(e[4].trim())), Integer.parseInt(e[5].trim()), e[6]));
                else if (e[0].equals("O"))
                    orders.add(new Order(e[1],e[2],e[3],Integer.parseInt(e[4])));
                else {
                    System.out.println("follow format guides for entering .");
                    break;
                }
                System.out.println("\nAdded:\n" + orders.get(orders.size() - 1).toString() + "\n");
                break;
            case 'D': case 'd':
                System.out.print("enter order id: ");
                int id = Integer.parseInt(cin.nextLine().trim());
                for (int i = 0; i < orders.size(); i++)
                    if (orders.get(i).getId() == id) {
                        System.out.println("\nDeleted:\n" + orders.get(i).toString() + "\n");
                        orders.remove(i);
                        break;
                    }
                break;
            case 'L': case 'l':
                System.out.print("Enter Customer ID: ");
                String cid = cin.nextLine().trim();
                for (Order o : orders)
                    if (o.getCustomerId().equals(cid))
                        if (o.type() == 'R') {
                            System.out.println(((RepeatedOrder)o).toString() + "\n");
                        } else {
                            System.out.println(o.toString() + "\n");
                        }
                break;

            }



        }
    }

};
