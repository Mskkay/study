package com.zhai.netty.spring.attribute;

import com.zhai.netty.spring.session.Session;
import io.netty.util.AttributeKey;

/**
 * @author Administrator
 */
public interface Attributes {

    /**
     * 这里AttributeKey.newInstance("session")，netty使用这个方法生成一个对象作为key
     */
    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
}
