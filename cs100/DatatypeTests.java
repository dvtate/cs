public class DatatypeTests {
    public static short factorial (short x) {
        short fact = 1;
        while (x > 1) {
            fact = (short)(fact * x);
            x = (short)(x - 1);
        }
        return fact;
    }

    public static int factorial (int x) {
        int fact = 1;
        while (x > 1) {
            fact = fact * x;
            x = x - 1;
        }
        return fact;
    }

    public static double factorial (double x) {
        double fact = 1.0;
        while (x > 1) {
            fact = fact * x;
            x = x - 1;
        }
        return fact;
    }

    public static double invfactorial (double x) {
        return 1.0 / factorial(x);
    }

    public static float factorial (float x) {
        float fact = 1.0F;
        while (x > 1) {
            fact = fact * x;
            x = x - 1;
        }
        return fact;
    }

    public static float invfactorial (float x) {
        return 1.0F / factorial(x);
    }

    public static void main (String[] args) {
        double total = 0;
        for (int i = 0; i < 100; i++) {
            System.out.println("total: " + total);
            total += 0.25;
        }

    }
}
