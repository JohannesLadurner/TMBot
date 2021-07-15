import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Main {

    final static int numOfDistances = 100;
    final static int degreeLeftPoint = 135;
    final static int degreeRightPoint = 45;
    final static boolean filter = true;
    static Robot robot;

    public static void main(String[] args) throws IOException, AWTException, InterruptedException {

        robot = new Robot();
        BufferedImage screenshot;
        //File file = new File("C:\\Users\\TheRubiksCube\\Desktop\\tm.png");
        //BufferedImage image = ImageIO.read(file);
        int fileCounter = 0;
        String filename = ""+ fileCounter;

        //...let the madness begin
        TimeUnit.SECONDS.sleep(5); //Sleep for x seconds
        Toolkit.getDefaultToolkit().beep();
        int counter = 0;
        while(true)
        {

            //if(counter > 0)
            //{
                //drawDistanceAndHeightOnScreenshots(filename);
                filename = ""+ fileCounter;
                fileCounter++;
            //}
            drive();
            counter++;
            System.out.println(counter);
        }
    }

    public static void drive() throws InterruptedException, AWTException, IOException {
        BufferedImage screenshot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        screenshot = resize(screenshot, 0.5);
        int[] distances = WallDetector.getDistances(screenshot, numOfDistances, degreeLeftPoint, degreeRightPoint, filter);
        //System.out.print(Arrays.toString(distances));

        if(distances == null){
            return;
        }
        if(distances[0] != 0 && distances[1] != 0) //When all elements are 0, not fixed yet?
          {
              Inputs nextInput = Brain.decideInput(distances);
              if(nextInput == Inputs.ACCELERATE){
                  //TimeUnit.MILLISECONDS.sleep(150); //Sleep for x seconds
              }
              //System.out.print("-> " + nextInput);
              //KBInput.performInput(nextInput);
          }
        //System.out.println();
    }

    public static void drawDistancesOnScreenShot(String filename) throws AWTException, IOException {
        BufferedImage screenshot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        screenshot = WallDetector.drawDistances(screenshot, numOfDistances, degreeLeftPoint, degreeRightPoint);
        ImageIO.write(screenshot, "png", new File ( "C:\\Users\\TheRubiksCube\\Desktop\\ss\\"+filename+".png") );
    }
    public static void drawDistancesOnSampleImage(BufferedImage image) throws IOException {
        image = WallDetector.drawDistances(image, numOfDistances, degreeLeftPoint, degreeRightPoint);
        ImageIO.write( image, "png", new File ( "C:\\Users\\TheRubiksCube\\Desktop\\tm_OUTPUT_Distance.png") );
    }
    public static void drawHeightsOnScreenshot(String filename) throws AWTException, IOException {
        BufferedImage screenshot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        screenshot = WallDetector.drawWallHeights(screenshot, numOfDistances, degreeLeftPoint, degreeRightPoint);
        ImageIO.write(screenshot, "png", new File ( "C:\\Users\\TheRubiksCube\\Desktop\\ss\\"+filename+".png") );
    }
    public static void drawHeightsOnSampleImage(BufferedImage image) throws IOException {
        image = WallDetector.drawWallHeights(image, numOfDistances, degreeLeftPoint, degreeRightPoint);
        ImageIO.write( image, "png", new File ( "C:\\Users\\TheRubiksCube\\Desktop\\tm_OUTPUT_Height.png") );
    }

    public static void drawDistanceAndHeightOnScreenshots(String filename) throws AWTException, IOException {
        BufferedImage screenshot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        screenshot = resize(screenshot, 0.5);
        screenshot = WallDetector.drawDistances(screenshot, numOfDistances, degreeLeftPoint, degreeRightPoint);
        screenshot = WallDetector.drawWallHeights(screenshot, numOfDistances, degreeLeftPoint, degreeRightPoint);
        ImageIO.write(screenshot, "png", new File ( "C:\\Users\\TheRubiksCube\\Desktop\\ss\\"+filename+".png") );
    }


    public static BufferedImage resize(BufferedImage inputImage, double percent) throws IOException {

        // creates output image
        BufferedImage outputImage = new BufferedImage((int)(inputImage.getWidth()*percent), (int)(inputImage.getHeight()*percent), inputImage.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, (int)(inputImage.getWidth()*percent), (int)(inputImage.getHeight()*percent), null);
        g2d.dispose();

        return outputImage;
    }

}
