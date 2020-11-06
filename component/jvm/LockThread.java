public class LockThread {
    Lock lock = new ReentrantLock();

    public void lock(String name) {
        // 获取锁
        lock.lock();
        try {
            System.out.println(name + " get the lock");
            // 访问此锁保护的资源
        } finally {
            // 释放锁
            lock.unlock();
            System.out.println(name + " release the lock");
        }
    }

    public static void main(String[] args) {
        LockThread lt = new LockThread();
        new Thread(() -> lt.lock("A")).start();
        new Thread(() -> lt.lock("B")).start();
    }
}