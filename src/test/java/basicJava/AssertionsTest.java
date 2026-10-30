package basicJava;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssertionsTest {

    // 1. Метод возвращает boolean
    @Test
    @Tag("smoke")
    void isEvenShouldReturnTrueForEvenNumber() {
        int n = 42;
        boolean expected = true;
        boolean actual = Task1_IsEven.isEven(n);
        assertEquals(expected, actual, "isEven(" + n + ")");
    }

    // 2. Метод возвращает список
    @Test
    @Tag("regression")
    void removeSpecificNameShouldReturnListWithoutName() {
        List<String> source = Arrays.asList("Anna", "Boris", "Anna", "Clara");
        List<String> expected = List.of("Boris", "Clara");
        List<String> actual = Task12_RemoveSpecificName.removeSpecificName(source, "Anna");
        assertEquals(expected, actual, "removeSpecificName(" + source + ", Anna)");
    }

    // 3. Метод возвращает строку
    @Test
    @Tag("smoke")
    void getGradeShouldReturnBForScore75() {
        int score = 75;
        String expected = "B";
        String actual = Task4_GetGrade.getGrade(score);
        assertEquals(expected, actual, "getGrade(" + score + ")");
    }

    // 4. Метод возвращает неверное значение: ассерт ДОЛЖЕН упасть.
    // Намеренно "сломанный" метод: цикл идёт до n - 1 вместо n.
    private static int buggySumToN(int n) {
        int sum = 0;
        for (int i = 1; i < n; i++) {
            sum += i;
        }
        return sum;
    }

    @Test
    @Tag("negative")
    void buggySumToNShouldFail() {
        int n = 5;
        int expected = 15;                 // 1+2+3+4+5
        int actual = buggySumToN(n);       // вернёт 10
        assertEquals(expected, actual, "buggySumToN(" + n + ")");
    }
}