package com.ruijie.framework.kyo.session.store;

import javax.servlet.http.HttpSession;

public interface Store {
	
    public HttpSession getSession(String sessionId);
	
    public void setSession(HttpSession session,int expireDealy);
	
    public void removeSession(String sessionId);
}
