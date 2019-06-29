#一、netty做了平时web中就有的事
    在普通的web编程中，其实有很多事是由容器（tomcat,jetty）和框架（spring）帮我们干了。
    然而netty并不依赖于上述，因此在netty中要做一些平时我们没有注意的事情。比如沾包与拆包
    
#二、netty做了很多独特的事情
###2.1 nioEventLoop
#####2.1.1 netty如何解决Jdk空轮询bug
    JDK空轮询Bug指，jdk本身在处理select()操作时，在极端情况下会无限地对执行select()操作，不阻塞并且立即返回，然后立即再执行。这会导致cpu使用率飚到100%
  netty在这里使用的思路是，计算每次select操作执行的实际时间，与select操作的限制时间进行比对  
  ``` 
     //select()开始执行的时间
     long currentTimeNanos = System.nanoTime();
     ...
     //select()操作的阻塞时间
     long timeoutMillis = (selectDeadLineNanos - currentTimeNanos + 500000L) / 1000000L;
     ...
     int selectedKeys = selector.select(timeoutMillis);
     ++selectCnt;
     ...
                   
     //select()操作执行完毕后当前的时间
     long time = System.nanoTime();
     if (time - TimeUnit.MILLISECONDS.toNanos(timeoutMillis) >= currentTimeNanos) {
     //如果执行时间是大于等于阻塞时间的，那么就算selectors执行了一次不是空轮询的操作
        selectCnt = 1;
     } else if (SELECTOR_AUTO_REBUILD_THRESHOLD > 0 && selectCnt >= SELECTOR_AUTO_REBUILD_THRESHOLD) {
     //如果执行时间是小于，那么就说明执行了一次空轮询。并且空轮询的次数大于500（因为只要成功轮询一次，上面的if就会把selectCnt置为1）。那么就重建selector并且注册
        logger.warn("Selector.select() returned prematurely {} times in a row; rebuilding Selector {}.", selectCnt, selector);
        this.rebuildSelector();
        selector = this.selector;
        selector.selectNow();
        selectCnt = 1;
        break;
     }
 ``` 
#####2.1.2 netty如何保证异步串行无锁化

#####2.1.3 PowerOfTowEventExecutorChooser()
  当一个新的连接进来的时候，nioEventLoop会使用chooserFactory.newChooser()为每一个新连接分配nioEventLoop  
  一般来说nioEventLoop是个数组，我们只需要每个新连接进来的时候进行一个取模（%）的操作，就能平均给每个nel分配连接了  
  然而这里netty也很丧心病狂地进行了优化，当新连接的下标为2的幂（2，4，8，16。。。）的时候，netty会使用PowerOfTowEventExecutorChooser，
  通过一个很要命的算法 idx++ & executors.length - 1 来进行分配。因为按位与是明显比取模更快的  
  即2^n % k = 2^n & (k-1) 

#####2.1.4 processSelectedKey()
  这个方法是selector获取到相关的事件后执行的方法，其中有两步
  1. selected KeySet 优化
  2. processSelectedKeysOptimized()  
  其中第二步很好理解，执行时间操作嘛。但是第一步是个什么鬼呢？  
  selector轮询触发结果之后，它优先返回的是一个size而不是int[]，换句话说，它说明了有多少事件触发了，但不知道具体哪些事件的fd（为什么？）。
  而当我们试图获取这些事件的fd的时候，它会把这些fd放入一个set中。netty认为这里完全不需要一个set，于是netty使用反射的操作，将一个披着
  set皮的array替换了selector的fdSet。从而使向fdSet中addFd的时间复杂度减少到了O(n)，因为不需要

###2.2 FastThreadLocalThread