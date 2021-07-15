import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

public class KBInput {

    private static Robot rob;

    static {
        try {
            rob = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void performInput(Inputs input)
    {
        //Release all keys
        rob.keyRelease(KeyEvent.VK_UP);
        rob.keyRelease(KeyEvent.VK_LEFT);
        rob.keyRelease(KeyEvent.VK_RIGHT);

        //Press the keys until this method gets called again
        switch (input)
        {
            case ACCELERATE:
                rob.keyPress(KeyEvent.VK_UP);
                break;
            case ACCELERATE_LEFT:
                rob.keyPress(KeyEvent.VK_UP);
                rob.keyPress(KeyEvent.VK_LEFT);
                break;
            case ACCELERATE_RIGHT:
                rob.keyPress(KeyEvent.VK_UP);
                rob.keyPress(KeyEvent.VK_RIGHT);
                break;
            case LEFT:
                rob.keyPress(KeyEvent.VK_LEFT);
                break;
            case RIGHT:
                rob.keyPress(KeyEvent.VK_RIGHT);
                break;
        }
    }
}
