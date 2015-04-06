package com.ruijie.framework.kyo.session.store;

import com.ruijie.framework.kyo.common.Constants;
import com.ruijie.framework.kyo.common.extension.Key;
import com.ruijie.framework.kyo.common.extension.SPI;

@SPI("redis")
public interface StoreFactory {
   @Key(Constants.SESSION_STORE_KEY)
   Store getStore();
}
