package com.ruijie.framework.kyo.servlet;

import javax.servlet.ServletContext;

public class ServletContextWrapper { 
	
    private static ServletContext servletContext;
    
    private static boolean isLock = false;
    
    public static void setServletContext(ServletContext context) {
    	if(isLock) {
    		return;
    	}
    	servletContext = context;
    	isLock = true;
    }
    
    public static ServletContext getServletContext() {
    	return ServletContextWrapper.servletContext;
    }
}
