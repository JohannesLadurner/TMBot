import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;

public class WallDetector {

    private static final int timeGapBottom = 60;

    /**
     * In this method, a list with all distances gets created based on the input.
     * Example:
     * leftPoint   rightPoint
     * o        o
     * \      /
     * \   /
     * \/
     * The number of lines (distances) get determined between those two points
     *
     * @param image            frame of the game
     * @param numOfDistances   number of distances between the points
     * @param degreePointLeft  Left point
     * @param degreePointRight Right point
     * @return the list with the distances, correctly from left to right
     */
    public static int[] getDistances(BufferedImage image, int numOfDistances, int degreePointLeft, int degreePointRight, boolean filter) {
        //Handle exceptions
        if (image == null)
            throw new IllegalArgumentException("Image is null!");
        if (numOfDistances < 0)
            throw new IllegalArgumentException("Number of lines < 0!");
        if (degreePointRight >= degreePointLeft)
            throw new IllegalArgumentException("Left degree Point has to be > than the right one!");

        int[] distances = new int[numOfDistances];
        double step = (double)(degreePointLeft - degreePointRight) / (numOfDistances - 1); //Distance between the two points, divided by how many distances we need
        double degree = degreePointRight; //Start on the right point

        /*
        Go through the reverse order, so that the distances go from left to right
        Else it will look like: first element comes in -> [240][][]...[]
        Instead it should look like: first element comes in -> [][][]...[240]
         */
        Point p;
        for (int i = numOfDistances - 1; i >= 0; i--) {
            p = getFirstDarkPixelFromWall(image, degree); //Call the method with the current degrees
            if (p != null) {
                distances[i] = (int) Math.sqrt(Math.abs(Math.pow((image.getWidth() / 2 - p.x), 2) + Math.pow((image.getHeight() - timeGapBottom - p.y), 2)));
            } else {
                distances[i] = -1;
            }
            degree += step; //Go to next degree
        }

        if (filter) {
            distances = filterDistances(distances);
        }

        return distances;
    }

    /**
     * Very similar to getDistances, but this time the wall heights get calculated.
     * Go through the degrees, and get the corresponding height for the angle.
     * <p>
     * | <= Height 2
     * |      |   < = Height 1
     * \     /
     * \   /
     * \/
     *
     * @param image            frame of the game
     * @param numOfDistances   number of straight distance lines
     * @param degreePointLeft  left view angle point
     * @param degreePointRight right view angle point
     * @return list of all heights
     */
    public static int[] getWallHeights(BufferedImage image, int numOfDistances, int degreePointLeft, int degreePointRight) {
        //Handle exceptions
        if (image == null)
            throw new IllegalArgumentException("Image is null!");
        if (numOfDistances < 0)
            throw new IllegalArgumentException("Number of lines < 0!");
        if (degreePointRight >= degreePointLeft)
            throw new IllegalArgumentException("Left degree Point has to be > than the right one!");

        int[] heights = new int[numOfDistances];
        int step = (degreePointLeft - degreePointRight) / (numOfDistances - 1); //Distance between the two points, divided by how many distances we need
        int degree = degreePointRight; //Start on the right point
        /*
        Go through the reverse order, so that the distances go from left to right
        Else it will look like: first element comes in -> [240][][]...[]
        Instead it should look like: first element comes in -> [][][]...[240]
         */

        for (int i = numOfDistances - 1; i >= 0; i--) {
            heights[i] = getWallHeight(image, degree);
            degree += step; //Go to next degree
        }
        return heights;
    }

    /**
     * this method draws the distances on the image from the parameter in form of lines, starting from bottom middle
     * the lines get drawn inbetween the parameters points
     *
     * @param image            frame of the game
     * @param numOfDistances   number of lines
     * @param degreePointLeft  left point of the view
     * @param degreePointRight right point of the view
     * @return same image with distance lines on it, if there are none then the identical one gets returned
     */
    public static BufferedImage drawDistances(BufferedImage image, int numOfDistances, int degreePointLeft, int degreePointRight) {
        //Handle exceptions
        if (image == null)
            throw new IllegalArgumentException("Image is null!");
        if (numOfDistances < 0)
            throw new IllegalArgumentException("Number of lines < 0!");
        if (degreePointRight >= degreePointLeft)
            throw new IllegalArgumentException("Left degree Point has to be > than the right one!");

        Graphics2D g2d = image.createGraphics();
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(Color.orange);

        double step = (double)(degreePointLeft - degreePointRight) / (numOfDistances - 1); //Distance between the two points, divided by how many distances we need
        double degree = degreePointRight; //Start on the right point

        Point p;
        for (int i = 0; i < numOfDistances; i++) {
            p = getFirstDarkPixelFromWall(image, degree); //Call the method with the current degrees
            if (p != null) //Draw only when there is a point
            {
                g2d.drawLine(image.getWidth() / 2, image.getHeight() - timeGapBottom, p.x, p.y);
            }
            degree += step; //Go to next degree
        }
        g2d.dispose();
        return image;
    }

