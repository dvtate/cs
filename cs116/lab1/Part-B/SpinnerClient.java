

public class SpinnerClient {
    public static void main(String[] args){
        Spinner d1 = new Spinner(),
                d2 = new Spinner(20, 5);

        System.out.println("d1: " + d1.toString());
        System.out.println("d2: " + d2.toString());

        System.out.println("d1 + d2 = " + (d1.getArrow() + d2.getArrow()));

        System.out.print("Spinning...");
        d1.spin();
        d2.spin();
        System.out.println("done");

        System.out.println("d1 " + ( d1.equals(d2) ? "==" : "!=" ) + " d2" );


        System.out.println("d1: " + d1.toString());
        System.out.println("d2: " + d2.toString());

        System.out.println("d1 + d2 = " + (d1.getArrow() + d2.getArrow()));



    }
};
