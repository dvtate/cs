

public class B extends A {
    public String s; // this is public and thus doesnt need mutiliators


    public B(A a, String s) {
        super(a.a, a.getAA());
        System.out.println("new B(A(a,aa),s)");
        this.s = s;
    }

    public B() {
        System.out.println("new B()");
        this.s = "empty";
    }
    public String toString() {
        return super.toString()
            + "\ns: " + s;
    }

    public int m1(){
        int i1= 5*a+(int)super.getAA();
        System.out.println("m1 implementation of B is executing now");
        return i1;
    }
}
