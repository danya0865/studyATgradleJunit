package basicJava;

public class Task7_HasBug {
    public static boolean hasBug(String[] messages) {
        for (String message : messages) {
            if (message.equalsIgnoreCase("Bug")) {
                return true;
            }
        }
        return false;
    }
}