    /**
     * Draws the heights of the walls on the frame.
     * If there is a line missing, no black pixel got found!
     *
     * @param image            frame from game
     * @param numOfDistances   number of distance lines
     * @param degreePointLeft  left view point angle
     * @param degreePointRight right view point angle
     * @return image that got drawn on
     */
    public static BufferedImage drawWallHeights(BufferedImage image, int numOfDistances, int degreePointLeft, int degreePointRight) {
        //Handle exceptions
        if (image == null)
            throw new IllegalArgumentException("Image is null!");
        if (numOfDistances < 0)
            throw new IllegalArgumentException("Number of lines < 0!");
        if (degreePointRight >= degreePointLeft)
            throw new IllegalArgumentException("Left degree Point has to be > than the right one!");

        int step = (degreePointLeft - degreePointRight) / (numOfDistances - 1); //Distance between the two points, divided by how many distances we need
        int degree = degreePointRight; //Start on the right point

        Graphics2D g2d = image.createGraphics();
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(Color.red);

        Point start;
        for (int i = numOfDistances - 1; i >= 0; i--) {
            start = getFirstDarkPixelFromWall(image, degree);
            if (start != null) //Draw only when there is a start point
            {
                g2d.drawLine(start.x, start.y, start.x, start.y - getWallHeight(image, degree)); //Draw from start point to the height
            }
            degree += step; //Go to next degree
        }
        g2d.dispose();
        return image;
    }

    /**
     * Filter values, so something like this cannot happen:
     * <p>
     * \  \
     * \  \
     * \ \\
     * \ \\
     * <p>
     * Use only when you have lots of lines!
     *
     * @param distances
     * @return
     */
    private static int[] filterDistances(int[] distances) {
        double tolerance = 1.6; //160%
        int minimumDistance = 15;
        double averageDistance = 0;
        int validDistancesForAverage = 0;
        for(int i = 0; i < distances.length; i++){ //Find average distance from all distances > minimumDistance
            if(distances[i] >= minimumDistance){
                averageDistance += distances[i];
                validDistancesForAverage++;
            }
        }
        if(validDistancesForAverage > 0){
            averageDistance = averageDistance / validDistancesForAverage;
        }
        else{
            return null;
        }

        for (int i = 0; i < distances.length - 1; i++) { //Filter out invalid distances

            if (distances[i] >= minimumDistance) //Distance has to be > 10, else the small middle line got found
            {
                if(distances[i] > averageDistance * tolerance && distances[i] < averageDistance / tolerance){ //A lot larger/smaller than average!
                    distances[i] = -1;
                }
            } else {
                distances[i] = -1; //distance < 10 is not possible
            }

        }
        return distances;
    }

    /**
     * This method "draws" a line (starting fromm bottom middle) with a angle (see parameter) to the x axis
     * and determines the first dark pixel it finds on it, for example:
     * <p>
     * \
     * \
     * \     <- this line and all the one above get ignored
     * x <- the first dark pixel starting from the bottom, this one gets returned
     * \
     * o <- player
     *
     * @param image  the frame of the game
     * @param degree the angle
     * @return the point with x/y coordinates. Return null if there is no dark pixel on this line
     */
    private static Point getFirstDarkPixelFromWall(BufferedImage image, double degree) {
        //Error handling
        if (degree < 0 || degree > 180)
            throw new IllegalArgumentException("Degree has an invalid input: " + degree);
        if (image == null)
            throw new IllegalArgumentException("Image is null!");

        //Start on bottom in the middle
        Point origin = new Point(image.getWidth() / 2, image.getHeight() - 60);
        double ankathete = image.getHeight() * 0.75;
        double hypotenuse = ankathete / Math.sin(Math.toRadians(degree));
        double gegenkathete = Math.sqrt(Math.pow(hypotenuse, 2) - Math.pow(ankathete, 2));
        Point resultPoint;

        if (degree <= 90) {
            resultPoint = new Point(origin.x + (int) gegenkathete, origin.y - (int) ankathete);
        } else {
            resultPoint = new Point(origin.x - (int) gegenkathete, origin.y - (int) ankathete);
        }

        Line line = new Line(origin, resultPoint);
        Point[] pointsOnLine = line.getPoints((int) ankathete); //Create as much points as the height of the triangle is


        for (int i = 0; i < pointsOnLine.length; i++) {

            try {
                //In bounds
                if(pointsOnLine[i].x < image.getWidth() && pointsOnLine[i].x > 0 && pointsOnLine[i].y < image.getHeight() && pointsOnLine[i].y > 0){
                    if (isDarkPixel(new Color(image.getRGB(pointsOnLine[i].x, pointsOnLine[i].y)))) {
                        return pointsOnLine[i];
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    /**
     * This method calculates the height of a wall, starting from the first dark pixel on a wall
     * and moving y straight upwards while keeping x at the same position.
     * <p>
     * |   < Height = 2
     * |
     * \    < Start Point
     * \
     * \
     *
     * @param image  the frame of the game
     * @param degree degree of the distance line
     * @return the height in pixels, -1 in case of inf. height
     */
    private static int getWallHeight(BufferedImage image, int degree) {
        Point startPoint = getFirstDarkPixelFromWall(image, degree);
        if (startPoint == null) {
            return -1;
        }

        int currentY = startPoint.y;
        do {
            currentY--; //Move upwards in the image
            if (currentY < 0) //Return error
            {
                return -1;
            }
        } while (isDarkPixel(new Color(image.getRGB(startPoint.x, currentY)))); //Go through the loop until pixel is no longer dark

        return startPoint.y - currentY; //Return the difference between origin and current y -> Height
    }

    /**
     * @param color transform color to brightness, then check if it is dark or bright.
     * @return
     */
    private static boolean isDarkPixel(Color color) {
        if (color == null)
            throw new IllegalArgumentException("Input color is null!");

        double grayfactor = 0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue(); //Transform Color to Black/White
        if (grayfactor < (255 * 0.2)) //maximum value of facor above is 255. If it is under x value, return true, else false
        {
            return true;
        }
        return false;
    }
}
