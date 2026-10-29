package basicJava;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
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

    private static void report(String description, boolean passed) {
        System.out.println(description);
        System.out.println(passed ? "TEST PASSED" : "TEST FAILED");
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

    // ===================== @Test (4 метода) =====================

    // Задача 1: isEven, один запуск со случайным числом от 1 до 100
    @Test
    void testIsEven() {
        int n = RANDOM.nextInt(100) + 1;
        boolean expected = (n / 2) * 2 == n;
        report("isEven(" + n + ")", Task1_IsEven.isEven(n) == expected);
    }

    // Задача 5: blastOff
    @Test
    void testBlastOff() {
        int start = RANDOM.nextInt(10) + 1;
        String expected = IntStream.iterate(start, i -> i - 1)
                .limit(start)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" ")) + " Поехали!";
        String actual = Task5_BlastOff.blastOff(start);
        report("blastOff(" + start + ") = " + actual, expected.equals(actual));
    }

    // Задача 7: hasBug (случайные массивы с "Bug" в разном регистре и без него)
    @Test
    void testHasBug() {
        String[] withBug = randomWords(5);
        String[] variants = {"Bug", "bug", "BUG"};
        withBug[RANDOM.nextInt(withBug.length)] = variants[RANDOM.nextInt(variants.length)];
        String[] withoutBug = randomWords(5);

        boolean passed = Task7_HasBug.hasBug(withBug) && !Task7_HasBug.hasBug(withoutBug);
        report("hasBug(" + Arrays.toString(withBug) + ") и hasBug(" + Arrays.toString(withoutBug) + ")", passed);
    }

    // Задача 10: reverse
    @Test
    void testReverse() {
        String[] arr = randomWords(5);
        List<String> tmp = new ArrayList<>(Arrays.asList(arr));
        Collections.reverse(tmp);
        String[] expected = tmp.toArray(new String[0]);
        String[] actual = Task10_Reverse.reverse(arr);
        report("reverse(" + Arrays.toString(arr) + ") = " + Arrays.toString(actual),
                Arrays.equals(expected, actual));
    }

    // ===================== @RepeatedTest (4 метода) =====================

    // Задача 2: checkAccess, 20 запусков со случайными числами от 0 до 99
    @RepeatedTest(20)
    void testCheckAccess() {
        int age = RANDOM.nextInt(100);
        String expected;
        if (age > 18) {
            expected = "Allowed";
        } else {
            expected = "Denied";
        }
        report("checkAccess(" + age + ")", expected.equals(Task2_CheckAccess.checkAccess(age)));
    }

    // Задача 3: isPositive
    @RepeatedTest(5)
    void testIsPositive() {
        int n = RANDOM.nextInt(101) - 50; // от -50 до 50
        boolean expected = n >= 0;
        report("isPositive(" + n + ")", Task3_isPositive.isPositive(n) == expected);
    }

    // Задача 6: sumToN
    @RepeatedTest(5)
    void testSumToN() {
        int n = RANDOM.nextInt(100) + 1;
        int expected = n * (n + 1) / 2;
        report("sumToN(" + n + ")", Task6_SumToN.sumToN(n) == expected);
    }

    // Задача 9: findMax
    @RepeatedTest(5)
    void testFindMax() {
        int[] arr = RANDOM.ints(8, -100, 101).toArray();
        int expected = Arrays.stream(arr).max().getAsInt();
        report("findMax(" + Arrays.toString(arr) + ")", Task9_FindMax.findMax(arr) == expected);
    }

    // ===================== @ParameterizedTest (4 метода) =====================

    static IntStream randomScores() {
        return RANDOM.ints(10, 0, 101); // 10 случайных чисел от 0 до 100
    }

    static Stream<Arguments> randomRanges() {
        return Stream.generate(() -> {
            int start = RANDOM.nextInt(20) + 1;
            int end = start + RANDOM.nextInt(20);
            return Arguments.of(start, end);
        }).limit(5);
    }

    static Stream<List<Integer>> randomLists() {
        return Stream.generate(() -> {
            List<Integer> list = new ArrayList<>();
            int size = RANDOM.nextInt(8) + 1;
            for (int i = 0; i < size; i++) {
                list.add(RANDOM.nextInt(100));
            }
            return list;
        }).limit(5);
    }

    static Stream<Arguments> randomNameCases() {
        return Stream.generate(() -> {
            List<String> list = new ArrayList<>(Arrays.asList(randomWords(6)));
            String name = NAMES[RANDOM.nextInt(NAMES.length)];
            return Arguments.of(list, name);
        }).limit(5);
    }

    // Задача 4: getGrade, массив случайных чисел от 0 до 100
    @ParameterizedTest
    @MethodSource("randomScores")
    void testGetGrade(int score) {
        String expected = expectedGrade(score);
        String actual = Task4_GetGrade.getGrade(score);
        report("getGrade(" + score + ") = " + actual, expected.equals(actual));
    }

    // Задача 8: getEvenInRange
    @ParameterizedTest
    @MethodSource("randomRanges")
    void testGetEvenInRange(int start, int end) {
        String expected = IntStream.rangeClosed(start, end)
                .filter(i -> i % 2 == 0)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" "));
        String actual = Task8_GetEvenInRange.getEvenInRange(start, end);
        report("getEvenInRange(" + start + ", " + end + ") = \"" + actual + "\"", expected.equals(actual));
    }

    // Задача 11: calcAverage
    @ParameterizedTest
    @MethodSource("randomLists")
    void testCalcAverage(List<Integer> list) {
        double expected = list.stream().mapToInt(Integer::intValue).average().orElse(0);
        double actual = Task11_CalcAverage.calcAverage(list);
        report("calcAverage(" + list + ") = " + actual, Math.abs(expected - actual) < 0.0001);
    }

    // Задача 12: removeSpecificName
    @ParameterizedTest
    @MethodSource("randomNameCases")
    void testRemoveSpecificName(List<String> list, String name) {
        List<String> expected = list.stream()
                .filter(s -> !s.equals(name))
                .collect(Collectors.toList());
        List<String> actual = Task12_RemoveSpecificName.removeSpecificName(list, name);
        report("removeSpecificName(" + list + ", " + name + ") = " + actual, expected.equals(actual));
    }
}