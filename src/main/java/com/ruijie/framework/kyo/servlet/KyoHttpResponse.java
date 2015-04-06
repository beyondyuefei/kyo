package com.ruijie.framework.kyo.servlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class KyoHttpResponse extends HttpServletResponseWrapper {

	public KyoHttpResponse(HttpServletResponse response) {
		super(response);
	}

	@Override
	public void addCookie(Cookie cookie) {
		super.addCookie(cookie);
	}

}
