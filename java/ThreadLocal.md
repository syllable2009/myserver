# ThreadLocal
ThreadLocal是一个本地线程副本变量工具类。主要用于将私有线程和该线程存放的副本对象做一个映射，
各个线程之间的变量互不干扰，在高并发场景下，可以实现无状态的调用，特别适用于各个线程依赖不通的变量值完成操作的场景。

每个Thread线程内部都有一个threadLocals，结构为ThreadLocalMap。
    void createMap(Thread t, T firstValue) {
        t.threadLocals = new ThreadLocalMap(this(threadLocal对象), firstValue);
    }
Map里面存储线程本地对象ThreadLocal（key）和线程的变量副本（value）。
Thread内部的Map是由ThreadLocal维护，ThreadLocal负责向map获取和设置线程的变量值。
一个Thread可以有多个ThreadLocal。
    public T get() {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                @SuppressWarnings("unchecked")
                T result = (T)e.value;
                return result;
            }
        }
        return setInitialValue();
    }
每个ThreadLocal只能保存一个变量副本，此方式能避免线程争抢Session，提高并发下的安全性。
ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
try {
    threadLocal.set(new Session(1, "Misout的博客"));
    // 其它业务逻辑
} finally {
    threadLocal.remove();
}
# 实现原理
ThreadLocal每个线程维护一个 ThreadLocalMap 的映射表，映射表的 key 是 ThreadLocal 实例本身，value 是要存储的副本变量。ThreadLocal 实例本身并不存储值，它只是提供一个在当前线程中找到副本值的 key。 
线程使用 ThreadLocalMap 来存储每个线程副本变量，它是 ThreadLocal 里的一个静态内部类。ThreadLocalMap 也是采用的散列表（Hash）思想来实现的，但是实现方式和 HashMap 不太一样。

在理想状态下，哈希函数可以将关键字均匀的分散到数组的不同位置，不会出现两个关键字散列值相同（假设关键字数量不大于数组的大小）的情况。
但是在实际使用中，经常会出现多个关键字散列值相同的情况（被映射到数组的同一个位置），我们将这种情况称为散列冲突。为了解决散列冲突，主要采用下面两种方式：
分离链表法（separate chaining）：分散链表法使用链表解决冲突，将散列值相同的元素都保存到一个链表中。当查询的时候，首先找到元素所在的链表，然后遍历链表查找对应的元素。
开放定址法（open addressing）：开放定址法不会创建链表，当关键字散列到的数组单元已经被另外一个关键字占用的时候，就会尝试在数组中寻找其他的单元，直到找到一个空的单元。探测数组空单元的方式有很多，这里介绍一种最简单的 -- 线性探测法。线性探测法就是从冲突的数组单元开始，依次往后搜索空单元，如果到数组尾部，再从头开始搜索（环形查找）
每个线程只存一个变量，这样的话所有的线程存放到map中的Key都是相同的ThreadLocal，如果一个线程要保存多个变量，就需要创建多个ThreadLocal，多个ThreadLocal放入Map中时会极大的增加Hash冲突的可能。



ThreadLocal内部的ThreadLocalMap键为弱引用，会有内存泄漏的风险。