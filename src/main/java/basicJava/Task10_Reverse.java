package basicJava;

import java.util.Arrays;

public class Task10_Reverse {

    public static String[] reverse(String[] arr) {
        String[] result = new String[arr.length];

        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[arr.length - 1 - i];
        }

        return result;
    }
}