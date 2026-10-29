package basicJava;

public class Task8_GetEvenInRange {

    public static String getEvenInRange(int start, int end) {
        String result = "";

        for (int i = start; i <= end; i++) {
            if (i % 2 == 0) {
                if (!result.isEmpty()) {
                    result += " ";
                }
                result += i;
            }
        }
        return result;
    }
}