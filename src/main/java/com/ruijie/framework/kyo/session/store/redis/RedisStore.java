package com.ruijie.framework.kyo.session.store.redis;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ruijie.framework.kyo.common.Constants;
import com.ruijie.framework.kyo.common.extension.ExtensionSingleSupport;
import com.ruijie.framework.kyo.session.store.Store;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisStore implements Store {
	private static final Logger logger = LoggerFactory.getLogger(RedisStore.class);
	private ShardedJedisPool jedisPool;
    
	public RedisStore() {
		GenericObjectPool.Config config = new GenericObjectPool.Config();
		config.testOnBorrow = Boolean.valueOf(Constants
				.get("redis.pool.testOnBorrow"));
		config.testOnReturn = Boolean.valueOf(Constants
				.get("redis.pool.testOnReturn"));
		config.testWhileIdle = Boolean.valueOf(Constants
				.get("redis.pool.testWhileIdle"));
		config.maxIdle = Integer.valueOf(Constants.get("redis.pool.maxIdle"));
		config.minIdle = Integer.valueOf(Constants.get("redis.pool.minIdle"));
		config.maxActive = Integer.valueOf(Constants.get("redis.pool.maxActive"));
		config.maxWait = Long.valueOf(Constants.get("redis.pool.maxWait"));
		config.numTestsPerEvictionRun = Integer.valueOf(Constants
				.get("redis.pool.numTestsPerEvictionRun"));
		config.timeBetweenEvictionRunsMillis = Integer.valueOf(Constants
				.get("redis.pool.timeBetweenEvictionRunsMillis"));
		config.minEvictableIdleTimeMillis = Integer.valueOf(Constants
				.get("redis.pool.minEvictableIdleTimeMillis"));
		
		int timeout = Integer.valueOf(Constants.get("redis.pool.timeout"));
		List<JedisShardInfo> addressList = new ArrayList<JedisShardInfo>();
		String[] address_arr = Constants.get("redis.address").split(";");
		
		if(address_arr == null || address_arr.length == 0) {
		    logger.error("redis.address 不能为空! ");
		    return;
		}
		
		for(int i = 0;i < address_arr.length;i++) {
		    String[] address = address_arr[i].split(":");
		    if(address == null || address.length != 2) {
		        logger.error("redis.address 配置不正确!");
	            return;
		    }
		    String host = address[0];
		    int port = Integer.valueOf(address[1]);
		    
		    logger.info("redis服务器" + (i+1) + "的地址为: " + host + ":" + port);
		    
		    addressList.add(new JedisShardInfo(host, port,timeout));
		}
		
		jedisPool = new ShardedJedisPool(config,addressList);

		logger.info("redis对象池初始化完毕!");
    }
	
	@Override
	public HttpSession getSession(String sessionId) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			byte[] sessionByte = jedis.get(sessionId.getBytes());
			if(sessionByte == null) {
				return null;
			} 
			return ExtensionSingleSupport.SERIALIZATION_EXTENSION.deserialize(sessionByte);
		} catch(Exception e) {
		   logger.error("redis get error,the key is: " + sessionId + " and the value is: " + jedis.get(sessionId));
		   throw new RuntimeException(e);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public void removeSession(String sessionId) {
		 ShardedJedis jedis = jedisPool.getResource();
			try {
				jedis.del(sessionId);
			} finally {
				jedisPool.returnResource(jedis);
			}
	}

	@Override
	public void setSession(HttpSession session,int  expireDealy) {
		  ShardedJedis jedis = jedisPool.getResource();
			try {
				jedis.setex(session.getId().getBytes(),expireDealy,ExtensionSingleSupport.SERIALIZATION_EXTENSION.serialize(session));
			} finally {
				jedisPool.returnResource(jedis);
			}
	}

}
