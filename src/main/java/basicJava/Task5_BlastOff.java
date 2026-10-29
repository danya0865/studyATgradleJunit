package basicJava;

public class Task5_BlastOff {
    public static String blastOff(int start){
        StringBuilder result = new StringBuilder();
        for (int i = start; i > 0; i--) {
            result.append(i).append(" ");
        }
        result.append("Поехали!");
        return result.toString();
    }
}
