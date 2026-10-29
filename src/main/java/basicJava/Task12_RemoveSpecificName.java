package basicJava;

import java.util.ArrayList;
import java.util.List;

public class Task12_RemoveSpecificName {

    public static List<String> removeSpecificName(List<String> list, String nameToRemove) {
        List<String> result = new ArrayList<>();

        for (String name : list) {
            if (!name.equals(nameToRemove)) {
                result.add(name);
            }
        }

        return result;
    }
}