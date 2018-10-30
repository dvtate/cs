

// orientation assumed to be:
;;;;;;;;
;;    ;;
;;    ;;
;;;;;;;;

public class Square2 extends GeometricFigure implements SidedObject {
    private double sideLength;

    public Square2(double sideLength) {
        super.height = this.width = this.sideLength = sideLength;
        super.area = this.area();
    }
    public Square2() { this(1); }


    public double getSideLength() { return this.sideLength; }
    public void setSideLength(double len) { this.sideLength = len;}



    public double area(){
        return Math.pow(this.sideLength, 2);
    }


    public String toString() {
        return super.toString()
            + "\nSquare:"
            + "\n+ side length: " + sideLength + "\n";
    }

    public void displaySides() { System.out.println("Sides: 4"); }

};
