/**
 * Атомарные операции
 */

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Лаунчер
 */

public class Main {
    public static void main(String[] args) throws InterruptedException {
        new InterferenceExample().example();
    }

    /**
     * Пример совместного использования ресурсов
     */
    static class InterferenceExample {
        private static final int HUNDRED_MILLION = 100_000_000;
        private AtomicInteger counter = new AtomicInteger();
        private final AtomicInteger i = new AtomicInteger();


        boolean stop(){
            return counter.incrementAndGet() > HUNDRED_MILLION;
        }

        void increment(){
            i.incrementAndGet();
        }


        int getI(){
            return i.get();
        }

        void example() throws InterruptedException {
            InterferenceThread thread1 = new InterferenceThread(this);
            InterferenceThread thread2 = new InterferenceThread(this);
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();


            int actualIncremets = getI();
            System.out.println("Expected: " + HUNDRED_MILLION);
            System.out.println("Result: " + actualIncremets);


        }
    }

    /**
     * Класс для инкрементирования общей переменной
     */

    public static class InterferenceThread extends Thread {
        private final InterferenceExample checker;


        InterferenceThread(InterferenceExample checker) {
            this.checker = checker;
        }


        public void run() {
            while (!checker.stop()) {
                checker.increment();
            }
        }

    }
}
