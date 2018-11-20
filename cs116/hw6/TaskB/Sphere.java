

public class Sphere extends Circle {

    public Sphere(float radius) {
        super(radius);
    }

    public Sphere() {
        this(0.0f);
    }

    public Sphere(Circle crossSection) {
        this(crossSection.getRadius());
    }

    public float getVolume() {
        return (float)Math.PI * (float) Math.pow(super.getRadius(), 3) * 4 / 3;
    }

    public String toString() {
        return "Sphere with radius " + super.getRadius();
    }

    

};
