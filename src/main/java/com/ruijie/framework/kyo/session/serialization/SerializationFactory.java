package com.ruijie.framework.kyo.session.serialization;

import com.ruijie.framework.kyo.common.Constants;
import com.ruijie.framework.kyo.common.extension.Key;
import com.ruijie.framework.kyo.common.extension.SPI;

@SPI("java")
public interface SerializationFactory {
	
	@Key(Constants.SESSION_SERIALIZATION_KEY)
	Serialization getSerialization();
}
