import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;

public class KdTree {
    
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
    }   

    private Node root;
    private int size = 0;
    private Stack<Point2D> points = new Stack<Point2D>();
    private Point2D low;
    private double min = 1;

    // construct an empty set of points
    public KdTree() {
        this.root = new Node();
    }

    // is the set empty?
    public boolean isEmpty() {
            return (root.p == null);
    }   

    // number of points in the set                   
    public int size() {
        return size;
    } 

    // add the point to the set (if it is not already in the set)   
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        if (root.p == null) {
            root.p = p;
            root.rect = new RectHV(0, 0, 1, 1);
            size++;
        }

        else {
            if (insert(root, p, 'X')) size++;
        }
    }   

    private boolean insert(Node pointer, Point2D p, char orientation) {
        if (p.x() != pointer.p.x() || p.y() != pointer.p.y()) {
            // X coordinate delineated (Vertical / Red Division)
            if (orientation == 'X') {
                // Left subtree
                if (p.x() < pointer.p.x()) {
                    if (pointer.lb == null) createNode(p, pointer, "X-L");
                    else return insert(pointer.lb, p, 'Y');
                    return true;
                }

                // Right subtree
                else {
                    if (pointer.rt == null) createNode(p, pointer, "X-R");
                    else return insert(pointer.rt, p, 'Y');
                    return true;
                }
            }

            // Y coordinate delineated (Horizontal / Blue Division)
            else {
                // Left Subtree
                if (p.y() < pointer.p.y()) {
                    if (pointer.lb == null) createNode(p, pointer, "Y-L");
                    else return insert(pointer.lb, p, 'X');
                    return true;
                }

                // Right subtree
                else {
                    if (pointer.rt == null) createNode(p, pointer, "Y-R");
                    else return insert(pointer.rt, p, 'X');
                    return true;
                }
            }
        }
        else {
            return false;
        }
    }

    private void createNode(Point2D p, Node parent, String tree) {
        Node n = new Node();
        // Assign the point to the new node
        n.p = p;
        // Assign the bounding rectangle to the node
        if (tree.equals("X-L")) n.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.p.x(), parent.rect.ymax());
        else if (tree.equals("X-R")) n.rect = new RectHV(parent.p.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
        else if (tree.equals("Y-L")) n.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.p.y());
        else n.rect = new RectHV(parent.rect.xmin(), parent.p.y(), parent.rect.xmax(), parent.rect.ymax());
        
        // Assign the new node to be the left or right child of the parent
        if (tree.equals("X-L") || tree.equals("Y-L")) parent.lb = n;
        else parent.rt = n;
    }

    // does the set contain point p? 
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (!isEmpty()) {
            return contains(root, p, 'X');
        }

        else {
            return false;
        }
    }       

    private boolean contains(Node pointer, Point2D p, char orientation) {
        // X coordinate delineated (Vertical / Red Division)
        if (orientation == 'X') {
            // Left Subtree
            if (p.x() < pointer.p.x()) {
                if (pointer.lb == null) return false;
                else return contains(pointer.lb, p, 'Y');
            }

            // Right subtree
            else {
                // Check for possible match in the right subtree (since > or =)
                if (check(p, pointer.p)) return true;

                // End of subtree
                if (pointer.rt == null) return false;
                else return contains(pointer.rt, p, 'Y');
            }
        }

        // Y coordinate delineated (Horizontal / Blue Division)
        else {
            // Left Subtree
            if (p.y() < pointer.p.y()) {
                if (pointer.lb == null) return false;
                else return contains(pointer.lb, p, 'X');
            }

            // Right subtree
            else {
                // Check for possible match in the right subtree (since > or =)
                if (check(p, pointer.p)) return true;

                // End of subtree
                if (pointer.rt == null) return false;
                else return contains(pointer.rt, p, 'X');
            }
        }
    }

    // Checks whether the point is a match
    private boolean check(Point2D a, Point2D b) {
        return (a.x() == b.x() && a.y() == b.y());
    }
    
    // draw all points to standard draw
    public void draw() {
        draw(root, 'X', 'X', -1);
    }

    // TODO Bug Fix: parting line on 3rd or 4th level of tree not bound by the boxes above it's parent
    private void draw(Node n, char orientation, char tree, double coord) {
        StdDraw.pause(1000);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(n.p.x(), n.p.y());
        
        if (orientation == 'X') {
            double x = n.p.x();
            double y0, y1;
        
            // Set the endpoint of the dividing line based upon the previous node 
            if (tree == 'L') {
                y0 = 0;
                y1 = coord;
            }

            else if (tree == 'R') {
                y0 = coord;
                y1 = 1;
            }

            else {
                y0 = 0;
                y1 = 1;
            }
            
            // Draws the parting line (red - vertical)
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.002);
            StdDraw.line(x, y0, x, y1);
            
            if (n.lb != null) draw(n.lb, 'Y', 'L', n.p.x());
            if (n.rt != null) draw(n.rt, 'Y', 'R', n.p.x());
        }

        if (orientation == 'Y') {
            double y = n.p.y();
            double x0, x1;

            // Set the endpoint of the dividing line based upon the previous node
            if (tree == 'L') {
                x0 = 0;
                x1 = coord;
            }

            else if (tree == 'R') {
                x0 = coord;
                x1 = 1;
            }

            else {
                x0 = 0;
                x1 = 1;
            }

            // Draws the parting line (blue - horizontal)        
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.002);
            StdDraw.line(x0, y, x1, y);
            
            if (n.lb != null) draw(n.lb, 'X', 'L', n.p.y());
            if (n.rt != null) draw(n.rt, 'X', 'R', n.p.y());
        }

    }

    // all points that are inside the rectangle (or on the boundary)                          
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        if (!isEmpty()) {
            if (rect.contains(root.p)) {
                points.push(root.p);
            }
            return range(rect, root);
        }
        else return null;
    }   

    private Iterable<Point2D> range(RectHV rect, Node n) {
        if (n.lb != null) {
            if (n.lb.rect.intersects(rect)) {
                // Recursively search left side
                range(rect, n.lb);
                if (rect.contains(n.lb.p)) {
                    points.push(n.lb.p);
                }
            }
        }

        if (n.rt != null) {
            if (n.rt.rect.intersects(rect)) {
                // Recursively search right side
                range(rect, n.rt);
                if (rect.contains(n.rt.p)) {
                    points.push(n.rt.p);
                }
            }
        }
        return points;
    }
         
    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        if (!isEmpty()) {
            low = root.p;
            nearest(p, root, 'X');
            return low;
        }

        else return null;
    }

    // TODO nearest() function not working correctly
    private void nearest(Point2D p, Node n, char orientation) {
        boolean left = false; 
        boolean right = false;

        // Check to see if the current point being examined is the closest point thus far
        if (n.p.distanceSquaredTo(p) < min) {
            min = n.p.distanceSquaredTo(p);
            low = n.p;
        }
        // Check to see if the left subtree could potentially contain a closer point
        if (n.lb != null) {
            if (n.lb.rect.distanceSquaredTo(p) < n.p.distanceSquaredTo(p)) {
                left = true;
            }
        }

        // Check to see if the left subtree could potentially contain a closer point
        if (n.rt != null) {
            if (n.rt.rect.distanceSquaredTo(p) < n.p.distanceSquaredTo(p)) {
                right = true;
            }
        }

        // Determine which tree to start with if both offer potential closest points
        if (left && right) {
            if (orientation == 'X') {
                if (p.x() < n.p.x()) {
                    nearest(p, n.lb, 'Y');
                    nearest(p, n.rt, 'Y');
                }

                else {
                    nearest(p, n.rt, 'Y');
                    nearest(p, n.lb, 'Y');
                }
            }

            else {
                if (p.y() < n.p.y()) {
                    nearest(p, n.lb, 'X');
                    nearest(p, n.rt, 'X');
                }
                else {
                    nearest(p, n.rt, 'X');
                    nearest(p, n.lb, 'X');
                }
            }
        }

        else {
            if (left) {
                if (orientation == 'X') nearest(p, n.lb, 'Y');
                if (orientation == 'Y') nearest(p, n.lb, 'X');
            }

            if (right) {
                if (orientation == 'X') nearest(p, n.rt, 'Y');
                if (orientation == 'Y') nearest(p, n.rt, 'X');
            }
        }
    }
              
    // unit testing of the methods (optional) 
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        KdTree testKD = new KdTree();
        
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            testKD.insert(p);
        }

        Point2D p = new Point2D(0.407, 0.86);
        RectHV r = new RectHV(.2, 0, .8, .2);

        System.out.println(testKD.contains(p));
        System.out.println(testKD.nearest(p));
        System.out.println(testKD.range(r));

        testKD.draw();
    }                  
}