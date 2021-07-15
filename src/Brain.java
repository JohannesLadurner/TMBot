public class Brain {

    public static Inputs decideInput(int[] distances) {
        //Get index of the smallest distance
        int largest = 0;
        int largestIndex = 0;
        int smallest = 0;
        int smallestIndex = 0;

        //Get start value for the smallest element
        for (int i = 0; i < distances.length; i++) {
            if (distances[i] > -1) {
                smallest = distances[i];
                break;
            }
        }
        //Search for the largest distance index + smallest distance
        for (int i = 0; i < distances.length; i++) {
            if (distances[i] > largest) {
                largest = distances[i];
                largestIndex = i;
            }
            if (distances[i] > 0 && distances[i] < smallest) {
                smallest = distances[i];
                smallestIndex = i;
            }
        }
        /*
        The input array looks like this:
        ||| 100 250 240 240 | 150 100 50 -1 -1|||
        In this case, 250 is the largest, which means the car has to go to the left
         */

        int middle = (distances.length / 2);
        double tolerance = distances.length * 0.2; //x%
        int tooClose = 150;

        if (largestIndex >= middle - tolerance && largestIndex <= middle + tolerance)  //Exactly in the middle, just accelerate the car
        {
            return Inputs.ACCELERATE;
        }
        // else //Car is away from the wall, so it can accelerate safetly

        if (largestIndex <= middle) //car is far away from the wall and must turn left:
        {
            return Inputs.ACCELERATE_LEFT;
        } else  //car is far away from the wall and must turn right:
        {
            return Inputs.ACCELERATE_RIGHT;
        }
        //return Inputs.ACCELERATE;
    }
}
