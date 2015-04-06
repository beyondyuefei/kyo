package com.ruijie.framework.kyo.session.serialization.fastjson;

import com.ruijie.framework.kyo.common.extension.Adaptive;
import com.ruijie.framework.kyo.session.serialization.Serialization;
import com.ruijie.framework.kyo.session.serialization.SerializationFactory;

@Adaptive("fastjson")
public class FastjsonSerializationFactory implements SerializationFactory {

	@Override
	public Serialization getSerialization() {
		return new FastjsonSerialization();
	}

}
