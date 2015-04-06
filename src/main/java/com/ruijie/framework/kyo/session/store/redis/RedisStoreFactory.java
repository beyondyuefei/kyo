package com.ruijie.framework.kyo.session.store.redis;

import com.ruijie.framework.kyo.common.extension.Adaptive;
import com.ruijie.framework.kyo.session.store.Store;
import com.ruijie.framework.kyo.session.store.StoreFactory;
@Adaptive("redis")
public class RedisStoreFactory implements StoreFactory {

	@Override
	public Store getStore() {
		return new RedisStore();
	}

}
