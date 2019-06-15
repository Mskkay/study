#一、netty做了平时web中就有的事
    在普通的web编程中，其实有很多事是由容器（tomcat,jetty）和框架（spring）帮我们干了。
    然而netty并不依赖于上述，因此在netty中要做一些平时我们没有注意的事情。比如沾包与拆包