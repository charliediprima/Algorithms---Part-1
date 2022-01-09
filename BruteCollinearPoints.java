import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.Arrays;

public class BruteCollinearPoints {
    
    private LineSegment[] collinearSegments = new LineSegment[1];
    private int numSegments = 0;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        // Handle null value passed to constructor 
        if (points == null) {
            throw new IllegalArgumentException("BruteCollinearPoints constructor cannot be null.");
        }
        
        int n = points.length;
        
        // Handle null and duplicate points to constructor
        for (int i = 0; i < n; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Points cannot be null.");
            }
            for (int j = i + 1; j < n; j++) {
                if (points[i].slopeTo(points[j]) == Double.NEGATIVE_INFINITY) {
                    throw new IllegalArgumentException("No duplicate points.");
                }
            }
        }     

        // Brute force method to find collinear points
        for (int i = 0; i < n - 3; i++) {
            for (int j = i + 1; j < n - 2; j++) {
                for (int k = j + 1; k < n - 1; k++) {
                    for (int m = k + 1; m < n; m++) {
                        // Measure the slopes between points i-j, i-k, and i-m (referred to as a, b, c)
                        double a = points[i].slopeTo(points[j]);
                        double b = points[i].slopeTo(points[k]);
                        double c = points[i].slopeTo(points[m]);
                        
                        // Check to see if the slopes match
                        if (a == b && b == c) {
                            // Sort points prior to creating line segment
                            Point[] sortedPoints = {points[i], points[j], points[k], points[m]};
                            Arrays.sort(sortedPoints, 0, 4);

                            // Create collinear line segment
                            collinearSegments[numSegments] = new LineSegment(sortedPoints[0], sortedPoints[3]);
                            numSegments++;
                            
                            // Doubles the size of the array that holds all line segments, if needed
                            if (numSegments >= collinearSegments.length * 0.75) {
                                LineSegment[] temp = new LineSegment[numSegments];
                                for (int ii = 0; ii < numSegments; ii++) {
                                    temp[ii] = collinearSegments[ii]; 
                                }
                                
                                collinearSegments = new LineSegment[collinearSegments.length * 2];

                                for (int ii = 0; ii < numSegments; ii++) {
                                    collinearSegments[ii] = temp[ii];
                                }
                            }
                        }
                    }
                }
            }
        }
    } 
    
    // the number of line segments
    public int numberOfSegments() {
        return numSegments;
    }                
    
    // the line segments
    public LineSegment[] segments() {               
        // Copy the line segments into a new array to avoid returning null array values
        LineSegment[] finalSegments = new LineSegment[numSegments];

        for (int i = 0; i < numSegments; i++) {
            finalSegments[i] = collinearSegments[i];
        }
        return finalSegments;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
    
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
    
        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}