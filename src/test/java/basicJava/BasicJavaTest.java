package basicJava;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasicJavaTest {

    private static final Random RANDOM = new Random();
    private static final String[] NAMES = {"Anna", "Boris", "Clara", "Dmitry", "Elena"};

    // ===================== Вывод до и после каждого теста =====================

    @BeforeEach
    void printStart() {
        System.out.println("========================Test method start");
    }

    @AfterEach
    void printEnd() {
        System.out.println("Test method end");
        System.out.println("========================");
    }

    // ===================== Вспомогательные методы =====================

    private static String[] randomWords(int size) {
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = NAMES[RANDOM.nextInt(NAMES.length)];
        }
        return result;
    }

    private static String expectedGrade(int score) {
        if (score <= 20) return "E";
        if (score <= 40) return "D";
        if (score <= 60) return "C";
        if (score <= 80) return "B";
        return "A";
    }

    // ===================== Тег smoke: простые методы =====================

    // Задача 1: isEven, 10 запусков со случайным числом от 1 до 100
    @RepeatedTest(10)
    @Tag("smoke")
    void testIsEven() {
        int n = RANDOM.nextInt(100) + 1;
        boolean expected = (n / 2) * 2 == n;
        boolean actual = Task1_IsEven.isEven(n);
        assertEquals(expected, actual, "isEven(" + n + ")");
    }

    // Задача 2: checkAccess, 20 запусков со случайными числами от 0 до 99
    @RepeatedTest(20)
    @Tag("smoke")
    void testCheckAccess() {
        int age = RANDOM.nextInt(100);
        String expected;
        if (age > 18) {
            expected = "Allowed";
        } else {
            expected = "Denied";
        }
        String actual = Task2_CheckAccess.checkAccess(age);
        assertEquals(expected, actual, "checkAccess(" + age + ")");
    }

    // Задача 3: isPositive
    @RepeatedTest(10)
    @Tag("smoke")
    void testIsPositive() {
        int n = RANDOM.nextInt(101) - 50; // от -50 до 50
        boolean expected = n >= 0;
        boolean actual = Task3_isPositive.isPositive(n);
        assertEquals(expected, actual, "isPositive(" + n + ")");
    }

    // Задача 4: getGrade, 10 случайных чисел от 0 до 100
    @ParameterizedTest
    @MethodSource("randomScores")
    @Tag("smoke")
    void testGetGrade(int score) {
        String expected = expectedGrade(score);
        String actual = Task4_GetGrade.getGrade(score);
        assertEquals(expected, actual, "getGrade(" + score + ")");
    }

    // Задача 6: sumToN
    @RepeatedTest(10)
    @Tag("smoke")
    void testSumToN() {
        int n = RANDOM.nextInt(100) + 1;
        int expected = n * (n + 1) / 2;
        int actual = Task6_SumToN.sumToN(n);
        assertEquals(expected, actual, "sumToN(" + n + ")");
    }

    // Задача 7: hasBug (случайные массивы с "Bug" в разном регистре и без него)
    @RepeatedTest(10)
    @Tag("smoke")
    void testHasBug() {
        String[] withBug = randomWords(5);
        String[] variants = {"Bug", "bug", "BUG"};
        withBug[RANDOM.nextInt(withBug.length)] = variants[RANDOM.nextInt(variants.length)];
        String[] withoutBug = randomWords(5);

        assertEquals(true, Task7_HasBug.hasBug(withBug), "hasBug(" + Arrays.toString(withBug) + ")");
        assertEquals(false, Task7_HasBug.hasBug(withoutBug), "hasBug(" + Arrays.toString(withoutBug) + ")");
    }

    // ===================== Тег regression: строки, массивы, коллекции =====================

    // Задача 5: blastOff
    @RepeatedTest(10)
    @Tag("regression")
    void testBlastOff() {
        int start = RANDOM.nextInt(10) + 1;
        String expected = IntStream.iterate(start, i -> i - 1)
                .limit(start)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" ")) + " Поехали!";
        String actual = Task5_BlastOff.blastOff(start);
        assertEquals(expected, actual, "blastOff(" + start + ")");
    }

    // Задача 8: getEvenInRange
    @ParameterizedTest
    @MethodSource("randomRanges")
    @Tag("regression")
    void testGetEvenInRange(int start, int end) {
        String expected = IntStream.rangeClosed(start, end)
                .filter(i -> i % 2 == 0)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" "));
        String actual = Task8_GetEvenInRange.getEvenInRange(start, end);
        assertEquals(expected, actual, "getEvenInRange(" + start + ", " + end + ")");
    }

    // Задача 9: findMax
    @RepeatedTest(10)
    @Tag("regression")
    void testFindMax() {
        int[] arr = RANDOM.ints(8, -100, 101).toArray();
        int expected = Arrays.stream(arr).max().getAsInt();
        int actual = Task9_FindMax.findMax(arr);
        assertEquals(expected, actual, "findMax(" + Arrays.toString(arr) + ")");
    }

    // Задача 10: reverse
    @RepeatedTest(10)
    @Tag("regression")
    void testReverse() {
        String[] arr = randomWords(5);
        List<String> tmp = new ArrayList<>(Arrays.asList(arr));
        Collections.reverse(tmp);
        String[] expected = tmp.toArray(new String[0]);
        String[] actual = Task10_Reverse.reverse(arr);
        assertArrayEquals(expected, actual, "reverse(" + Arrays.toString(arr) + ")");
    }

    // Задача 11: calcAverage
    @ParameterizedTest
    @MethodSource("randomLists")
    @Tag("regression")
    void testCalcAverage(List<Integer> list) {
        double expected = list.stream().mapToInt(Integer::intValue).average().orElse(0);
        double actual = Task11_CalcAverage.calcAverage(list);
        assertEquals(expected, actual, 0.0001, "calcAverage(" + list + ")");
    }

    // Задача 12: removeSpecificName
    @ParameterizedTest
    @MethodSource("randomNameCases")
    @Tag("regression")
    void testRemoveSpecificName(List<String> list, String name) {
        List<String> expected = list.stream()
                .filter(s -> !s.equals(name))
                .collect(Collectors.toList());
        List<String> actual = Task12_RemoveSpecificName.removeSpecificName(list, name);
        assertEquals(expected, actual, "removeSpecificName(" + list + ", " + name + ")");
    }

    // ===================== Источники случайных данных (по 10 наборов) =====================

    static IntStream randomScores() {
        return RANDOM.ints(10, 0, 101); // 10 случайных чисел от 0 до 100
    }

    static Stream<Arguments> randomRanges() {
        return Stream.generate(() -> {
            int start = RANDOM.nextInt(20) + 1;
            int end = start + RANDOM.nextInt(20);
            return Arguments.of(start, end);
        }).limit(10);
    }

    static Stream<List<Integer>> randomLists() {
        return Stream.generate(() -> {
            List<Integer> list = new ArrayList<>();
            int size = RANDOM.nextInt(8) + 1;
            for (int i = 0; i < size; i++) {
                list.add(RANDOM.nextInt(100));
            }
            return list;
        }).limit(10);
    }

    static Stream<Arguments> randomNameCases() {
        return Stream.generate(() -> {
            List<String> list = new ArrayList<>(Arrays.asList(randomWords(6)));
            String name = NAMES[RANDOM.nextInt(NAMES.length)];
            return Arguments.of(list, name);
        }).limit(10);
    }
}