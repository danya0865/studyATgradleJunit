package basicJava;

import java.util.List;

public class Task11_CalcAverage {

    public static double calcAverage(List<Integer> list) {
        int sum = 0;

        for (int number : list) {
            sum += number;
        }

        return (double) sum / list.size();
    }
}