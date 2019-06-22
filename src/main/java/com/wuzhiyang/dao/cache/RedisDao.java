package com.wuzhiyang.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.wuzhiyang.entity.InstantKill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author wzy
 */
public class RedisDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JedisPool jedisPool;

    public RedisDao(String ip, int port, int timeout, String password) {
        jedisPool = new JedisPool(new JedisPoolConfig(), ip, port, timeout, password);
    }

    private RuntimeSchema<InstantKill> schema = RuntimeSchema.createFrom(InstantKill.class);

    public InstantKill getIntKill(long instantkillId) {
        //redis操作逻辑
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "instantKill:" + instantkillId;
                //并没有实现内部序列化操作
                // get-> byte[] -> 反序列化 ->Object(Seckill)
                // 采用自定义序列化
                //protostuff : pojo.
                byte[] bytes = jedis.get(key.getBytes());
                //缓存中获取到bytes
                if (bytes != null) {
                    //空对象
                    InstantKill instantKill = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes, instantKill, schema);
                    //seckill 被反序列化
                    return instantKill;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public String putSeckill(InstantKill instantKill) {
        // set Object(Seckill) -> 序列化 -> byte[]
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "instantKill:" + instantKill.getInstantkillId();
                byte[] bytes = ProtostuffIOUtil.toByteArray(instantKill, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //超时缓存
                int timeout = 60 * 60;//1小时
                String result = jedis.setex(key.getBytes(), timeout, bytes);
                return result;
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }
}
