/**
 * wait用法
 *
 * @author DreamSea
 * @time 2015.3.9
 */
package Chapter4.NotifyAndWait;

public class ABCPrinter implements Runnable {

    private final String name;
    private final Object prev;
    private final Object self;

    private ABCPrinter(String name, Object prev, Object self) {
        this.name = name;
        this.prev = prev;
        this.self = self;
    }

    public static void main(String[] args) throws Exception {
        Object a = new Object();
        Object b = new Object();
        Object c = new Object();

        ABCPrinter pa = new ABCPrinter("A", c, a);
        ABCPrinter pb = new ABCPrinter("B", a, b);
        ABCPrinter pc = new ABCPrinter("C", b, c);

        new Thread(pa).start();
        Thread.sleep(100);  //确保按顺序A、B、C执行
        new Thread(pb).start();
        Thread.sleep(100);
        new Thread(pc).start();
        Thread.sleep(100);
    }

    @Override
    public void run() {
        int count = 10;
        while (count > 0) {
            synchronized (prev) {
                synchronized (self) {
                    System.out.print(name);
                    count--;

                    self.notify();  //释放self的锁，给下一位使用
                }
                try {
                    // 不判定count，直接prev.wait()的话会死锁
                    if (count > 0) {
                        prev.wait();        //等待prev的锁被释放
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}  
 