
public class CylinderClient {

    public static void main(String[] args) {
        Circle circle1 = new Circle(1.1f);
        System.out.println(circle1);

        Cylinder cylinder1 = new Cylinder(1.5f, 2.0f);
        System.out.println(cylinder1);

        Cylinder cylinder2 = new Cylinder(circle1, 5.0f);
        System.out.println(cylinder2);

        circle1.setRadius(2.2f);
        System.out.println(circle1);


        System.out.println("\n\ncylinder1.getRadius(): " + cylinder1.getRadius());
        System.out.println("cylinder1.getArea(): " + cylinder1.getArea());
        System.out.println("cylinder1.getCircumfrence(): " + cylinder1.getCircumfrence());
        System.out.println("cylinder1.getDiameter(): " + cylinder1.getDiameter());
        System.out.println("((circle)cylinder1).toString(): " + ((Circle)cylinder1).toString());

    }
};
