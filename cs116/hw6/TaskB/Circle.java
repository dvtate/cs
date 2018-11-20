

public class Circle {
    // member variable
    protected float radius;

    // constructor
    public Circle(float radius) {
        this.radius = radius;
    }
    public Circle(){
        this(0);
    }

    // accessmodifiers
    public float getRadius() { return this.radius; }
    public void setRadius(float radius) { this.radius = radius; }

    // should be inlined
    public float getDiameter()
        { return this.radius * 2; }
    public float getArea()
        { return (float)Math.pow(this.radius, 2) * (float)Math.PI; }
    public float getCircumfrence()
        { return 2 * (float) Math.PI * this.radius; }

    public String toString() {
        return "circle with radius " + this.radius;
    }

    
};
