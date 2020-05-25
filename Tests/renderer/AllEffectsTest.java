package renderer;

import elements.*;
import geometries.*;
import org.junit.Test;
import primitives.*;
import scene.Scene;

public class AllEffectsTest {
    @Test
    public void TestAllEffects() {
        Scene scene = new Scene("Test scene");
        scene.setCamera(new Camera(new Point3D(0, 0, -1000), new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.setDistance(1000);
        scene.setBackground(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(Color.BLACK, 0));

        scene.addGeometries(
                new Sphere(new Color(java.awt.Color.BLUE), new Material(0.4, 0.3, 100, 0.3, 0), 50,
                        new Point3D(0, 0, 60)),
                new Sphere(new Color(java.awt.Color.YELLOW), new Material(0.5, 0.5, 100, 0, 0.8), 25, new Point3D(0, -40, 60)),
                new Triangle(new Color(java.awt.Color.PINK), new Material(0, 0, 0, 0.3, 0.7),
                        new Point3D(-80, -60, 150), new Point3D(60, 40, 20), new Point3D(70, 20, 150))
                );

        scene.addLights(new SpotLight(new Color(1000, 600, 0), new Point3D(-100, 100, -500), new Vector(-1, 1, 2), 1,
                0.0004, 0.0000006));

        ImageWriter imageWriter = new ImageWriter("allEffects", 150, 150, 500, 500);
        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();

        /*scene.addGeometries(
                new Sphere(new Color(100, 0, 100), new Material(0.4, 0.3, 100, 0.3, 0), 473, new Point3D(0, 0, 1000)),
                new Sphere(new Color(181, 227, 244), new Material(0.5, 0.5, 100, 0, 0.4), 266, new Point3D(0, 0, 1000)),
                //new Plane(new Color(255, 185, 104), new Material(0, 0, 0, 0.3, 1), new Point3D(1500, 1500, 1500),
                        //new Point3D(-1500, -1500, 1500), new Point3D(670, -670, -3000)),
                new Triangle(new Color(79, 185, 104), new Material(0, 0, 0, 0.2, 0.1), new Point3D(-1500, 1500, 1500),
                        new Point3D(-1500, -1500, 150), new Point3D(-1500, 1500, 2000)));*/
    }
}
