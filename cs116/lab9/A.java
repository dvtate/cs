

abstract public class A {
    public int a;
    private double aa;

    public A(int a, double aa) {
        System.out.println("new A(int, double)");
        this.a = a;
        this.aa = aa;
    }
    public A() {
        System.out.println("new A()");
        this.a = 0;
        this.aa = 0.0;
    }

    public A(A a) {
        System.out.println("new A(A)");
        this.a = a.a;
        this.aa = a.getAA();
    }

    public double getAA() { return aa; }
    public void setAA(double aa) { this.aa = aa; }

    public String toString() {
        return super.toString()
            + "\na: " + this.a
            + "\naa: " + this.aa;
    }


    public int m2(char a) {
        int x = (int) a;
        System.out.println("m2 of A is executing now");
        return x;
    }

    public int m2(int x1) {
        int y = 10 + x1;
        System.out.println("second version of m2 in a is executing now");
        return y;
    }

    public void m3() {
        System.out.println("m3 of a is executing now");
    }

    public abstract int m1();
}
