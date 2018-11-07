
public class D extends C {
    public String str;

    public D(C c, String str) {
        super(c);
        this.str = str;
        System.out.println("new D(C,String)");
    }

    public D() {
        this.str = "empty";
        System.out.println("new D()");
    }

    public void m3()
    {
        //place the code here to execute the code from method m3 of class A first
        super.m3();
        System.out.println("I am executing m3 as implemented in class D");
    }


    public int m2(char a) {
        if (a == 'e') {
            final int ret = super.m2('e');
            System.out.println("D.m2(char) executing now with return value = " + ret);
            return ret;
        } else {
            final int ret = super.m2(100);
            System.out.println("D.m2(int) executing now with return value = " + ret);
            return ret;
        }
    }


}
