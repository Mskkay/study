#一、netty做了平时web中就有的事
    在普通的web编程中，其实有很多事是由容器（tomcat,jetty）和框架（spring）帮我们干了。
    然而netty并不依赖于上述，因此在netty中要做一些平时我们没有注意的事情。比如沾包与拆包
    
#二、netty做了很多独特的事情
###2.1 nioEventLoop
#####2.1.1 netty如何解决Jdk空轮询bug

#####2.1.2 netty如何保证异步串行无锁化

#####2.1.3 PowerOfTowEventExecutorChooser
  当一个新的连接进来的时候，nioEventLoop会使用chooserFactory.newChooser()为每一个新连接分配nioEventLoop  
  一般来说nioEventLoop是个数组，我们只需要每个新连接进来的时候进行一个取模（%）的操作，就能平均给每个nel分配连接了  
  然而这里netty也很丧心病狂地进行了优化，当新连接的下标为2的幂（2，4，8，16。。。）的时候，netty会使用PowerOfTowEventExecutorChooser，
  通过一个很要命的算法 idx++ & executors.length - 1 来进行分配。因为按位与是明显比取模更快的  
  即2^n % k = 2^n & (k-1) 

###2.2 FastThreadLocalThread