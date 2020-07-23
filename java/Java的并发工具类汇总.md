# Atomic包
更新基本类型变量Atomic包提高原子更新基本类型的工具类，主要有这些：
AtomicBoolean：以原子更新的方式更新boolean；
AtomicInteger：以原子更新的方式更新Integer;
AtomicLong：以原子更新的方式更新Long；

更新数组类型变量:
AtomicIntegerArray：原子更新整型数组中的元素；
AtomicLongArray：原子更新长整型数组中的元素；
AtomicReferenceArray：原子更新引用类型数组中的元素

更新引用类型变量:
AtomicReference：原子更新引用类型；
AtomicReferenceFieldUpdater：原子更新引用类型里的字段；
AtomicMarkableReference：原子更新带有标记位的引用类型；

更新对象的某个字段:
AtomicIntegeFieldUpdater：原子更新整型字段类；
AtomicLongFieldUpdater：原子更新长整型字段类；
AtomicStampedReference：原子更新引用类型，这种更新方式会带有版本号。而为什么在更新的时候会带有版本号，是为了解决CAS的ABA问题；

    person = new Person("Tom", 18);
    aRperson = new AtomicReference<Person>(person);
    System.out.println("Atomic Person is " + aRperson.get().toString());
    aRperson.getAndSet(new Person("Tom2", aRperson.get().getAge() + 2));

# CountDownLatch
public class CountDownLatchDemo implements Runnable{

    static final CountDownLatch latch = new CountDownLatch(10);
    static final CountDownLatchDemo demo = new CountDownLatchDemo();

    @Override
    public void run() {
        // 模拟检查任务
        try {
            Thread.sleep(new Random().nextInt(10) * 1000);
            System.out.println("check complete");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //计数减一
            //放在finally避免任务执行过程出现异常，导致countDown()不能被执行
            latch.countDown();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newFixedThreadPool(10);
        for (int i=0; i<10; i++){
            exec.submit(demo);
        }

        // 等待检查
        latch.await();

        // 发射火箭
        System.out.println("Fire!");
        // 关闭线程池
        exec.shutdown();
    }
}
# CyclicBarrier
public class CyclicBarrierDemo {
    //指定必须有20个乘客到达才行
    private static CyclicBarrier barrier = new CyclicBarrier(20, () -> {
        System.out.println("所有乘客都上车了，司机开车！！！！！");
    });
    public static void main(String[] args) {
        System.out.println("乘客准备上车...........");

        ExecutorService service = Executors.newFixedThreadPool(6);
        for (int i = 0; i < 6; i++) {
            service.execute(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " 乘客，上车");
                    barrier.await();
                    System.out.println(Thread.currentThread().getName() + "  乘客，上车");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
    }

}

CountDownLatch与CyclicBarrier的比较
这两者还是各有不同侧重点的：

1、CountDownLatch一般用于某个线程A等待若干个其他线程执行完任务之后，它才执行；而CyclicBarrier一般用于一组线程互相等待至某个状态，然后这一组线程再同时执行；CountDownLatch强调一个线程等多个线程完成某件事情。CyclicBarrier是多个线程互等，等大家都完成，再携手共进。

2、调用CountDownLatch的countDown方法后，当前线程并不会阻塞，会继续往下执行；而调用CyclicBarrier的await方法，会阻塞当前线程，直到CyclicBarrier指定的线程全部都到达了指定点的时候，才能继续往下执行；

3、CountDownLatch方法比较少，操作比较简单，而CyclicBarrier提供的方法更多，比如能够通过getNumberWaiting()，isBroken()这些方法获取当前多个线程的状态，并且CyclicBarrier的构造方法可以传入barrierAction，指定当所有线程都到达时执行的业务功能；

4、CountDownLatch是不能复用的，而CyclicLatch是可以复用的。


# Semaphore

/**

 * Semaphore叫信号量 or 信号灯

 * Semaphore有两个目的，第一个目的是多个共享资源互斥使用，第二目的是并发线程数的控制

 */

public class SemaphoreDemo {

    public static void main(String[] args) {

        // 模拟厕所10个茅坑

        Semaphore semaphore = new Semaphore(5);

        for (int i = 1; i <= 10; i++) {

            new Thread(() -> {

                try {

                    // 获取锁资源

                    semaphore.acquire();

                    System.out.println(Thread.currentThread().getName() + "\t上厕所");

                    // 模拟人上厕所10秒，然后让出坑位

                    TimeUnit.SECONDS.sleep(5);

                    System.out.println(Thread.currentThread().getName() + "\t上完厕所，让出坑位");

                } catch (InterruptedException e) {

                    e.printStackTrace();

                } finally {

                    // 释放锁资源

                    semaphore.release();

                }

            }, "" + i + "号帅哥").start();

        }

    }
}
# Exchanger
Exchanger是一个用于线程间协作的工具类，用于两个线程间能够交换。它提供了一个交换的同步点，在这个同步点两个线程能够交换数据。具体交换数据是通过exchange方法来实现的，如果一个线程先执行exchange方法，那么它会同步等待另一个线程也执行exchange方法，这个时候两个线程就都达到了同步点，两个线程就可以交换数据。
public class ExchangerDemo {
    private static Exchanger<String> exchanger = new Exchanger();

    public static void main(String[] args) {

        //代表男生和女生
        ExecutorService service = Executors.newFixedThreadPool(2);

        service.execute(() -> {
            try {
                //男生对女生说的话
                String girl = exchanger.exchange("我其实暗恋你很久了......");
                System.out.println("女孩儿说：" + girl);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        service.execute(() -> {
            try {
                System.out.println("女生慢慢的从教室你走出来......");
                TimeUnit.SECONDS.sleep(3);
                //男生对女生说的话
                String boy = exchanger.exchange("我也很喜欢你......");
                System.out.println("男孩儿说：" + boy);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }
}



