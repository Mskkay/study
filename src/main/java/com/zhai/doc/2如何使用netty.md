#一、开始使用netty
  这一篇主要是记录如何写出一个netty在的demo，用于作为以后netty项目的脚手架。以及分析创建过程中的源码
  
###1.1 netty的服务端demo（参考com.zhai.netty.server.demo）
    如果只想快速地知道如何使用netty的api,看这里应该就可以了
  首先再次强调，netty是一个
    
    异步事件驱动框架  

  这也就指出了
  - netty关注的是如何处理接收大量信息的时候，通常来讲这是指服务端。所以客户端并不是重点（甚至服务端也不是，一个需要处理大量网络block请求的场景才是）
  - 并且，netty虽然支持多种协议，但这并不是重点。协议的解析可以由编程者实现，使用netty关注的应该是拆包沾包，以及消息字节码在网络端口，内核缓存区，程序缓存区的流动才是重点
  
#####1.1.1 netty的服务端的启动
  1. create channel 调用jdk底层生成一个socket，并且Netty对其进行封装
  2. init channel 初始化，注册逻辑处理器
  3. 注册selector 有关selector更多，参考[select, poll, epoll](4从netty思考更多.md)
  
#####1.1.2nioEventLoop的创建  
  
    new NioEventLoopGroup()【线程组，默认CPU*2】
        new ThreadPerTaskExecutor()【线程创建器】
        for() {
            new Child()【构造NioEventLoop】
        }
        chooserFactory.newChooser()【线程选择器】
  
  - nioEventLoop的线程命名规则 nioEventLoop-1-xx
  - new Child() 中 创建了线程执行器，MpscQueue(事件队列)，selector
  
#####1.1.3nioEventLoop的启动

#####1.1.4nioEventLoop的执行
  nioEventLoop有一个无限循环的run方法，这个方法的执行内容是select() -> processSelectedKeys() -> runAllTasks()  
  selector.select()也是一个阻塞的操作
  
###1.2 netty的服务端demo（参考com.zhai.netty 在spring+netty 的分支上）
  首先要了解的是，其实netty和spring并没有什么鸟关系。netty并不依赖于MVC模式，也不需要像web项目那样用tomcat啥的支持。netty在启动的时候
本身就会要求一个独立端口。    
  之所以我们要整合spring+netty，只是作为一个在web项目中的脚手架。可以看到的是，唯一两个有交界的地方在于，serverHandler如何调用受spring管理的
bean，spring的bean如何调用netty向客户端发送信息