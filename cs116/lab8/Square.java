

// orientation assumed to be:
;;;;;;;;
;;    ;;
;;    ;;
;;;;;;;;

public class Square extends GeometricFigure {
    private double sideLength;

    public Square(double sideLength) {
        super.height = this.width = this.sideLength = sideLength;
        super.area = this.area();
    }
    public Square() {
        this(1);
    }


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

};
