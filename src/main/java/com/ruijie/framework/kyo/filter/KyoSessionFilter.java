package com.ruijie.framework.kyo.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.ruijie.framework.kyo.common.Constants;
import com.ruijie.framework.kyo.common.utils.StringUtils;
import com.ruijie.framework.kyo.servlet.KyoHttpRequest;
import com.ruijie.framework.kyo.servlet.KyoHttpResponse;
import com.ruijie.framework.kyo.servlet.ServletContextWrapper;
import com.ruijie.framework.kyo.session.KyoSessionManager;

public class KyoSessionFilter implements Filter {

	private final String[] staticResources = { "gif", "jpg", "bmp", "png",
			"html", "htm", "js", "css", "mp4" };

	private static final Logger logger = Logger
			.getLogger(KyoSessionFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("开始初始化分布式session框架---- kyo");
		Constants.init();
		ServletContextWrapper.setServletContext(filterConfig
				.getServletContext());
		logger.info("分布式session框架---- kyo 初始化完成");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String reqUri = req.getRequestURI();
		// 如果是静态资源，则不需要处理
		if (StringUtils.isNotBlank(reqUri)) {
			int lastIndex = reqUri.lastIndexOf(".");
			if (lastIndex != -1) {
				String endSuffix = reqUri.substring(lastIndex + 1);
				if (isStaticResource(endSuffix)) {
					chain.doFilter(request, response);
					return;
				}
			}
		}

		KyoHttpRequest kyoRequest = new KyoHttpRequest(req);
		KyoHttpResponse kyoResponse = new KyoHttpResponse(resp);

		HttpSession session = (HttpSession) kyoRequest.getSession();
		if (session.isNew()) {
			String contextPath = req.getContextPath();
			Cookie cookie = null;
			if (StringUtils.isBlank(contextPath)) {
				cookie = new Cookie(Constants.SESSION_ID_IDENTITY,session.getId());
				cookie.setPath("/");
			} else {
				cookie = new Cookie(Constants.SESSION_ID_IDENTITY + contextPath,session.getId());
				cookie.setPath(contextPath);
			}
			resp.addCookie(cookie);
		}
		chain.doFilter(kyoRequest, kyoResponse);
		
		KyoSessionManager.sessionCommit(kyoRequest);
	}

	/**
	 * 判断是否静态资源
	 * 
	 * @param endSuffix
	 * @return
	 */
	private boolean isStaticResource(String endSuffix) {
		for (String staticResource : staticResources) {
			if (staticResource.equalsIgnoreCase(endSuffix)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
