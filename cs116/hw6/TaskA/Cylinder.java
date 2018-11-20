

public class Cylinder extends Circle {
    protected float height;

    public Cylinder(float radius, float height) {
        super(radius);
        this.height = height;
    }
    public Cylinder(Circle circle, float height)
        { this(circle.getRadius(), height); }
    public Cylinder() { this(0, 0); }


    public void setHeight(float height) { this.height = height; }
    public float getHeight() { return this.height; }


    public float getVolume() { return super.getArea() * this.height; }
    public String toString() {
        return "Cylinder:"
        + "\n - base: " + super.toString()
        + "\n - height: " + this.getHeight();
    }

};
