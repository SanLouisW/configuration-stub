package com.github.configurationstub.container;


import com.github.configurationstub.MockTool;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MockLoaderServlet extends HttpServlet {
    private static final long serialVersionUID = -5368418271542329997L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MockTool.start();
    }
}
