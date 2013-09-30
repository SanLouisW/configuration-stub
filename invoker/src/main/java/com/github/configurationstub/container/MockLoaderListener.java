package com.github.configurationstub.container;


import com.github.configurationstub.MockTool;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * User: zhaohuiyu
 * Date: 8/24/12
 * Time: 11:49 AM
 */
public class MockLoaderListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        MockTool.start();
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
