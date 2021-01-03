package Chapter4.interrupt;

/**
 * 中断标志位isInterrupted和中断异常InterruptedException<br/>
 * 当线程在{@link Thread#sleep(long)}状态下被中止时，会抛出异常，且重置中断标志位为false。
 *
 * @author zmx
 * @date 2021-01-03
 */
public class HasInterruptedException {
    private static class UseThread extends Thread {
        public UseThread(String name) {
            super(name);
        }

        public static void main(String[] args) throws InterruptedException {
            Thread endThread = new UseThread("HasInterruptEx");
            endThread.start();
            Thread.sleep(100);
/*  这里当Thread.sleep()中的参数millis大于3时，就会有异常发生，为什么呢？
    因为调用interrupt的时候，子线程甚至还么来的及初始化完成，即还没走到sleep方法
    sleep的时间在1millis时候不会发生异常，在3millis时候会抛出异常，所以初始化时间大概在1-3millis之间  */
            endThread.interrupt();
        }
/*        线程调用sleep方法后进入sleep状态，而sleep方法中java在实现的时候支持对中断标志位的检查，
        一旦sleep方法检查到了中断标志位为true，就会终止sleep，并抛出这个InterruptedException。
        方法里如果抛出InterruptedException,
        线程的中断标志位会被复位成false，如果确定是需要中断线程，
        要求我们自己在catch语句块里再次调用interrupt()
        InterruptedException表示一个等待阻塞被中断了，阻塞包括sleep(),wait()
        注意上面说的阻塞和synchronized的同步阻塞不是一回事*/

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("the  flag  is " + isInterrupted());
                    e.printStackTrace();
                    interrupt();
                    System.out.println("the  flag2  is " + isInterrupted());
                }
            }
        }
    }
}