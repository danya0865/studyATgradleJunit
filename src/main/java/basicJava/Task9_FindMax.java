package basicJava;

public class Task9_FindMax {

    public static int findMax(int[] arr) {
        int max = arr[0];

        for (int number : arr) {
            if (number > max) {
                max = number;
            }
        }
        return max;
    }
}