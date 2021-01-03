package Chapter4.NotifyAndWait;

/**
 * 笔记：<br/>
 * 1、通过synchronized关键字定义的同步方法，在进入时若未获取到锁，则会进入BLOCKED状态，阻塞态会一直尝试去获取锁<br/>
 * 2、通过调用对象的wait()方法，会使当前线程进入WAITING状态，进入WAITING状态的线程不会尝试获取锁，必须被notify()后，获取锁才能被唤醒为RUNNABLE状态<br/>
 * 3、在线程调用对象的wait()、notify()、notifyAll()方法之前，要先对这个对象加锁<br/>
 * 4、在使用notify()、notifyAll()方法进行通知时，不会立即释放锁，而是当前同步方法执行完之后才释放锁<br/>
 * 5、线程调用wait()方法后会放弃锁，然后一直卡在wait。直到其他线程调用notify()方法，等待线程的wait才会返回。<br/>
 * <br/>
 * 注意：<br/>
 * 1、这里只new了<b>唯一</b>一个NotifyAndNotifyAll对象n<br/>
 * 2、不论是add/sub方法修饰中的synchronized还是add/sub方法中的wait()/notify()，他们使用的<b>锁都是同一个</b>：由唯一的n来提供。
 * <br/><br/>
 *
 * @author zhou mingxin
 * @since 2020/12/20
 **/
public class NotifyOrNotifyAll {
    private int count = 0;
    private boolean flag = true;

    public static void main(String[] args) {
        NotifyOrNotifyAll n = new NotifyOrNotifyAll();
        // 循环创建add线程
        for (int i = 0; i < 3; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 5; j++) {
                        n.add();
                    }
                }
            }, "add:group" + i).start();
        }

        // 循环创建sub线程
        for (int i = 0; i < 3; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 5; j++) {
                        n.sub();
                    }
                }
            }, "sub:group" + i).start();
        }
    }

    private synchronized void add() {
        while (!flag) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        flag = false;
        System.out.println("count:" + count++);
        this.notify();  //这里用notify会造成死锁，用notifyAll则不会。一般都用notifyAll，除非只存在一个需要被唤醒的线程。
    }

    private synchronized void sub() {
        while (flag) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        flag = true;
        System.out.println("count:" + count--);
        this.notify();
    }


}
