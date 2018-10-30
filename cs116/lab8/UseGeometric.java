


public class UseGeometric {


    public static void main(String[] args) {
        GeometricFigure[] figs = new GeometricFigure[10];
        figs[0] = new Square(20);
        figs[2] = new Square(6);
        figs[1] = new Triangle(20,23, 20);
        figs[3] = new Triangle(6,6,6);
        figs[4] = new Triangle(4,5,6);
        figs[5] = new Square(12);
        figs[6] = new Square(60);
        figs[7] = new Square(10);
        figs[8] = new Triangle();
        figs[9] = new Square();
        for (GeometricFigure f : figs)
            System.out.println("Area: " + f.area);

    }
};
