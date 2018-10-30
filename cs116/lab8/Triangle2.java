


public class Triangle2 extends GeometricFigure implements SidedObject {
    private double a, b, c;


    // Triangle inequality theorem
    private boolean isTriangle() {
        return a + b > c && a + c > b && b + c > a;

    }
    public Triangle2(double sidea, double sideb, double sidec) {
        //
        this.a = sidea;
        this.b = sideb;
        this.c = sidec;
        if (!this.isTriangle())
            throw new Error("Not a Triangle");

        //
        super.width = c;
        super.height = this.calcHeight();
        super.area = this.area();

    }


    public Triangle2() {
        this(1, 1, 1);
    }


    // access modifiers
    public void setSideA(double side) { this.a = side; }
    public void setSideB(double side) { this.b = side; }
    public void setSideC(double side) { this.c = side; }
    public double getSideA() { return this.a; }
    public double getSideB() { return this.b; }
    public double getSideC() { return this.c; }


    // herons formula
    public double calcHeight() {
        double s = (this.a + this.b + this.c) / 2;
        return Math.sqrt(4 * s * (s - a) * (s - b) * (s - c) / Math.pow(c, 2));
    }
    public double area() {
        double s = (this.a + this.b + this.c) / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }


    public String toString() {
        return super.toString()
            + "\nTriangle:"
            + "\nsides: (" + a + ", " + b + ", " + c + ")\n";
    }

    public void displaySides() { System.out.println("Sides: 3"); }

};
