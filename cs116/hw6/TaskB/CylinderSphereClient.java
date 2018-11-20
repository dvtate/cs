

public class CylinderSphereClient {

    public static void main(String[] shouldbevoid) {
        Cylinder c = new Cylinder(28, 35);
        System.out.println(c);
        Sphere s = new Sphere(6);
        System.out.println(s);

        System.out.println("c.vol / s.vol = " + (c.getVolume() / s.getVolume()));

        Circle circle = new Circle(10);
        System.out.println(circle);

        Cylinder cyl = new Cylinder(circle, 5);
        System.out.println(cyl);
        Sphere sph = new Sphere(circle);
        System.out.println(sph);

        Circle[] arr = new Circle[3];
        arr[0] = new Circle(5);
        arr[2] = new Cylinder(12, 50);
        arr[1] = new Sphere(13);

        for (Circle e : arr)
            System.out.println(e);

    }
};
