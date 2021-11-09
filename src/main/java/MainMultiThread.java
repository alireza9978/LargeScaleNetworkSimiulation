import Models.Network;
import Models.Node;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class MainMultiThread {

    static abstract class TestParent {

        public abstract void work();

    }

    static class TestObject extends TestParent {

        @Override
        public void work() {
            System.out.println("test object");
        }
    }

    static class OtherTestObject extends TestParent {

        @Override
        public void work() {
            System.out.println("other test object");
        }
    }

    static class TestThread extends Thread {

        private final TestParent object;

        public TestThread(TestParent object) {
            this.object = object;
        }

        public void run() {
            object.work();
        }

    }

    public void test() {
        int count = 50;
        ExecutorService executor = Executors.newFixedThreadPool(5);//creating a pool of 5 threads
        for (int i = 0; i < count; i++) {
            executor.execute(new TestThread(new TestObject()));
            executor.execute(new TestThread(new OtherTestObject()));
        }
        executor.shutdown();
        boolean state = false;
        try {
            state = executor.awaitTermination(1000, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Finished all threads = " + state);
    }

    public void testTwo() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10_000_000L; i++) {
            for (int k = 0; k < 1_000_000; k++) ;
        }
        System.out.println(System.currentTimeMillis() - start);

        ExecutorService executor = Executors.newFixedThreadPool(28);
        start = System.currentTimeMillis();
        for (long i = 0; i < 10_000_000L; i++) {
            executor.execute(() -> {
                for (int k = 0; k < 1_000_000; k++) ;
            });
        }
        executor.shutdown();
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        LongStream.range(0, 10_000_000L).parallel().forEach(value -> {
            for (int k = 0;k < 1_000_000;k++);
        });
        System.out.println(System.currentTimeMillis() - start);

    }

    public static void main(String[] args) {
        MainMultiThread t = new MainMultiThread();
        t.testTwo();
    }

}
