import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] collinearSegments = new LineSegment[1];
    private int numSegments = 0;
    
    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        // Handle null value passed to constructor 
        if (points == null) {
            throw new IllegalArgumentException("BruteCollinearPoints constructor cannot be null.");
        }

        int n = points.length;
        Point[] pointsSort = new Point[n];

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
            // Set up duplicate points array for use in sorting algorithm (so order of original array can be preserved)
            pointsSort[i] = points[i];
        }  

        // Improved method to find collinear points by sorting slopes
        for (int i = 0; i < n; i++) {
            // Sort the array of "q" points by the slope they make with the "p" point
            Arrays.sort(pointsSort, 0, n, points[i].slopeOrder());

            // Check for 3 points with matching slope (start at 1 since the referenced point will be the first sorted term - NEGATIVE_INFINITY)
            for (int j = 1; j < n - 2; j++) {
                if (points[i].slopeTo(pointsSort[j]) == points[i].slopeTo(pointsSort[j+1]) && points[i].slopeTo(pointsSort[j+1]) == points[i].slopeTo(pointsSort[j+2])) {
                    // Sort points
                    Point[] sortedPoints = {points[i], pointsSort[j], pointsSort[j+1], pointsSort[j+2]};
                    Arrays.sort(sortedPoints, 0, 4);

                    // Check to see if line segment is already represented
                    LineSegment proposedSegment = new LineSegment(sortedPoints[0], sortedPoints[3]);
                    boolean found = false;
                    for (int ii = 0; ii < numSegments; ii++) {
                        if (proposedSegment.toString().equals(collinearSegments[ii].toString())) {
                            found = true;
                            break;
                        } 
                    }

                    // Create the collinear line segment
                    if (found == false) {
                        collinearSegments[numSegments] = new LineSegment(sortedPoints[0], sortedPoints[3]);
                        numSegments++;
                    }
                   
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
