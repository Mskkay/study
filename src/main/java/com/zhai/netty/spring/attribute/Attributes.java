package com.zhai.netty.spring.attribute;

import com.zhai.netty.spring.session.Session;
import io.netty.util.AttributeKey;

/**
 * @author Administrator
 */
public interface Attributes {

    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
}
