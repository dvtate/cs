


public class UseGeometric2 {


    private static void printSides(SidedObject o) {
        o.displaySides();
    }

    public static void main(String[] args) {
        GeometricFigure[] figs = new GeometricFigure[10];
        figs[0] = new Square2(20);
        figs[2] = new Square2(6);
        figs[1] = new Triangle2(20,23, 20);
        figs[3] = new Triangle2(6,6,6);
        figs[4] = new Triangle2(4,5,6);
        figs[5] = new Square2(12);
        figs[6] = new Square2(60);
        figs[7] = new Triangle2(2,3,2);
        figs[8] = new Triangle2();
        figs[9] = new Square2();

        for (GeometricFigure f : figs) {
            System.out.println("Area: " + f.area);
            printSides((SidedObject) f);
        }
    }
};
