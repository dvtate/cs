

public class C extends A {
    public double c;

    public C(A a, double c) {
        super(a.a, a.getAA());
        System.out.println("new C(A,double)");
        this.c = c;
    }

    public C() {
        this.c = 0;
        System.out.println("new C()");
    }

    public C(C c) {
        super((A)c);
        this.c = c.c;
        System.out.println("new C(C)");
    }

    public String toString() {
        return super.toString()
            + "\nc: " + c;
    }

    public int m1() {
        int i2= a+(int)(c/2);
        System.out.println("m1 implementation of C is executing now");
        return i2;
    }

}
