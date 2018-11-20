

public abstract class CircleVolume {
    // member variable
    protected float radius;

    // constructor
    public CircleVolume(float radius) {
        this.radius = radius;
    }
    public CircleVolume(CircleVolume cv) {
        this.radius = cv.getRadius();
    }
    public CircleVolume(){
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

    public abstract float getVolume();
};
