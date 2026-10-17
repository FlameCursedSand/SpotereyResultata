/**
 * Synchronized
 */

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Лаунчер
 */

public class Main2 {
    public static void main(String[] args) throws InterruptedException {
        new InterferenceExample().example();
    }

    /**
     * Пример совместного использования ресурсов
     */
    static class InterferenceExample {
        private static final int HUNDRED_MILLION = 100_000_000;
        private AtomicInteger counter = new AtomicInteger();

        boolean stop(){
            return counter.incrementAndGet() > HUNDRED_MILLION;
        }

        void example() throws InterruptedException {
            InterferenceThread thread1 = new InterferenceThread(this);
            InterferenceThread thread2 = new InterferenceThread(this);
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
            System.out.println("Expected: " + HUNDRED_MILLION);
            System.out.println("Result: " + thread1.getI());
        }
    }

    /**
     * Класс для инкрементирования общей переменной
     */

    public static class InterferenceThread extends Thread {
        private final InterferenceExample checker;
        private static int i;

        InterferenceThread(InterferenceExample checker) {
            this.checker = checker;
        }

        private static synchronized void increment() {
            i++;
        }

        private static synchronized int getI() {
            return i;
        }

        public void run() {
            while (!checker.stop()) {
                increment();
            }
        }
    }
}
