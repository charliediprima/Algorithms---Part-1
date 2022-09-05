import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

public class PointSET {
    
    private SET<Point2D> pSet;

    // construct an empty set of points
    public PointSET() {
        pSet = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pSet.isEmpty();
    }   

    // number of points in the set                   
    public int size() {
        return pSet.size();
    } 

    // add the point to the set (if it is not already in the set)   
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        pSet.add(p);
    }   

    // does the set contain point p? 
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        
        return pSet.contains(p);
    }       
    
    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        
        for (Point2D p : pSet) {
            StdDraw.point(p.x(), p.y());
        }
    }

    // all points that are inside the rectangle (or on the boundary)                          
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        Stack<Point2D> points = new Stack<Point2D>();
        
        for (Point2D p : pSet) {
            if ((p.x() <= rect.xmax() && p.x() >= rect.xmin()) && (p.y() <= rect.ymax() && p.y() >= rect.ymin())) {
                points.push(p);
            }
        }

        return points;
    }       
         
    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        
        double dist, minDist = 2; // Hardcoded minDist to a value greater than max possible
        Point2D closestPoint = null;

        for (Point2D p2 : pSet) {
            dist = p.distanceSquaredTo(p2);
            if (dist < minDist) {
                minDist = dist;
                closestPoint = new Point2D(p2.x(), p2.y());
            }
        }

        return closestPoint;
    } 
              
    // unit testing of the methods (optional) 
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        PointSET testpSet = new PointSET();
        
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            testpSet.insert(p);
        }

        Point2D p = new Point2D(0.4, 0.1);
        RectHV r = new RectHV(0.2, 0, 0.8, 0.2);

        testpSet.draw();
        System.out.println(testpSet.nearest(p));
        System.out.println(testpSet.range(r));

    }                  
}
