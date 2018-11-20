

public class CylinderSphereClient {

    public static void main(String[] shouldbevoid) {

        Cylinder cyl = new Cylinder(1, 2);
        System.out.println(cyl);

        Sphere sph = new Sphere(5);
        System.out.println(sph);

        CircleVolume cv = cyl;

        System.out.println(cv.getRadius());
        System.out.println(cv.getDiameter());
        System.out.println(cv.getArea());
        System.out.println(cv.getCircumfrence());
        System.out.println(cv.toString());
        System.out.println(cv.getVolume());
        cv.setRadius(12);

        cv = sph;

        System.out.println(cv.getRadius());
        System.out.println(cv.getDiameter());
        System.out.println(cv.getArea());
        System.out.println(cv.getCircumfrence());
        System.out.println(cv.toString());
        System.out.println(cv.getVolume());
        cv.setRadius(12);


        CircleVolume[] arr = new CircleVolume[4];

        arr[0] = new Cylinder(12,13);
        arr[1] = new Cylinder(14,15);
        arr[2] = new Sphere(16);
        arr[3] = new Sphere(17);

        for (CircleVolume e : arr)
            System.out.println(e);

    }
};
