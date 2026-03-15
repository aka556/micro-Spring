package org.xiaoyu.micro.web;

import jakarta.annotation.Nullable;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface ViewResolver {

    void init();

    void render(String viewName, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws ServletException, Exception;
}
