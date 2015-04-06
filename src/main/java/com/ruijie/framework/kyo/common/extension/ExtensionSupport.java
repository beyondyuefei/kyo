package com.ruijie.framework.kyo.common.extension;

import com.ruijie.framework.kyo.session.serialization.Serialization;
import com.ruijie.framework.kyo.session.store.Store;
/**
 * 
 * @author liq
 *
 */
public class ExtensionSupport {
	
    public static final Serialization SERIALIZATION_EXTENSION = ExtensionLoader.getExtensionLoader(Serialization.class).getAdapativeExtension();
 
    public static final Store Store_EXTENSION = ExtensionLoader.getExtensionLoader(Store.class).getAdapativeExtension();

}
