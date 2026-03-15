package org.xiaoyu.micro.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xiaoyu.micro.context.ApplicationContext;
import org.xiaoyu.micro.io.PropertyResolver;

import java.io.IOException;
import java.io.PrintWriter;

public class DispatcherServlet extends HttpServlet {

    final Logger logger = LoggerFactory.getLogger(getClass());

    ApplicationContext applicationContext;

    public DispatcherServlet(ApplicationContext applicationContext, PropertyResolver propertyResolver) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void destroy() {
        this.applicationContext.close();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("{} {}", req.getMethod(), req.getRequestURI());
        PrintWriter pw = resp.getWriter();
        pw.write("<h1>Hello, Xiaoyu!</h1>");
        pw.flush();
    }
}
