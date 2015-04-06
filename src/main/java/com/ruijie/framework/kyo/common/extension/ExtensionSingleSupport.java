package com.ruijie.framework.kyo.common.extension;

import com.ruijie.framework.kyo.session.serialization.Serialization;
import com.ruijie.framework.kyo.session.serialization.SerializationFactory;
import com.ruijie.framework.kyo.session.store.Store;
import com.ruijie.framework.kyo.session.store.StoreFactory;
/**
 * 扩展点的单例支持类
 * @author liq
 *
 */
public class ExtensionSingleSupport {
	
    public static final Serialization SERIALIZATION_EXTENSION = Extensions.getExtensionLoader(SerializationFactory.class).getAdapativeExtension().getSerialization();
 
    public static final Store Store_EXTENSION = Extensions.getExtensionLoader(StoreFactory.class).getAdapativeExtension().getStore();

}
