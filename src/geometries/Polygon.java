package geometries;

import java.util.LinkedList;
import java.util.List;
import primitives.*;
import static primitives.Util.*;

/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 *
 * @author Dan
 */
public class Polygon extends Geometry {
    /**
     * List of polygon's vertices
     */
    protected List<Point3D> _vertices;
    /**
     * Associated plane in which the polygon lays
     */
    protected Plane _plane;

    /**
     * Polygon constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     * Receives also a color which is the polygon's color
     * and a material which is the material that the polygon is made from
     *
     * @param _emission a color of the plane
     * @param _material the material the polygon is made of
     * @param vertices list of vertices according to their order by edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)
     *                                  <li>The polygon is concave (not convex></li>
     *                                  </ul>
     */
    public Polygon(Color _emission, Material _material, Point3D... vertices) {
        super(_emission, _material);
        if (vertices.length < 3)
            throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
        _vertices = List.of(vertices);
        // Generate the plane according to the first three vertices and associate the
        // polygon with this plane.
        // The plane holds the invariant normal (orthogonal unit) vector to the polygon
        _plane = new Plane(vertices[0], vertices[1], vertices[2]);
        if (vertices.length == 3) return; // no need for more tests for a Triangle

        Vector n = _plane.get_normal();

        // Subtracting any subsequent points will throw an IllegalArgumentException
        // because of Zero Vector if they are in the same point
        Vector edge1 = vertices[vertices.length - 1].subtract(vertices[vertices.length - 2]);
        Vector edge2 = vertices[0].subtract(vertices[vertices.length - 1]);

        // Cross Product of any subsequent edges will throw an IllegalArgumentException
        // because of Zero Vector if they connect three vertices that lay in the same
        // line.
        // Generate the direction of the polygon according to the angle between last and
        // first edge being less than 180 deg. It is hold by the sign of its dot product
        // with
        // the normal. If all the rest consequent edges will generate the same sign -
        // the
        // polygon is convex ("kamur" in Hebrew).
        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
        for (int i = 1; i < vertices.length; ++i) {
            // Test that the point is in the same plane as calculated originally
            if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
            // Test the consequent edges have
            edge1 = edge2;
            edge2 = vertices[i].subtract(vertices[i - 1]);
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
        }
    }

    /**
     * Polygon constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     * Sets polygon's color (_emission) to black
     * Sets the material to (0, 0, 0)
     *
     * @param vertices list of vertices according to their order by edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)
     *                                  <li>The polygon is concave (not convex></li>
     *                                  </ul>
     */
    public Polygon(Point3D... vertices) {
        this(Color.BLACK, vertices);
    }

    /**
     * Polygon constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     * Receives also a color which is the polygon's color
     * Sets the material to (0, 0, 0)
     *
     * @param _emission a color of the plane
     * @param vertices list of vertices according to their order by edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)
     *                                  <li>The polygon is concave (not convex></li>
     *                                  </ul>
     */
    public Polygon(Color _emission, Point3D... vertices) {
        this(Color.BLACK, new Material(0,0, 0), vertices);
    }

    /**
     * Auxiliary function. Finds the minimal or maximal point for the box
     *
     * @param isMin true if we want to get the minimal value, false if maximal
     * @return the min or max point of the box. (Has min or max X coordinate, min or max Y coordinate, min or max Z coordinate)
     */
    private Point3D minOrMaxPoint(boolean isMin) {
        double maxOrMinX = isMin ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        double maxOrMinY = maxOrMinX;
        double maxOrMinZ = maxOrMinX;
        for (Point3D vertex : _vertices) {
            double valueX = vertex.get_x().get();
            double valueY = vertex.get_y().get();
            double valueZ = vertex.get_z().get();
            if (isMin ? valueX < maxOrMinX : valueX > maxOrMinX)
                maxOrMinX = valueX;
            if (isMin ? valueY < maxOrMinY : valueY > maxOrMinY)
                maxOrMinY = valueY;
            if (isMin ? valueZ < maxOrMinZ : valueZ > maxOrMinZ)
                maxOrMinZ = valueZ;
        }
        return new Point3D(maxOrMinX, maxOrMinY, maxOrMinZ);
    }

    /*************** Admin *****************/
    @Override
    public Vector getNormal(Point3D point) {
        return _plane.get_normal();
    }

    @Override
    public List<GeoPoint> findIntersectionsTemp(Ray ray) {
        List<GeoPoint> point = _plane.findIntersections(ray);
        if (point == null)
            return null;
        List<Vector> v = new LinkedList<Vector>();
        for (Point3D thisV : _vertices)
            v.add(thisV.subtract(ray.get_startPoint()));
        List<Vector> N = new LinkedList<Vector>();
        for (int i = 0; i < _vertices.size() - 1; i++)
            N.add(v.get(i).crossProduct(v.get(i + 1)).normalize());
        N.add(v.get(_vertices.size() - 1).crossProduct(v.get(0)).normalize());
        double first = ray.get_direction().dotProduct(N.get(0));
        if (isZero(first))
            return null;
        double sign = first/Math.abs(first);
        for (int i = 1; i < N.size(); i++) {
            if (sign * alignZero(ray.get_direction().dotProduct(N.get(i))) <= 0)
                return null;
        }
        return List.of(new GeoPoint(this, point.get(0).point));
    }

    @Override
    protected BVHBox calcBox() {
        return new BVHBox(minOrMaxPoint(true), minOrMaxPoint(false));
    }
}
