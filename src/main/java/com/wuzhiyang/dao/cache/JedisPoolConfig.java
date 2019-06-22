package com.wuzhiyang.dao.cache;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author wzy
 */
public class JedisPoolConfig extends GenericObjectPoolConfig {
    public JedisPoolConfig() {
        // defaults to make your life with connection pool easier :)
        setTestWhileIdle(true);
        //
        setMinEvictableIdleTimeMillis(60000);
        //
        setTimeBetweenEvictionRunsMillis(30000);
        setNumTestsPerEvictionRun(-1);
    }
}
