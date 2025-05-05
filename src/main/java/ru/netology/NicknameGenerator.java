package ru.netology;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class NicknameGenerator {

    // Генератор случайных текстов
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    // Критерии красивого слова
    public static boolean isPalindrome(String word) {
        return new StringBuilder(word).reverse().toString().equals(word);
    }

    public static boolean allSameLetters(String word) {
        char firstLetter = word.charAt(0);
        for (char ch : word.toCharArray()) {
            if (ch != firstLetter) return false;
        }
        return true;
    }

    public static boolean sortedCharacters(String word) {
        char prevCh = '\u0000'; // Нулевой символ ASCII
        for (char currentCh : word.toCharArray()) {
            if (currentCh < prevCh) return false;
            prevCh = currentCh;
        }
        return true;
    }

    // Атрибуты для хранения счётчиков
    public static final AtomicInteger counterLength3 = new AtomicInteger(0);
    public static final AtomicInteger counterLength4 = new AtomicInteger(0);
    public static final AtomicInteger counterLength5 = new AtomicInteger(0);

    // Главный метод
    public static void main(String[] args) {
        // Генерация 100 тыс. слов разной длины
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3)); // Длина от 3 до 5 символов
        }

        // Три потока, по одному на каждую длину
        Thread threadForLen3 = new Thread(() -> checkWordsOfGivenLength(texts, 3));
        Thread threadForLen4 = new Thread(() -> checkWordsOfGivenLength(texts, 4));
        Thread threadForLen5 = new Thread(() -> checkWordsOfGivenLength(texts, 5));

        // Запуск потоков
        threadForLen3.start();
        threadForLen4.start();
        threadForLen5.start();

        // Дожидаемся завершения всех потоков
        try {
            threadForLen3.join();
            threadForLen4.join();
            threadForLen5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Выводим итоги
        System.out.println("Красивых слов с длиной 3: " + counterLength3.get() + " шт");
        System.out.println("Красивых слов с длиной 4: " + counterLength4.get() + " шт");
        System.out.println("Красивых слов с длиной 5: " + counterLength5.get() + " шт");
    }

    // Вспомогательная функция для проверки слов заданной длины
    public static void checkWordsOfGivenLength(String[] words, int targetLength) {
        for (String word : words) {
            if (word.length() == targetLength &&
                    (isPalindrome(word) || allSameLetters(word) || sortedCharacters(word))) {
                incrementCounter(targetLength);
            }
        }
    }

    // Безопасный способ увеличения счётчика
    public static void incrementCounter(int len) {
        switch (len) {
            case 3 -> counterLength3.incrementAndGet();
            case 4 -> counterLength4.incrementAndGet();
            case 5 -> counterLength5.incrementAndGet();
        }
    }
}