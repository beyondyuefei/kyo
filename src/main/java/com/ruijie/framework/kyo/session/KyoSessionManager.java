package com.ruijie.framework.kyo.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.ruijie.framework.kyo.common.Constants;
import com.ruijie.framework.kyo.common.extension.ExtensionSingleSupport;
import com.ruijie.framework.kyo.common.utils.RandomUtils;
import com.ruijie.framework.kyo.common.utils.StringUtils;
import com.ruijie.framework.kyo.servlet.KyoHttpRequest;
import com.ruijie.framework.kyo.servlet.ServletContextWrapper;

/**
 * 负责维护所有session
 * 
 * @author liq
 * 
 */
public class KyoSessionManager {

	private static final Logger logger = Logger
			.getLogger(KyoSessionManager.class);

	private static Map<KyoHttpRequest, KyoSession> sessionMap = new ConcurrentHashMap<KyoHttpRequest, KyoSession>();

	public static KyoSession createNew(KyoHttpRequest request) {
		KyoSession session = new KyoSession();
		session.setCreateTime(System.currentTimeMillis());
		session.setSessionId(generatorId());
		session.setServletContext(ServletContextWrapper.getServletContext());
		session.setVaild(true);
		session.setExpire(false);
		session.setLastAccessTime(session.getCreateTime());
		session.setSessionFacade(new KyoSessionFacade(session));
		session.setNew(true);
		String timeout = Constants.get(Constants.SESSION_TIMEOUT_KEY);
		if (StringUtils.isBlank(timeout)) {
			session.setMaxInactiveInterval(Integer
					.valueOf(Constants.DEFAULT_SESSION_TIMEOUT));
		} else {
			try {
				int t = Integer.valueOf(timeout);
				if (t <= 0) {
					throw new IllegalArgumentException();
				}
				session.setMaxInactiveInterval(t);
			} catch (IllegalArgumentException e) {
				logger.error("session timeout 参数设置不正确，必须为正整数", e);
				throw e;
			}
		}

		sessionMap.put(request, session);
		logger.info("创建新的session,sessionId为:" + session.getSessionId());
		return session;
	}

	public static KyoSession getSession(KyoHttpRequest request) {
		KyoSession session = sessionMap.get(request);
		if (session != null) {
			session.setLastAccessTime(System.currentTimeMillis());
			return session;
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			String sessionCookieName = Constants.SESSION_ID_IDENTITY;
			String contextPath = request.getContextPath();
			if(!StringUtils.isBlank(contextPath)) {
				sessionCookieName += contextPath;
			}
			for (Cookie cookie : cookies) {
				if (sessionCookieName.equals(cookie.getName())) {
					String sessionId = cookie.getValue();
					if (StringUtils.isNotBlank(sessionId)) {
						session = (KyoSession) ExtensionSingleSupport.Store_EXTENSION
								.getSession(sessionId);
						if (session != null) {
							if (session.isNew()) {
								session.setNew(false);
							}
							session.setLastAccessTime(System
									.currentTimeMillis());
							session.setServletContext(ServletContextWrapper
									.getServletContext());
							session.setSessionFacade(new KyoSessionFacade(
									session));
							sessionMap.put(request, session);
							return session;
						}
					}
				}
			}
		}
		return null;
	}

	public static void sessionCommit(KyoHttpRequest request) {
		KyoSession session = sessionMap.get(request);
		sessionMap.remove(request);
		ExtensionSingleSupport.Store_EXTENSION.setSession(session,
				session.getMaxInactiveInterval() * 60);
	}

	public static void removeSession(String sessionId) {
		ExtensionSingleSupport.Store_EXTENSION.removeSession(sessionId);
	}

	private static String generatorId() {
		return RandomUtils.randomCode(Constants.DEFAULT_SESSION_ID_LENGTH);
	}
}
