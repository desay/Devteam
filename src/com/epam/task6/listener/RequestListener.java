package com.epam.task6.listener;

import org.apache.log4j.Logger;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by olga on 30.04.15.
 */
public class RequestListener implements ServletRequestListener {
    private static Logger logger = Logger.getLogger("activity");

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        String command = servletRequestEvent.getServletRequest().getParameter("executionCommand");
        HttpServletRequest request = (HttpServletRequest)servletRequestEvent.getServletRequest();
        logger.info("Request initialized. Command: " + command + " " + request.getContextPath());
    }

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        String command = servletRequestEvent.getServletRequest().getParameter("executionCommand");
        logger.info("Request destroyed. Command: " + command);
    }

}
