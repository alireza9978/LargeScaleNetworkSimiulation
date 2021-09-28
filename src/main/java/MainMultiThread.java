import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Finished all threads = " + state);

    }

    public static void main(String[] args) {
        MainMultiThread t = new MainMultiThread();
        t.test();
    }

}
