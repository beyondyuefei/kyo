package com.ruijie.framework.kyo.session.serialization.hessian;

import com.ruijie.framework.kyo.common.extension.Adaptive;
import com.ruijie.framework.kyo.session.serialization.Serialization;
import com.ruijie.framework.kyo.session.serialization.SerializationFactory;
@Adaptive("hessian")
public class HessianSerializationFactory implements SerializationFactory {

	@Override
	public Serialization getSerialization() {
		return new HessianSerialization();
	}

}
