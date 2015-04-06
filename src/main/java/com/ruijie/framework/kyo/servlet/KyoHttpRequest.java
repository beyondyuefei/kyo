package com.ruijie.framework.kyo.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import com.ruijie.framework.kyo.session.KyoSession;
import com.ruijie.framework.kyo.session.KyoSessionManager;

public class KyoHttpRequest extends HttpServletRequestWrapper {
	public KyoHttpRequest(HttpServletRequest request) {
		super(request);
	}

	@Override
	public HttpSession getSession(boolean create) {
		KyoSession session = KyoSessionManager.getSession(this);
		if(session == null) {
			if(create) {
				session = KyoSessionManager.createNew(this);
			}
		}
		
		return session.getSessionFacade();
	}

	@Override
	public HttpSession getSession() {
		return getSession(true);
	}
	
}
