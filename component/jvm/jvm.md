Java内存模型（JMM）是一种内存规范，它可以屏蔽各种硬件和操作系统的访问差异，从而保证一段Java程序在不同的平台上运行都能得到一样的结果。如何保证？JMM可提供影响并发编程的原子性操作（synchronized和Lock）、可见性操作（volatile、synchronized和Lock）、有序性操作（volatile、synchronized和Lock以及happens-before原则）。
1）Java内存模型将内存分为了主内存和工作内存。类的状态，也就是类之间共享的变量，是存储在主内存中的，每次Java线程用到这些主内存中的变量的时候，会读一次主内存中的变量，并让这些内存在自己的工作内存中有一份拷贝，运行自己线程代码的时候，用到这些变量，操作的都是自己工作内存中的那一份。在线程代码执行完毕之后，会将最新的值更新到主内存中去。
2）定义了几个原子操作，用于操作主内存和工作内存中的变量。
4）happens-before，即先行发生原则，定义了操作A必然先行发生于操作B的一些规则，比如在同一个线程内控制流前面的代码一定先行发生于控制流后面的代码、一个释放锁unlock的动作一定先行发生于后面对于同一个锁进行锁定lock的动作等等，只要符合这些规则，则不需要额外做同步措施，如果某段代码不符合所有的happens-before规则，则这段代码一定是线程非安全的

 由于synchronized和Lock能够保证任一时刻只有一个线程执行该代码块，那么自然就不存在原子性问题了，从而保证了原子性。synchronized和Lock能保证同一时刻只有一个线程获取锁然后执行同步代码，并且在释放锁之前会将对变量的修改刷新到主存当中。因此可以保证可见性。synchronized和Lock保证每个时刻是有一个线程执行同步代码，相当于是让线程顺序执行同步代码，自然就保证了有序性。
  Volatile关键字可以　1）保证了不同线程对这个变量进行操作时的可见性，即一个线程修改了某个变量的值，这新值对其他线程来说是立即可见的。2）禁止进行指令重排序（通过添加内存屏障）。Volatile无法保证对变量的操作的原子性（要么都执行，要么都不执行。）。（比如自增操作：包括读取变量的原始值、进行加1操作、写入工作内存。）
  通常来说，使用volatile的场景必须具备以下2个条件：1）对变量的写操作不依赖于当前值；2）该变量没有包含在具有其他变量的不变式中。（我的理解就是上面的2个条件需要保证操作是原子性操作，才能保证使用volatile关键字的程序在并发时能够正确执行。）（比如：单列模式的双重检查（double check））


当程序在运行过程中，会将运算需要的数据从主存复制一份到CPU的高速缓存当中，那么CPU进行计算时就可以直接从它的高速缓存读取数据和向其中写入数据，当运算结束之后，再将高速缓存中的数据刷新到主存当中。
这一过程在单线程运行是没有问题的，但是在多线程中运行就会有问题了。在多核CPU中，每条线程可能运行于不同的CPU中，因此每个线程运行时有自己的高速缓存（对单核CPU来说，其实也会出现这种问题，只不过是以线程调度的形式来分别执行的）。这时CPU缓存中的值可能和缓存中的值不一样，这就是著名的缓存一致性问题。
 缓存一致性协议（MESI）的核心思想：当CPU在读取数据时，如果发现操作的变量是共享变量，即在其它CPU中也存在改变量的副本，它就会发出信号通知其它CPU将该变量的缓存行置为无效状态，因此当其它CPU需要读取这个变量的时候就会发现自己缓存中的该变量处于无效状态，那么它就会从主内存中重新读取。
Java内存模型规定所有变量都存储在主存（Main Memory）中（虚拟机内存的一部分）。每条线程还有自己的工作内存（Working Memory），线程的工作内存保存了被线程使用到的变量的主内存副本拷贝，线程对变量的所有操作（读取/赋值等）都必须在工作内存中进行，而不能直接读写主内存中的变量。不同线程之间也无法直接访问对方工作内存中的变量，线程间变量值的传递均需要通过主存来完成。