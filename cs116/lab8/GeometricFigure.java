


public abstract class GeometricFigure {
    protected double height, width, area;

    public GeometricFigure(double height, double width, double area) {
        this.height = height;
        this.width = width;
        this.area = area;
    }

    public GeometricFigure() {
        this(0,0,0);
    }

    abstract double area();

    public String toString() {
        return super.toString()
            + "\nFigure:"
            + "+ height: " + height
            + "+ width: " + width
            + "+ area: " + area;
    }
};
