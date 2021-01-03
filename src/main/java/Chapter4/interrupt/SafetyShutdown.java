package Chapter4.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * 安全停止线程的两种方法：<br/>
 * 1:使用boolean标志终止线程(on)<br/>
 * 2:使用isInterrupted()方法判断当前线程已终止
 *
 * @author zhou mingxin
 * @since 2021/1/3
 **/
public class SafetyShutdown {
    public static void main(String[] args) throws InterruptedException {
        Thread interruptToStop = new Thread(new Runner(), "CountThread");
        interruptToStop.start();
        TimeUnit.SECONDS.sleep(1);
        interruptToStop.interrupt();

        Runner flagRunner = new Runner();
        Thread flagToStop = new Thread(flagRunner, "CountThread");
        flagToStop.start();
        TimeUnit.SECONDS.sleep(1);
        flagRunner.cancel();
    }

    private static class Runner implements Runnable {
        private long i;
        private volatile boolean on = true;

        @Override
        public void run() {
            while (on && !Thread.currentThread().isInterrupted()) {
                i++;
            }
            System.out.println("current i value = " + i);
        }

        public void cancel() {
            on = false;
        }
    }
}
