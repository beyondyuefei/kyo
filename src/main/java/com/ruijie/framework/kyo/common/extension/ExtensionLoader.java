package com.ruijie.framework.kyo.common.extension;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import com.ruijie.framework.kyo.common.utils.Holder;
import com.ruijie.framework.kyo.common.utils.JavassistCompiler;
import com.ruijie.framework.kyo.common.utils.StringUtils;

/**
 * 
 * @author liq
 * 
 * @param <T>
 */
public class ExtensionLoader<T> {
	private Class<T> type;
	private ConcurrentHashMap<String, T> cacheInstances = new ConcurrentHashMap<String, T>();
	private final Holder<Object> cacheAdaptiveInstance = new Holder<Object>();
	private static final ConcurrentHashMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<Class<?>, ExtensionLoader<?>>();

	private ExtensionLoader(Class<T> type) {
		this.type = type;
	}

	private static <T> boolean withExtensionType(Class<T> type) {
		return type.isAnnotationPresent(SPI.class);
	}

	@SuppressWarnings("unchecked")
	public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
		if (type == null) {
			throw new IllegalArgumentException("Extension Type == null");
		}

		if (!type.isInterface()) {
			throw new IllegalArgumentException("Extension Type( " + type
					+ ") must be a interface");
		}

		if (!withExtensionType(type)) {
			throw new IllegalArgumentException("Extension Type( " + type
					+ ")  is not Extension,because do not with annotation "
					+ SPI.class.getName());
		}

		ExtensionLoader<T> loader = (ExtensionLoader<T>) EXTENSION_LOADERS
				.get(type);
		if (loader == null) {
			EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<T>(type));
			loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
		}
		return loader;
	}

	@SuppressWarnings("unchecked")
	public T getAdapativeExtension() {
		Object instance = cacheAdaptiveInstance.getValue();
		if (instance == null) {
			synchronized (cacheAdaptiveInstance) {
				instance = cacheAdaptiveInstance.getValue();
				if (instance == null) {
					instance = createAdapativeExtension();
					cacheAdaptiveInstance.setValue(instance);
				}
			}
		}
		return (T) cacheAdaptiveInstance.getValue();
	}

	@SuppressWarnings("unchecked")
	private T createAdapativeExtension() {
		ServiceLoader<T> loader = ServiceLoader.load(type);
		Iterator<T> iter = loader.iterator();
		while (iter.hasNext()) {
			T t = iter.next();
			Class<?> clazz = t.getClass();
			if (!type.isAssignableFrom(clazz)) {
				throw new IllegalStateException(
						"Error when load extension class(interface: " + type
								+ ",  class " + clazz.getName()
								+ "is not subtype of interface.");
			}
			Adaptive adaptive = clazz.getAnnotation(Adaptive.class);
			if (adaptive == null) {
				throw new IllegalStateException(
						"Error when load extension class(interface: " + type
								+ ",  class " + clazz.getName()
								+ "do not have annotation "
								+ Adaptive.class.getName());
			}

			String key = adaptive.value();
			if (StringUtils.isBlank(key)) {
				throw new IllegalStateException(
						"Error when load extension class(interface: " + type
								+ ",  class " + clazz.getName()
								+ "Adaptive annotation  must have the value");
			}

			cacheInstances.put(key, t);
		}

		String adapativeCode = createAdapatviceCode();
		try {
			return (T) JavassistCompiler.compile(adapativeCode,
					getClass().getClassLoader()).newInstance();
		} catch (Exception e) {
			throw new IllegalStateException(
					"Can not create adaptive extenstion " + type + ", cause: "
							+ e.getMessage(), e);
		}
	}

	private String createAdapatviceCode() {
		StringBuilder codeBuilder = new StringBuilder();
		codeBuilder.append("package " + type.getPackage().getName() + ";");
		codeBuilder
				.append("\n import " + ExtensionLoader.class.getName() + ";");
		codeBuilder.append("\n public class " + type.getSimpleName()
				+ "$Adapative implements " + type.getName() + "{");
		Method[] methods = type.getMethods();
		for (Method method : methods) {
			Class<?> rt = method.getReturnType();
			Class<?>[] pts = method.getParameterTypes();
			Class<?>[] ets = method.getExceptionTypes();

			codeBuilder.append("\n public " + rt.getCanonicalName() + " "
					+ method.getName() + "(");
			for (int i = 0; i < pts.length; i++) {
				if (i > 0) {
					codeBuilder.append(",");
				}
				codeBuilder.append(pts[i].getCanonicalName());
				codeBuilder.append(" ");
				codeBuilder.append("arg" + i);
			}
			codeBuilder.append(")");

			for (int i = 0; i < ets.length; i++) {
				if (i == 0) {
					codeBuilder.append(" throws ");
				}

				if (i > 0) {
					codeBuilder.append(",");
				}
				codeBuilder.append(ets[i].getCanonicalName());
			}
			codeBuilder.append(" { ");

			codeBuilder.append(" } ");
		}
		return null;
	}

	public T getExtension(String extName) {
		T t = cacheInstances.get(extName);
		if (t == null) {
			throw new IllegalArgumentException("interface " + type.getName()
					+ " do not have the subtype implements like key :"
					+ extName);
		}
		return t;
	}
}
