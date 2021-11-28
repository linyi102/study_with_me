package com.example.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class OnLineListener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {
    int[] QtyofPeopleinRoom = new int[10];
    ServletContext application;

    public OnLineListener() {}

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {

    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {

    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {

    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 一定要先获取，否则application是null
        application = sce.getServletContext();
        // 把数组添加到application域对象中
        application.setAttribute("QtyofPeopleinRoomName", QtyofPeopleinRoom);
        System.out.println(">---------创建上下文参数成功！---------<");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
